package battleship.players;

import battleship.playinggame.Battle;
import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.enums.*;
import battleship.interfaces.*;
import java.awt.Point;
import java.util.Random;

//ai class for when its one player
public class Ai implements AiShooter {
    public static String logresult;
    
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
            ypos++;
            char letterxpos = (char)('a' + xpos);
            String usershot = "" + letterxpos + ypos;
            System.out.println("\nThe ai shoots at " + usershot);
            if (trial_shot == Cell.SHIP){result = (pos +",hit");}
            if(trial_shot == Cell.WATER){result = (pos +",miss");}
            Battle.usershot(usershot, aiboard, playerfleet, playerboard);
            
        }
        return result;
    }

    
    public static void AiShot(Board aiboard, Fleet playerfleet, Board playerboard) {
        Random rand = new Random();
        int xpos = rand.nextInt(10);
        int ypos = rand.nextInt(10);
        Point result = null;
        //checking what state the chosen cell is
        Cell trial_shot = playerboard.cellAt(ypos, xpos, GridType.SHIPS);
        
        
        String xposis = String.valueOf((char) ('a' + xpos));
        String yposis = String.valueOf(ypos);
        String pos = (xposis + yposis);
        
        //checking if they already shot in that space
        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
            AiShot(aiboard, playerfleet, playerboard);
        }
        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
            ypos++;
            char letterxpos = (char)('A' + xpos);
            String usershot = "" + letterxpos + ypos;
            logresult = ("\nAi fired at " + usershot + " - ");
            
            
            Battle.usershot(pos, aiboard, playerfleet, playerboard);
            
        }
        
    }
    
    
    
}