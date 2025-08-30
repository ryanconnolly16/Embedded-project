/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleship;

/**
 *
 * @author ryanconnolly
 */
public class Battle {
    
                                //shooting player, receieving fleet, receiving board
    public static void usershot(String usershot, Board p1board, Fleet fleet, Board p2board){
        if(!fleet.allSunk()){
            int col = usershot.charAt(0) - 'a';
            int row = usershot.charAt(1) - '1';
            //incase of 10 in row
            if(usershot.length() > 2){
               row = 9;
            }

            if(col > 9 || row > 10){
                System.out.println("Input is out of bounds, try again.\n\n");
            }

            else{
                Fleet.Ship hit = fleet.processHit(row, col);

                if(hit == null){
                    System.out.println("Miss.");
                    p1board.markMiss(row, col);
                    p2board.shipMiss(row, col);
                }
                else{
                    p1board.markHit(row, col);
                    System.out.println("Hit " + 
                        (hit.isSunk() ? "SUNK!" + hit.name: ""));
                    p2board.shipHit(row, col);
                    if(fleet.allSunk()){
                        System.exit(0);
                    }
                }

                // Display board
                //System.out.println("\n" + BoardRenderer.renderBoth(board));
            }
        }
    }
}
