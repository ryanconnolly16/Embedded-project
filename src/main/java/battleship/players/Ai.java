package battleship.players;

import battleship.playinggame.Battle;
import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.enums.*;
import battleship.gui_setup.SetupController;
import battleship.interfaces.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//ai class for when its one player
public class Ai implements AiShooter {
    public static String logresult;
    public static String usershot;
    public Ai(Board playerboard, Board aiboard, Fleet playerfleet) {
    }
    
    @Override
    public String aiShoot(Board aiboard, Fleet playerfleet, Board playerboard) {
        Random rand = new Random();
        int xpos = rand.nextInt(10);
        int ypos = rand.nextInt(10);
        String result = null;
        //checking what state the chosen cell is
        Cell trial_shot = playerboard.cellAt(ypos, xpos, GridType.SHIPS);
        
        
        String xposis = String.valueOf((char) ('A' + xpos));
        String yposis = String.valueOf(ypos);
        String pos = (xposis + yposis);
        
        //checking if they already shot in that space
        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
            aiShoot(aiboard, playerfleet, playerboard);
        }
        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
            ypos = ypos + 1;
            char letterxpos = (char)('a' + xpos);
            String usershot = "" + letterxpos + ypos;
            System.out.println("\nThe ai shoots at " + usershot);
            if (trial_shot == Cell.SHIP){result = (pos +",hit");}
            if(trial_shot == Cell.WATER){result = (pos +",miss");}
            Battle.usershot(usershot, aiboard, playerfleet, playerboard);
            
        }
        return result;
    }
    
    
    public static List<String> checking = new ArrayList<>();
    public static void AiShot(Board aiboard, Fleet playerfleet, Board playerboard) {
        Random rand = new Random();
        int xpos = rand.nextInt(10);
        int ypos = rand.nextInt(10);
        
        Point result = null;
        //checking what state the chosen cell is
        Cell trial_shot = playerboard.cellAt(ypos, xpos, GridType.SHIPS);
        
        
        String xposis = String.valueOf((char) ('a' + xpos));
        String yposis = String.valueOf(ypos);
        System.out.println(ypos);
        String pos = (xposis + yposis);
        
        
        char letterxpos = (char)('A' + xpos);
        usershot = "" + letterxpos + (ypos+1);
        
        
        
        //checking if they already shot in that space
        if (SetupController.aivisited[xpos][ypos]== true || checking.contains(usershot) ||
                trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
            System.out.println("reset");
            AiShot(aiboard, playerfleet, playerboard);
        }
        
        
        if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
//            ypos++;
            
            SetupController.aivisited[xpos][ypos] = true;
            System.out.println("added");
            checking.add(usershot);
            logresult = ("\nAi fired at " + usershot + " - ");
            
            
            Battle.aishot(ypos, xpos, aiboard, playerfleet, playerboard);
            
        }
        System.out.println(checking);
    }
    
    
    
}