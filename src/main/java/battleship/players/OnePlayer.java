package battleship.players;

import battleship.playinggame.Shooting;
import battleship.setup.Setup;

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
    
    private final Setup setup;
    private final Shooting shooting;
    private final Ai ai;
    private final GameFlow gameFlow;
    
    //constructor
    public OnePlayer() {
        pboard = new Board(10);
        pfleet = new Fleet();
        aiboard = new Board(10);
        aifleet = new Fleet();
        
        setup = new Setup();
        shooting = new Shooting();
        ai = new Ai(pboard, aiboard, pfleet);
        gameFlow = new GameFlow();
    }
    
    
    public static OnePlayer OnePlayerSetup() throws IOException {
        OnePlayer game = new OnePlayer();
        game.onePlayerSetup(); 
        return game;
    }
    
    //will call the relevant functions to set up the ai and player boards
    public void onePlayerSetup() throws IOException {
        setup.PlayerSetup(pfleet, pboard, "Player");
        AiSetup(aifleet, aiboard);
    }
    public static void AiSetup(Fleet aifleet, Board aiboard) {
        aifleet.Preset(aifleet, aiboard);
    }
    //function to call shooter function in shooting
    public void PlayerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        shooting.playerShoot(shooterboard, receiverfleet, receiverboard);
    }
    
    //calls GameFlow file to alternate between ai and player shots
    public void PlayGame() throws IOException {
        InputManager.startedGame = 1;
        gameFlow.runOnePlayerGame(pboard, aiboard, pfleet, aifleet, shooting, ai);
    }
    
}