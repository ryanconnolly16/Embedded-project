package battleship;

import battleship.io.SaveManager;
import battleship.domain.Board;
import battleship.enums.GridType;
import battleship.enums.Cell;
import java.io.*;
import java.util.*;

public class OnePlayerold extends TwoPlayers {
    
    //global variables for each fleet and board
    static Board pboard = new Board(10);
    static Fleet pfleet = new Fleet();
    static Board aiboard = new Board(10);
    static Fleet aifleet = new Fleet();
    
    public static void OnePlayerSetup() throws IOException {
        PlayerSetup(pfleet, pboard, "Player");
        AiSetup(aifleet, aiboard);
    }

    //setting up aiboard with preset function in fleet class
    public static void AiSetup(Fleet aifleet, Board aiboard) {
        aifleet.Preset(aifleet, aiboard);
    }
    
    //will alternate between player and ai shots until either fleet is sunk
    @Override
    public void PlayGame() throws IOException {
        while (!pfleet.allSunk() || !aifleet.allSunk()) {
            AiShoot();
            //inhertited from TwoPlayers
            PlayerShoot(pboard, aifleet, aiboard);
            UI_Output.autosave = SaveManager.writeTurnAutosave(pboard, aiboard);
        }
    }
    
    //using random function to chose a square, checks if already shot there
    public static void AiShoot() {
        Random rand = new Random();
        int xpos = rand.nextInt(10);
        int ypos = rand.nextInt(10);
        
        //checking what state the chosen cell is
        Cell trial_shot = pboard.cellAt(ypos, xpos, GridType.SHIPS);
        
        //checking if they already shot in that space 
        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
            AiShoot();
        }
        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
            ypos++;
            char letterxpos = (char)('a' + xpos);
            String usershot = "" + letterxpos + ypos;
            System.out.println("\nThe ai shoots at " + usershot);
            Battle.usershot(usershot, aiboard, pfleet, pboard);
        }
    }
}
