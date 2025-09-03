package battleship;

public class Battle {
    //shooting player, receieving fleet, receiving board
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
                Fleet.Ship hit = p2fleet.processHit(row, col);
                
                //checks if cell hits or misses a ship
                if(hit == null){
                    System.out.println("Miss.");
                    p1board.markMiss(row, col);
                    p2board.shipMiss(row, col);
                }
                else{
                    p1board.markHit(row, col);
                    //will display which ship is sunk if the ship runs out of health
                    System.out.println("Hit " + 
                        (hit.isSunk() ? "SUNK!" + hit.name: ""));
                    p2board.shipHit(row, col);
                    if(p2fleet.allSunk()){
                        System.exit(0);
                    }
                }
            }
        }
    }
    
    
    public static void aishot(int xpos,int ypos, Board aiboard, Fleet pfleet, Board pboard){
        Fleet.Ship hit = pfleet.processHit(xpos, ypos);
        //checks if cell hits or misses a ship
        if(hit == null){
            System.out.println("Miss.");
            aiboard.markMiss(xpos, ypos);
            pboard.shipMiss(xpos, ypos);
        }
        else{
            aiboard.markHit(xpos, ypos);
            //will display which ship is sunk if the ship runs out of health
            System.out.println("Hit " + 
                (hit.isSunk() ? "SUNK!" + hit.name: ""));
            pboard.shipHit(xpos, ypos);
            if(pfleet.allSunk()){
                System.exit(0);
            }
        }
    }
}
