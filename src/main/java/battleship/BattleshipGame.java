package battleship;

import battleship.database.Db;
import battleship.database.Db.Player;
import battleship.playinggame.GameFlow;
import static battleship.database.Db.recordShot;
import battleship.ui.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BattleshipGame {
    public static void main(String[] args) throws Exception {
        try (Connection c = Db.connect()) {
            Db.ensureSchema(c);
            String home = System.getProperty("derby.system.home");
            String dbDir = java.nio.file.Path.of(home, "BattleshipDb").toAbsolutePath().toString();
            
            UiOutput.startUp(c);
        } 
        catch (SQLException ex) {
            Logger.getLogger(GameFlow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}