package battleship.players;

import battleship.playinggame.Shooting;
import battleship.setup.SetUp;

import battleship.domain.Board;
import battleship.ui.*;
import battleship.fleetplacements.*;
import battleship.playinggame.*;
import java.io.IOException;


public class OnePlayer {
    public static Board pboard;
    public static Fleet pfleet;
    public static Board aiboard;
    public static Fleet aifleet;
    
    private final SetUp setup;
    private final Shooting shooting;
    private final Ai ai;
    private final GameFlow gameFlow;
    
    //constructor
    public OnePlayer() {
        pboard = new Board(10);
        pfleet = new Fleet();
        aiboard = new Board(10);
        aifleet = new Fleet();
        
        setup = new SetUp();
        shooting = new Shooting();
        ai = new Ai(pboard, aiboard, pfleet);
        gameFlow = new GameFlow();
    }
    
    
    public static OnePlayer onePlayerSetup() throws IOException {
        OnePlayer game = new OnePlayer();
        game.oneplayerSetup(); 
        return game;
    }
    
    //will call the relevant functions to set up the ai and player boards
    public void oneplayerSetup() throws IOException {
        setup.playersetup(pfleet, pboard, "Player");
        aiSetup(aifleet, aiboard);
    }
    public static void aiSetup(Fleet aifleet, Board aiboard) {
        aifleet.preset(aifleet, aiboard);
    }
    //function to call shooter function in shooting
    public void playerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        shooting.playershoot(shooterboard, receiverfleet, receiverboard);
    }
    
    //calls GameFlow file to alternate between ai and player shots
    public void PlayGame() throws IOException {
        InputManager.startedGame = 1;
        gameFlow.runOnePlayerGame(pboard, aiboard, pfleet, aifleet, shooting, ai);
    }
    
}