package battleship;

import battleship.Fleet.Ship;
import java.io.IOException;
import java.io.File;
import java.util.*;
// class for testing shit

public class Test {
    public static void main(String[] args) throws IOException {
        Test main = new Test();

        UI_Output.Startup();

        
//        
//        //Scanner input = new Scanner(System.in);
//        
//
//         Board player1 = new Board(10);
//         Board player2 = new Board(10);
//         Fleet fleet1 = new Fleet();
//         Fleet fleet2 = new Fleet();
//        
//
//
//
//        // after each turn use:
//        File autosave = SaveManager.writeTurnAutosave(player1, player2); // calls static method to write the current states 
//        //of both players to a temp file called autosave
//
//        // if the user presses 'x' check if they want to save the file and use discard or keep methods
//        SaveManager.keep(autosave);
//
//        
//        
//        
//        
//        
//        // for loading save files
//        FileInput input = new FileInput();
//
//        
//        
//        // change name to whatever save file you need to use and save loadmatch into an array of boards
//        //String path = SaveManager.getProjectFolderPath("saves");
//        //System.out.println(path);
//        SaveManager.listSaves();
//        
//        
//        Board[] boards = input.loadMatch(new File(SaveManager.getProjectFolderPath("saves") + "/" + "save_2025-08-22_23-35-22.txt"));
//        // save player1 to boards 0 ect...
//        Board player1_new = boards[0];
//        Board player2_new = boards[1];
//        System.out.println(battleship.BoardRenderer.renderBoth(player1_new));
//        System.out.println(battleship.BoardRenderer.renderBoth(player2_new));
    }
}
