package battleship.players;

import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.setup.*;
import battleship.playinggame.*;
import battleship.ui.InputManager;
import java.io.IOException;
import java.util.Scanner;

public class TwoPlayers {
    
    public static Board board1;
    public static Board board2;
    public static Fleet fleet1;
    public static Fleet fleet2;
    
    private Setup setup;
    private Shooting shooting;
    private GameFlow gameFlow;
    
    public TwoPlayers() {
        board1 = new Board(10);
        board2 = new Board(10);
        fleet1 = new Fleet();
        fleet2 = new Fleet();
        
        setup = new Setup();
        shooting = new Shooting();
        gameFlow = new GameFlow();
    }
    
    public static void TwoPlayerSetup() throws IOException {
        TwoPlayers game = new TwoPlayers();
        game.twoPlayerSetup();  // delegate to instance method  
    }
    
    //same method signature as your original
    public void twoPlayerSetup() throws IOException {
        setup.PlayerSetup(fleet1, board1, "Player 1");
        setup.PlayerSetup(fleet2, board2, "Player 2");
    }
    
    //same method signature as your original

//    public void PlayerSetup(Fleet fleet, Board board, String name) throws IOException {
//        setup.PlayerSetup(fleet, board, name);
//    }
//    
//    //same method signature as your original  
//    public void PlayerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
//        shooting.playerShoot(shooterboard, receiverfleet, receiverboard);
//    }
    
    //same method signature as your original
    public void PlayGame() throws IOException {
        InputManager.startedGame = 1;
        gameFlow.runTwoPlayerGame(board1, board2, fleet1, fleet2, shooting);
    }
    
    //keep static methods like your style
    public static void PlayerSetup(Fleet fleet, Board board, String name) throws IOException {
        Setup setup = new Setup();
        setup.PlayerSetup(fleet, board, name);
    }
    
    public static void PlayerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        Shooting shooter = new Shooting();
        shooter.playerShoot(shooterboard, receiverfleet, receiverboard);
    }
    
}