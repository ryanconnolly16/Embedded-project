package battleship;

import battleship.Fleet.Ship;
import java.io.IOException;
import java.io.File;
import java.util.List;
// class for testing shit

public class Test {
        public static void main(String[] args) throws IOException {

        // create 2 players
        Board player1 = new Board(10);
        Board player2 = new Board(10);
        Fleet fleet1 = new Fleet();
        
        if(UI_Output.Startup().equals("y")){
            fleet1.preset(player1);
        }
        
        
        
        
        
        // placing a ship for demonstration
        //player2.placeShip(6, 0, 5, Board.Direction.UP);
        
        // after user asks to hit (6,0) and you check that the state is hit use:
        //player1.markHit(6, 0);// marks hit on player 1's hit/miss board
        //player2.shipHit(6, 0);// marks hit on player 2's ship board
        // there are also markMiss and shipMiss
        
        // use renderBoth for rendering both boards for a player
        System.out.println(BoardRenderer.renderBoth(player1));
        
        
        // after each turn use:
        File autosave = SaveManager.writeTurnAutosave(player1, player2); // calls static method to write the current states 
        //of both players to a temp file called autosave
        
        // if the user presses 'x' check if they want to save the file and use discard or keep methods
        SaveManager.keep(autosave);
        
        // for loading save files
        FileInput input = new FileInput();

        // change name to whatever save file you need to use and save loadmatch into an array of boards
        Board[] boards = input.loadMatch(new File("save.log"));
        // save player1 to boards 0 ect...
        Board player1_new = boards[0];
        Board player2_new = boards[1];

        System.out.println(battleship.BoardRenderer.renderBoth(player2_new));
    }
}
