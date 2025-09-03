package battleship.playinggame;

import battleship.Fleet;
import battleship.domain.Board;
import battleship.interfaces.*;
import battleship.io.SaveManager;
import battleship.ui.*;
import java.io.IOException;

public class GameFlow implements GameRunner {
    public GameFlow() {
    }
    
    //match your two-player game loop structure
    public void runTwoPlayerGame(Board board1, Board board2, Fleet fleet1, Fleet fleet2, Shooting shooter) throws IOException {
        while (!fleet1.allSunk() || !fleet2.allSunk()) {
            Console.ClearConsole();
            System.out.println("Player one's turn:");
            shooter.playerShoot(board1, fleet2, board2);
            Console.ClearConsole();
            System.out.println("Player two's turn:");
            shooter.playerShoot(board2, fleet1, board1);
            InputManager.autosave = SaveManager.writeTurnAutosave(board1, board2);
        }
    }
    
    @Override
    public void playGame() throws IOException {
    }
}