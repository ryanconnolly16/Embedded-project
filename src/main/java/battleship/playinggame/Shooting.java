package battleship.playinggame;

import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.enums.*;
import battleship.interfaces.*;
import battleship.ui.*;
import java.io.IOException;
import java.util.Scanner;

// Shooting class handles player shooting logic for GUI-based gameplay.
public class Shooting implements PlayerShooter {
    public static String logresult = null;
    public static boolean value = false;
    public static String usershots;
    
    public Shooting() {}
    
    // Processes a player's shot attempt at the specified coordinates.
    public static void playershooting(int ypos, int xpos, Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        // Convert coordinates to lowercase string format for Battle class
        String x = String.valueOf((char)('a' + (xpos)));
        String y = Integer.toString(ypos+1);
        String usershot = x + y;
        
        // Check if this cell has already been shot at
        Cell trial_shot = receiverboard.cellAt(ypos, xpos, GridType.SHIPS);
        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
            // Cell already shot - invalid attempt
            value = false;
        }
        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
            // Valid shot - cell hasn't been shot yet
            char letterxpos = (char)('A' + xpos);
            usershots = "" + letterxpos + (y);
            logresult = ("\nYou fired at " + usershots + " - ");

            // Process the shot through Battle class
            Battle.usershot(usershot, shooterboard, receiverfleet, receiverboard);
            value = true;
        }
    }
    @Override
    public String playershoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}