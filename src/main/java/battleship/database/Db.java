package battleship.database;

import battleship.database.DbPaths;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.nio.file.Path;

public final class Db {
    private Db() {}

    // Database name on disk (folder name under derby.system.home)
    private static final String DB_NAME = "BattleshipDb";
    private static final String URL = "jdbc:derby:" + DB_NAME + ";create=true";

    static {
        Path home = battleship.database.DbPaths.derbyHome(); // returns an absolute, OS-appropriate folder
        System.setProperty("derby.system.home", home.toString());
        String url = "jdbc:derby:BattleshipDb;create=true";

    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void ensureSchema(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            // Be explicit about schema
            st.executeUpdate("SET SCHEMA APP");

            try {
                st.executeUpdate("""
                    CREATE TABLE PLAYER1(
                      ShotNum   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      GridSpace VARCHAR(64) NOT NULL,
                      HitMiss   VARCHAR(64) NOT NULL
                    )
                """);
            } catch (SQLException e) { if (!"X0Y32".equals(e.getSQLState())) throw e; }
            
            try {
                st.executeUpdate("""
                  CREATE TABLE PLAYER2(
                    ShotNum   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    GridSpace VARCHAR(64)  NOT NULL,
                    HitMiss   VARCHAR(64)  NOT NULL
                  )
                """);
            } catch (SQLException e) { if (!"X0Y32".equals(e.getSQLState())) throw e; }   
            try {
                st.executeUpdate("""
                                 CREATE TABLE GameState
                                 (slot VARCHAR(32) PRIMARY KEY, 
                                 content CLOB)
                                 """);
            } catch (SQLException e) { if (!"X0Y32".equals(e.getSQLState())) throw e; }
        }
    }
    
    public enum Player { PLAYER1, PLAYER2 }
    
    public static void recordShot(Connection c, Player who, String grid, String result) throws SQLException {
        final String table = (who == Player.PLAYER1) ? "PLAYER1" : "PLAYER2";
        try (var ps = c.prepareStatement("INSERT INTO " + table + " (GridSpace, HitMiss) VALUES (?, ?)")) {
            ps.setString(1, grid);
            ps.setString(2, result);
            ps.executeUpdate();
        }
    }


    
    public static void shutdownQuietly() {
        try { DriverManager.getConnection("jdbc:derby:;shutdown=true"); }
        catch (SQLException e) { if (!"XJ015".equals(e.getSQLState())) e.printStackTrace(); }
    }
    
    
    public static void overwriteOrInsert(Connection c, String slot, File f) throws Exception {
        int n;
        try (PreparedStatement up = c.prepareStatement("UPDATE GameState SET content=? WHERE slot=?");
             Reader r1 = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
          up.setCharacterStream(1, r1);
          up.setString(2, slot);
          n = up.executeUpdate();
        }
        if (n == 0) {
          try (PreparedStatement ins = c.prepareStatement("INSERT INTO GameState(slot,content) VALUES(?,?)");
               Reader r2 = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
            ins.setString(1, slot);
            ins.setCharacterStream(2, r2);
            ins.executeUpdate();
          }
        }
      }
    
//    
//    public static long saveTempTextFile(Connection c, String name, File tmp) throws Exception {
//        String sql = "INSERT INTO PASTSAVE(name, content) VALUES (?, ?)";
//        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//             Reader r = new BufferedReader(new InputStreamReader(
//                         new FileInputStream(tmp), StandardCharsets.UTF_8))) {
//            ps.setString(1, name);
//            // length can be omitted with recent Derby; if needed use tmp.length()
//            ps.setCharacterStream(2, r); 
//            ps.executeUpdate();
//            try (ResultSet k = ps.getGeneratedKeys()) {
//                return k.next() ? k.getLong(1) : -1L;
//            }
//        }
//    }

}
