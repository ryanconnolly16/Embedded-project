package battleship.playinggame;

import battleship.fleetplacements.*;
import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;

public class Battle {
    //function to check what the user inputted for the shot and then shoot
    //shooting player, receieving fleet, receiving board
    
    public static String hitmiss;
    
    
    
    public static void usershot(String usershot, Board p1board, Fleet p2fleet, Board p2board){
        if(!p2fleet.allSunk()){
            int col;
            int row;
            //if usershot num num
            if (!Character.isLetter(usershot.charAt(0))) {
                col = usershot.charAt(0) - '1'+1;
                row = usershot.charAt(1) - '1'+1;
            }
            //if usershot letter num
            else{
                col = usershot.charAt(0) - 'a';
                row = usershot.charAt(1) - '1';
            }
            
            //incase of 10 in row
            if(usershot.length() > 2){
               row = 9;
            }
            
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
    
    //function for the ai to shot 
    public static void aishot(int xpos,int ypos, Board aiboard, Fleet playerfleet, Board playerboard){
        Fleet.Ship hit = playerfleet.processHit(xpos, ypos);
        //checks if cell hits or misses a ship
        if(playerboard.cellAt(xpos, ypos, GridType.SHIPS) == Cell.WATER
                ){
            hitmiss = ("Miss");
            aiboard.markMiss(xpos, ypos);
            playerboard.shipMiss(xpos, ypos);
        }
        else{
            aiboard.markHit(xpos, ypos);
            
            
            //will display which ship is sunk if the ship runs out of health
            hitmiss = ("Hit ");// + 
//                (hit.isSunk() ? "\nSUNK! " + hit.name: ""));
            
            playerboard.shipHit(xpos, ypos);
            
        }
    }
}
