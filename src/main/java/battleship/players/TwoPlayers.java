package battleship.players;

import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.setup.*;
import battleship.playinggame.*;
import battleship.ui.InputManager;
import java.io.IOException;

public class TwoPlayers {
    
    public static Board board1;
    public static Board board2;
    public static Fleet fleet1;
    public static Fleet fleet2;
    
    private SetUp setup;
    private Shooting shooting;
    private GameFlow gameFlow;
    
    //constructor
    public TwoPlayers() {
        board1 = new Board(10);
        board2 = new Board(10);
        fleet1 = new Fleet();
        fleet2 = new Fleet();
        
        setup = new SetUp();
        shooting = new Shooting();
        gameFlow = new GameFlow();
    }
    
    public static void twoPlayerSetup() throws IOException {
        TwoPlayers game = new TwoPlayers();
        game.twoplayerSetup();  
    }
    
    //function to setup the boards for 2 players
    public void twoplayerSetup() throws IOException {
        setup.playersetup(fleet1, board1, "Player 1");
        setup.playersetup(fleet2, board2, "Player 2");
    }
    
    
    //calls gameflow file to alternate between each player
    public void playGame() throws IOException {
        InputManager.startedGame = 1;
        gameFlow.runTwoPlayerGame(board1, board2, fleet1, fleet2, shooting);
    }
    
    public static void playerSetup(Fleet fleet, Board board, String name) throws IOException {
        SetUp setup = new SetUp();
        setup.playersetup(fleet, board, name);
    }
    
    public static void PlayerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        Shooting shooter = new Shooting();
        shooter.playershoot(shooterboard, receiverfleet, receiverboard);
    }
    
}