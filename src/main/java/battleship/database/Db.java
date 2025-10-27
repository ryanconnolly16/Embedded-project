package battleship.database;

import battleship.database.DbPaths;
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
}




