package battleship.playinggame;

import battleship.fleetplacements.*;
import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;

// Battle class handles the core game logic for shooting mechanics.
// Processes shots for both players and AI, determines hits/misses,
// and updates board states accordingly.
public class Battle {
    // Result string indicating whether the last shot was a "Hit" or "Miss".
    // May include additional information like "SUNK! ShipName" if a ship was sunk.
    public static String hitmiss;
    
    
    
    // Processes a player's shot against the opponent's board.
    // Parses the shot location string, determines hit/miss based on board state,
    // updates both boards, and processes fleet damage.
    // Parameters: usershot - String representation of shot location (e.g., "a1", "b5", "j10")
    //             p1board - Board of the shooting player (tracks their shots)
    //             p2fleet - Fleet of the receiving player (tracks ship damage)
    //             p2board - Board of the receiving player (shows ship locations)
    public static void usershot(String usershot, Board p1board, Fleet p2fleet, Board p2board){
        // Only process shot if opponent's fleet hasn't been completely sunk
        if(!p2fleet.allSunk()){
            int col;
            int row;
            
            // Parse shot coordinates from string format
            // Handle numeric-numeric format (e.g., "11" for row 1, col 1)
            if (!Character.isLetter(usershot.charAt(0))) {
                col = usershot.charAt(0) - '1' + 1;
                row = usershot.charAt(1) - '1' + 1;
            }
            // Handle letter-numeric format (e.g., "a1", "b5")
            else{
                col = usershot.charAt(0) - 'a';
                row = usershot.charAt(1) - '1';
            }
            
            // Handle row 10 (two-digit number, e.g., "a10", "j10")
            if(usershot.length() > 2){
               row = 9; // Row 10 is index 9 (0-based)
            }
            
            // Validate coordinates are within bounds
            if(col > 9 || row > 10){
                System.out.println("Input is out of bounds, try again.\n\n");
            }

            else{
                // Check the board's SHIPS grid to determine if there's a ship (source of truth)
                Cell shipCell = p2board.cellAt(row, col, GridType.SHIPS);
                Fleet.Ship hit = p2fleet.processHit(row, col);
                
                // Use board state to determine hit/miss, processHit just tracks fleet health
                if(shipCell == Cell.SHIP || shipCell == Cell.HIT){
                    // There's a ship at this location (or was, if already hit)
                    p1board.markHit(row, col);
                    // Only mark ship as hit if it wasn't already hit
                    if(shipCell == Cell.SHIP) {
                        p2board.shipHit(row, col);
                    }
                    // Get ship name for sunk message if available
                    String shipName = "";
                    if (hit != null && hit.isSunk()) {
                        shipName = "\nSUNK! " + hit.name;
                    }
                    hitmiss = ("Hit " + shipName);
                }
                else{
                    // No ship at this location
                    hitmiss = ("Miss");
                    p1board.markMiss(row, col);
                    p2board.shipMiss(row, col);
                }
            }
        }
    }
    
    // Processes the AI's shot against the player's board.
    // Determines hit/miss, updates boards, and processes fleet damage.
    // Parameters: xpos - Row coordinate (0-based) where AI is shooting
    //             ypos - Column coordinate (0-based) where AI is shooting
    //             aiboard - AI's board (tracks where AI has shot)
    //             playerfleet - Player's fleet (tracks ship damage)
    //             playerboard - Player's board (shows ship locations)
    public static void aishot(int xpos, int ypos, Board aiboard, Fleet playerfleet, Board playerboard){
        // Process the hit on the fleet (increments ship damage counter)
        Fleet.Ship hit = playerfleet.processHit(xpos, ypos);
        
        // Determine hit or miss based on board state
        if(playerboard.cellAt(xpos, ypos, GridType.SHIPS) == Cell.WATER){
            // Miss - no ship at this location
            hitmiss = ("Miss");
            aiboard.markMiss(xpos, ypos);
            playerboard.shipMiss(xpos, ypos);
        }
        else{
            // Hit - ship present at this location
            aiboard.markHit(xpos, ypos);
            hitmiss = ("Hit ");
            // Note: Ship sunk message could be added here if needed
            playerboard.shipHit(xpos, ypos);
        }
    }
}
