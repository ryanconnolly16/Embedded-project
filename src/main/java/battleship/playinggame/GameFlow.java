//package battleship.playinggame;
//
//import battleship.database.Db;
//import static battleship.database.Db.recordShot;
//import battleship.fleetplacements.*;
//import battleship.domain.Board;
//import battleship.interfaces.*;
//import battleship.io.SaveManager;
//import battleship.players.Ai;
//import battleship.ui.*;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class GameFlow implements GameRunner {
//    public GameFlow() {
//    }
//    String dbshot=null;
//    String gridplace = null;
//    String gridstatus = null;
//    String[] s;
//    
//    //function for twoplayers, will alternate between each player
//    public void runTwoPlayerGame(Board board1, Board board2, Fleet fleet1, Fleet fleet2, Shooting shooter,Connection c) throws IOException, SQLException {
////        try (Connection c = Db.connect()) {
////            Db.ensureSchema(c);
////            String home = System.getProperty("derby.system.home");
////            String dbDir = java.nio.file.Path.of(home, "BattleshipDb").toAbsolutePath().toString();
////            System.out.println("derby.system.home = " + home);
////            System.out.println("DB directory      = " + dbDir);
////            
////            
//            while (!fleet1.allSunk() || !fleet2.allSunk()) {
//                Console.clearConsole();
//                System.out.println("Player one's turn:");
//                dbshot = shooter.playershoot(board1, fleet2, board2);
//                if (!dbshot.equals("No")){}
//                else {
//                    s = dbshot.split(",");
//                    gridplace = s[0];
//                    gridstatus = s[1];
//                    recordShot(c, Db.Player.PLAYER1, gridplace, gridstatus);
//                    if (!c.getAutoCommit()) c.commit();
//                }              
//                
//                
//                
//                Console.clearConsole();
//                System.out.println("Player two's turn:");
//                dbshot = shooter.playershoot(board2, fleet1, board1);
//                if (!dbshot.equals("No")){}
//                else {
//                    if (!dbshot.isEmpty()){
//                        s = dbshot.split(",");
//                        gridplace = s[0];
//                        gridstatus = s[1];
//                        recordShot(c, Db.Player.PLAYER2, gridplace, gridstatus);
//                    }
//                    if (!c.getAutoCommit()) c.commit();
//                }
//                
//                
//                
//                
//                InputManager.autosave = SaveManager.writeTurnAutosave(board1, board2);
//                
//                
//                
//                
//                
//                
//            }
////        }
////        catch (SQLException ex) {    
////            Logger.getLogger(GameFlow.class.getName()).log(Level.SEVERE, null, ex);
////        }    
////        finally {
////            Db.shutdownQuietly();
////        }
//    }
//    
//    
//    
//    
//    //function for oneplayer, will alternate between player and ai
//    public void runOnePlayerGame(Board pboard, Board aiboard, Fleet pfleet, Fleet aifleet, Shooting shooter, Ai ai, Connection c)throws IOException, Exception {
//        
//            while (!pfleet.allSunk() || !aifleet.allSunk()) {
//                InputManager.startedGame = 1;
//                
//
//
//                dbshot = ai.aiShoot(aiboard, pfleet, pboard);
//                s = dbshot.split(",");
//                gridplace = s[0];
//                gridstatus = s[1];
//                recordShot(c, Db.Player.PLAYER2, gridplace, gridstatus);
//                if (c.getAutoCommit()) c.commit();
//                
//                dbshot = shooter.playershoot(pboard, aifleet, aiboard);
//                if (dbshot.contains("bad")){}
//                else {
//                    if (!dbshot.isEmpty()){
//                        s = dbshot.split(",");
//                        gridplace = s[0];
//                        gridstatus = s[1];
//                        recordShot(c, Db.Player.PLAYER1, gridplace, gridstatus);
//                        if (!c.getAutoCommit()) c.commit();
//                    }
//                }
//                InputManager.autosave = SaveManager.writeTurnAutosave(pboard, aiboard);
//                System.out.println(InputManager.autosave);
//                String sql = "DELETE FROM PASTSAVE WHERE id = ?";
//                try (PreparedStatement ps = c.prepareStatement(sql)) {
//                    ps.setLong(1, 1);
//                    ps.executeUpdate();
//                }
//                
//                
//                Db.overwriteOrInsert(c, "current", InputManager.autosave);
//                
//                
//                
//                
//            }
//       
//    }
//    
//    
//    @Override
//    public void playGame() throws IOException {
//    }
//}