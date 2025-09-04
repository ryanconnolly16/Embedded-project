package battleship.playinggame;

import battleship.fleetplacements.*;
import battleship.domain.Board;
import battleship.interfaces.*;
import battleship.io.SaveManager;
import battleship.players.Ai;
import battleship.ui.*;
import java.io.IOException;

public class GameFlow implements GameRunner {
    public GameFlow() {
    }
    
    //function for twoplayers, will alternate between each player
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
    
    //function for oneplayer, will alternate between player and ai
    public void runOnePlayerGame(Board pboard, Board aiboard, Fleet pfleet, Fleet aifleet, Shooting shooter, Ai ai)throws IOException {
        while (!pfleet.allSunk() || !aifleet.allSunk()) {
            InputManager.startedGame = 1;
            ai.aiShoot();
            shooter.playerShoot(pboard, aifleet, aiboard);
            InputManager.autosave = SaveManager.writeTurnAutosave(pboard, aiboard);
        }
    }
    
    
    @Override
    public void playGame() throws IOException {
    }
}