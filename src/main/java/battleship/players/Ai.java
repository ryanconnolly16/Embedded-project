package battleship.players;

import battleship.playinggame.Battle;
import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.enums.*;
import battleship.gui_setup.SetupController;
import battleship.interfaces.*;
import java.util.ArrayList;
import java.util.List;


// Handles AI shooting logic \
public class Ai implements AiShooter {
    // Log message describing the AI's shot action
    public static String logresult;
    
    // String representation of the AI's shot location
    public static String usershot;
    
    public Ai(Board playerboard, Board aiboard, Fleet playerfleet) {
    }
    
    // List tracking shot locations that have been attempted by the AI
    public static List<String> checking = new ArrayList<>();
    
    
    public static void AiShot(Board aiboard, Fleet playerfleet, Board playerboard) {
        java.util.concurrent.ThreadLocalRandom rand = java.util.concurrent.ThreadLocalRandom.current();
        
        
        // Check how many cells have been shot at by counting HIT/MISS on the board
        int triedCount = 0;
        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++) {
                Cell shotCell = aiboard.cellAt(y, x, GridType.SHOTS);
                if (shotCell == Cell.HIT || shotCell == Cell.MISS) triedCount++;
            }
        if (triedCount >= 100) return;
        
        
        // Add maximum iteration limit so it wont hang
        int maxAttempts = 1000;
        int attempts = 0;
        
        while(attempts < maxAttempts){
            attempts++;
            int xpos = rand.nextInt(10);
            int ypos = rand.nextInt(10);

            // Convert coordinates to a5 format
            char letterxpos = (char)('A' + xpos);
            usershot = "" + letterxpos + (ypos+1);

            // getting state of cell shooting at
            Cell aiShotCell = aiboard.cellAt(ypos, xpos, GridType.SHOTS);
            
            // skiping if it has already shot there
            if (aiShotCell == Cell.HIT || aiShotCell == Cell.MISS) {
                // Keep aivisited in sync
                SetupController.aivisited[ypos][xpos] = true;
                continue;
            }

            // will shot at cell
            SetupController.aivisited[ypos][xpos] = true;
            checking.add(usershot);
            logresult = ("\nAi fired at " + usershot + " - ");

            Battle.aishot(ypos, xpos, aiboard, playerfleet, playerboard);
            return;
        }
        
        // If couldnt get valid cell, will log error and return
        System.err.println("AI failed to find valid shot after " + maxAttempts + " attempts");
        logresult = "\nAi error - no valid shot found";
    }

    @Override
    public String aiShoot(Board aiboard, Fleet pfleet, Board pboard) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}