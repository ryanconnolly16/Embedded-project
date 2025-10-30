package battleship.playinggame;

import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.enums.*;
import battleship.interfaces.*;
import battleship.ui.*;
import java.io.IOException;
import java.util.Scanner;

// Shooting class handles player shooting logic for GUI-based gameplay.
// Validates shot attempts and processes valid shots through the Battle class.
public class Shooting implements PlayerShooter {
    // Log message describing the player's shot action
    public static String logresult = null;
    
    // Flag indicating whether the last shot attempt was valid (true) or invalid (false)
    public static boolean value = false;
    
    // String representation of the player's shot location in display format (e.g., "A1", "B5")
    public static String usershots;
    
    // Constructor for Shooting class.
    public Shooting() {
    }
//    //function to take in where the user wants to shooter, will convert from string to int, check that shot hasnt already been done, then shoot
//    public String playershoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
//        Scanner input = new Scanner(System.in);
//        System.out.println("\n" + BoardRenderer.renderBoth(shooterboard, new DefaultGlyphs()));
//        System.out.println("\bWhere would you like to shoot?:");
//        String usershot = InputManager.getInput(input);
//        String result = null;
//        
//        
//        
//        
//        if (usershot.length() < 1 || !Character.isLetter(usershot.charAt(0)) || !Character.isDigit(usershot.charAt(1))) {
//            System.out.println("Invalid shot, try again.");
//            result = "bad1";
//            playershoot(shooterboard, receiverfleet, receiverboard);
//        }
//        
//        int xpos = Character.toLowerCase(usershot.charAt(0)) - 'a';
//        int ypos = 0;
//        if (usershot.length() == 3) {
//            if (Character.toLowerCase(usershot.charAt(2) - '1') + 1 > 0) {
//                System.out.println("Shot is out of bounds, try again");
//                result = "bad2";
//                playershoot(shooterboard, receiverfleet, receiverboard);
//            }
//            else {
//                ypos = 9;
//            }
//        }
//        else {
//            ypos = (usershot.charAt(1) - '1');
//        }
//        
//        if (xpos < 0 || ypos < 0) {
//            System.out.println("Shot out of bounds, try again.");
//            result = "bad3";
//            playershoot(shooterboard, receiverfleet, receiverboard);
//        }
//        
//        
//        
//        String xposis = Character.toString(usershot.charAt(0));
//        String yposis = String.valueOf(ypos);
//        String pos = (xposis + yposis);
//        
//        Cell trial_shot = receiverboard.cellAt(ypos, xpos, GridType.SHIPS);
//        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
//            System.out.println("\n\nShot already taken there.");
//            System.out.println("Try again.\n\n");
//            playershoot(shooterboard, receiverfleet, receiverboard);
//        }
//        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
//            Console.clearConsole();
//            if (trial_shot == Cell.SHIP){result = (pos +",hit");}
//            if(trial_shot == Cell.WATER){result = (pos +",miss");}
//            Battle.usershot(usershot, shooterboard, receiverfleet, receiverboard);
//            
//            
//        }
//        return result;
//    }
//    
    
    
    
    
    
    // Processes a player's shot attempt at the specified coordinates.
    // Validates that the cell hasn't been shot before, and if valid, processes the shot.
    // Parameters: ypos - Row coordinate (0-based) where player wants to shoot
    //             xpos - Column coordinate (0-based) where player wants to shoot
    //             shooterboard - Board of the shooting player (will be updated with shot result)
    //             receiverfleet - Fleet of the receiving player (will process damage if hit)
    //             receiverboard - Board of the receiving player (shows ship locations)
    // Throws: IOException - If there's an I/O error during shot processing
    public static void playershooting(int ypos, int xpos, Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        // Convert coordinates to lowercase string format for Battle class (e.g., "a1", "b5")
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
            // Create display format string (uppercase letter, e.g., "A1", "B5")
            char letterxpos = (char)('A' + xpos);
            usershots = "" + letterxpos + (y);
            logresult = ("\nYou fired at " + usershots + " - ");

            // Process the shot through Battle class
            Battle.usershot(usershot, shooterboard, receiverfleet, receiverboard);
            value = true;
        }
    }

    // Unused interface method - use playershooting() instead.
    // Parameters: shooterboard - Shooting player's board
    //             receiverfleet - Receiving player's fleet
    //             receiverboard - Receiving player's board
    // Returns: Unsupported operation (throws exception)
    // Throws: IOException - If operation fails
    @Override
    public String playershoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}