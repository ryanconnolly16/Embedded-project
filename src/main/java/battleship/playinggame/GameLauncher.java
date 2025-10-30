//
//package battleship.playinggame;
//
//
//import battleship.interfaces.*;
//import battleship.ui.*;
//import battleship.io.*;
//import battleship.players.*;
//import java.io.IOException;
//import java.sql.Connection;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
////function to get how many players are playing, thens asks if they want to load a save, then will play game
//public class GameLauncher implements GameStarter {
//    private final  InputManager input;
//    private final LoadGame save;
//    
//    public GameLauncher() {
//        input = new InputManager();
//        save = new LoadGame();
//    }
//    
//    public void playeramount(Connection c) throws IOException, Exception {
//        System.out.println("Are you playing with one or two people?");
//        String amount = input.getinput().trim();
//        while(true){
//            if(amount.equals("1") || amount.equalsIgnoreCase("one")){
//                OnePlayer oneplayer = new OnePlayer();
//                save.loadsavedgame(1,c);
//                
//                oneplayer.PlayGame(c);
//            }
//            else if(amount.equals("2") || amount.equalsIgnoreCase("two")){
//                TwoPlayers twoplayers = new TwoPlayers();
//                save.loadsavedgame(2,c);
//                
//                twoplayers.playGame(c);
//            }
//            else{
//                System.out.println("Invalid input, try agin.");
//                playeramount(c);
//                return;
//            }
//        }
//    }
//    
////    @Override
//    public void startGame(Connection c) throws IOException {
//        try {
//            playeramount(c);
//        } catch (Exception ex) {
//            Logger.getLogger(GameLauncher.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    @Override
//    public void startGame() throws IOException {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//}