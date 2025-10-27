package battleship;

import battleship.database.Db;
import battleship.database.Db.Player;
import static battleship.database.Db.recordShot;
import battleship.ui.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BattleshipGame {
    public static void main(String[] args) throws Exception {
        UiOutput.startUp();
    }
}



//public class BattleshipGame {
//    public static void main(String[] args) throws Exception {
//        try (Connection c = Db.connect()) {
//            // Create tables if missing (ignores "already exists")
//            Db.ensureSchema(c);
//            String home = System.getProperty("derby.system.home");
//            String dbDir = java.nio.file.Path.of(home, "BattleshipDb").toAbsolutePath().toString();
//            System.out.println("derby.system.home = " + home);
//            System.out.println("DB directory      = " + dbDir);
//        
//
//            recordShot(c, Player.PLAYER1, "A2", "Hit");
//            recordShot(c, Player.PLAYER1, "B3", "Miss");
//            recordShot(c, Player.PLAYER2, "J10", "Miss");
//
//            listShots(c, Player.PLAYER1);
//            listShots(c, Player.PLAYER2);
//            if (!c.getAutoCommit()) c.commit();
//
//        }
//    
//        finally {
//            Db.shutdownQuietly();
//        }
//    }
//}
