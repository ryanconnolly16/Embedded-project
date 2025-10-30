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

    // searchs for battleship database in files
    private static final String DB_NAME = "BattleshipDb";
    private static final String URL = "jdbc:derby:" + DB_NAME + ";create=true";

    //returns filepath
    static {
        Path home = battleship.database.DbPaths.derbyHome(); 
        System.setProperty("derby.system.home", home.toString());
        String url = "jdbc:derby:BattleshipDb;create=true";

    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    //makes sure the data bases have been made
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

    //function to record each player shot
    public static void recordShot(Connection c, Player who, String grid, String result) throws SQLException {
        final String table = (who == Player.PLAYER1) ? "PLAYER1" : "PLAYER2";
        try (var ps = c.prepareStatement("INSERT INTO " + table + " (GridSpace, HitMiss) VALUES (?, ?)")) {
            ps.setString(1, grid);
            ps.setString(2, result);
            ps.executeUpdate();
        }
    }

    // Call this once on startup
    public static void clearOnStartup(java.sql.Connection c) throws java.sql.SQLException {
        try (java.sql.Statement st = c.createStatement()) {
            st.executeUpdate("TRUNCATE TABLE Player1");   // <-- your table name
            st.executeUpdate("TRUNCATE TABLE Player2");
        }
    }

    

    
    public static void shutdownQuietly() {
        try { DriverManager.getConnection("jdbc:derby:;shutdown=true"); }
        catch (SQLException e) { if (!"XJ015".equals(e.getSQLState())) e.printStackTrace(); }
    }
    
    //overwrites what is the in save file in the database
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
    
    
    // Delete Derby .lck files that can mess with running
    public static void deleteDerbyLocks() {
        String home = System.getProperty("derby.system.home");
        try (java.nio.file.DirectoryStream<java.nio.file.Path> ds =
                 java.nio.file.Files.newDirectoryStream(java.nio.file.Path.of(home, "BattleshipDb").toAbsolutePath(), "*.lck")) {
            
            for (java.nio.file.Path p : ds) java.nio.file.Files.deleteIfExists(p);
        } catch (java.io.IOException ignored) {}
    }

}
