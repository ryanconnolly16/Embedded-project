package battleship.playinggame;

import battleship.fleetplacements.*;
import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;

// Battle class handles the core game logic for shooting 
public class Battle {
    // Result string saying if hit or miss
    public static String hitmiss;
    
    // Processes a player's shot against the opponent's board.
    public static void usershot(String usershot, Board p1board, Fleet p2fleet, Board p2board){
        // Only process shot if opponent's fleet hasn't been completely sunk
        if(!p2fleet.allSunk()){
            int col;
            int row;
            
            // Parse shot coordinates from string format
            if (!Character.isLetter(usershot.charAt(0))) {
                col = usershot.charAt(0) - '1' + 1;
                row = usershot.charAt(1) - '1' + 1;
            }
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
                // Check the board's SHIPS grid to determine if there's a ship
                Cell shipCell = p2board.cellAt(row, col, GridType.SHIPS);
                Fleet.Ship hit = p2fleet.processHit(row, col);
                
                // Use board state to determine hit/miss, processHit just tracks fleet health
                if(shipCell == Cell.SHIP || shipCell == Cell.HIT){
                    // There's a ship at this location (or was, if already hit)
                    p1board.markHit(row, col);
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
    public static void aishot(int xpos, int ypos, Board aiboard, Fleet playerfleet, Board playerboard){
        // Process the hit on the fleet 
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
            String shipName = "";
            if (hit != null && hit.isSunk()) {
                        shipName = "\nSUNK! " + hit.name;
                    }
            aiboard.markHit(xpos, ypos);
            hitmiss = ("Hit "+ shipName);
            playerboard.shipHit(xpos, ypos);
        }
    }
}
