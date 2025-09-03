
package battleship;

import java.io.*;
import java.io.IOException;
import java.util.*;
public class OnePlayer extends TwoPlayers {
    
    //global variables for each fleet and board
    static Board pboard = new Board(10);
    static Fleet pfleet = new Fleet();
    static Board aiboard = new Board(10);
    static Fleet aifleet = new Fleet();
    
    public static void OnePlayerSetup() throws IOException{
        PlayerSetup(pfleet,pboard, "Player");
        AiSetup(aifleet, aiboard);
    }

    //setting up aiboard with preset function in fleet class
    public static void AiSetup(Fleet aifleet, Board aiboard){
        aifleet.Preset(aifleet, aiboard);
        
    }
    
    //will alternate between player and ai shots until either fleet is sunk
    @Override
    public void PlayGame() throws IOException{
        while(!pfleet.allSunk() || !aifleet.allSunk()){
            AiShoot();
            //inhertited from TwoPlayers
            PlayerShoot(pboard, aifleet, aiboard);
            UI_Output.autosave = SaveManager.writeTurnAutosave(pboard, aiboard);
            
        }
    }
    
    //using random function to chose a square, checks if already shot their
    public static void AiShoot(){
        Random rand = new Random();
        int xpos = rand.nextInt(10);
        int ypos = rand.nextInt(10);
        //checking what state the chosen cell is
        char trial_shot = pboard.cellAt(ypos, xpos, Board.GridType.SHIPS);
        
        //checking if they already shot in that space 
        if (trial_shot == 'X' || trial_shot == 'O'){
            AiShoot();
        }
        else if (trial_shot == ' ' || trial_shot == 'S'){
            ypos ++;
            char letterxpos = (char)('a' + xpos);
            String usershot = "" + letterxpos + ypos;
            System.out.println("\nThe ai shoots at " + usershot);
            Battle.usershot(usershot, aiboard, pfleet, pboard);
        }
    }
}
