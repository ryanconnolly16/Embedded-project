package battleship.players;

import battleship.playinggame.Battle;
import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.enums.*;
import battleship.gui_setup.SetupController;
import battleship.interfaces.*;
import java.util.ArrayList;
import java.util.List;

// AI player implementation for one-player battleship games.
// Handles AI shooting logic including shot selection and tracking.
public class Ai implements AiShooter {
    // Log message describing the AI's shot action
    public static String logresult;
    
    // String representation of the AI's shot location (e.g., "A1", "B5")
    public static String usershot;
    
    // Constructor for AI player.
    // Parameters: playerboard - Board representing the player's board
    //             aiboard - Board representing the AI's board (tracks AI's shots)
    //             playerfleet - Fleet representing the player's ships
    public Ai(Board playerboard, Board aiboard, Fleet playerfleet) {
    }
    
//    @Override
//    public String aiShoot(Board aiboard, Fleet playerfleet, Board playerboard) {
//        Random rand = new Random();
//        int xpos = rand.nextInt(10);
//        int ypos = rand.nextInt(10);
//        String result = null;
//        //checking what state the chosen cell is
//        Cell trial_shot = playerboard.cellAt(ypos, xpos, GridType.SHIPS);
//        
//        
//        String xposis = String.valueOf((char) ('A' + xpos));
//        String yposis = String.valueOf(ypos);
//        String pos = (xposis + yposis);
//        
//        //checking if they already shot in that space
//        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
//            aiShoot(aiboard, playerfleet, playerboard);
//        }
//        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
//            ypos = ypos + 1;
//            char letterxpos = (char)('a' + xpos);
//            String usershot = "" + letterxpos + ypos;
//            System.out.println("\nThe ai shoots at " + usershot);
//            if (trial_shot == Cell.SHIP){result = (pos +",hit");}
//            if(trial_shot == Cell.WATER){result = (pos +",miss");}
//            Battle.usershot(usershot, aiboard, playerfleet, playerboard);
//            
//        }
//        return result;
//    }
//    
    
    // List tracking shot locations that have been attempted by the AI
    public static List<String> checking = new ArrayList<>();
    
    // Main AI shooting method. Selects a random valid cell to shoot at on the player's board.
    // Uses the AI's shots grid to determine which cells have already been shot at.
    // Parameters: aiboard - The AI's board (tracks where AI has shot)
    //             playerfleet - The player's fleet (used to process hits)
    //             playerboard - The player's board (shows ship locations)
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
        
        
        // Add maximum iteration limit to prevent infinite loops
        int maxAttempts = 1000;
        int attempts = 0;
        
        while(attempts < maxAttempts){
            attempts++;
            int xpos = rand.nextInt(10);
            int ypos = rand.nextInt(10);

            // Convert coordinates to human-readable format (e.g., "A1", "B5")
            char letterxpos = (char)('A' + xpos);
            usershot = "" + letterxpos + (ypos+1);

            // Check if AI has already shot at this location by checking the AI's SHOTS grid
            Cell aiShotCell = aiboard.cellAt(ypos, xpos, GridType.SHOTS);
            
            // Skip if already shot (HIT/MISS on AI's shots grid) - this is the source of truth
            // The aivisited array should match, but we rely on the board state
            if (aiShotCell == Cell.HIT || aiShotCell == Cell.MISS) {
                // Keep aivisited in sync
                SetupController.aivisited[ypos][xpos] = true;
                continue;
            }

            // Found a valid cell to shoot at - mark it as visited and fire
            SetupController.aivisited[ypos][xpos] = true;
            checking.add(usershot);
            logresult = ("\nAi fired at " + usershot + " - ");

            Battle.aishot(ypos, xpos, aiboard, playerfleet, playerboard);
            return;
        }
        
        // If we couldn't find a valid cell after maxAttempts, log error and return
        System.err.println("AI failed to find valid shot after " + maxAttempts + " attempts");
        logresult = "\nAi error - no valid shot found";
    }

    // Unused interface method - use AiShot() instead.
    // Parameters: aiboard - AI's board, pfleet - Player's fleet, pboard - Player's board
    // Returns: Unsupported operation (throws exception)
    @Override
    public String aiShoot(Board aiboard, Fleet pfleet, Board pboard) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}