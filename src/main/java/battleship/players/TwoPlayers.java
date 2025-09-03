package battleship.players;

import battleship.Fleet;
import battleship.domain.*;
import battleship.setup.*;
import battleship.playinggame.*;
import java.io.IOException;
import java.util.Scanner;

public class TwoPlayers {
    
    protected Board board1;
    protected Board board2;
    protected Fleet fleet1;
    protected Fleet fleet2;
    
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
    
    //same method signature as your original
    public void TwoPlayerSetup() throws IOException {
        setup.PlayerSetup(fleet1, board1, "Player 1");
        setup.PlayerSetup(fleet2, board2, "Player 2");
    }
    
    //same method signature as your original

    public void PlayerSetup(Fleet fleet, Board board, String name) throws IOException {
        setup.PlayerSetup(fleet, board, name);
    }
    
    //same method signature as your original  
    public void PlayerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        shooting.playerShoot(shooterboard, receiverfleet, receiverboard);
    }
    
    //same method signature as your original
    public void PlayGame() throws IOException {
        gameFlow.runTwoPlayerGame(board1, board2, fleet1, fleet2, shooting);
    }
}