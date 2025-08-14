package battleship;
/*
public class BattleshipGame {
    public static void main(String[] args) throws Exception {
        
        Board board = new Board(10); // Creates board of size x

        board.clear(); // Makes board all water
        
        board.markHit(9, 8); // Marks a hit 
        board.markMiss(6, 6); // Marks a miss
        Board.Result r = board.markMiss(6, 11);
        if (r != Board.Result.OK) {
            System.out.println("markMiss failed: " + r + " at " + toCoord(6, 11)); // Example of how to check for input errors.
            // Will return OK, OUT_OF_BOUNDS or, INVALID_STATE
            //toCoord(num,letter)
        }
        
        
        
        board.placeShip(6, 0, 5, Board.Direction.UP); // Will place a ship on the board (y(0(top)-9(bottom)), x(0(left)-9(right)), length, direction)
        //

        System.out.println(BoardRenderer.render(board)); // Will print board with ships shown
        //System.out.println(BoardRenderer.render(board, true)); // Will print board with ships hidden

        
        
    }
    // Will print out coords in battleship sytle: 'A1' instead of 0,0
    private static String toCoord(int row, int col) { // Will need another way to do this
        return "" + (char)('A' + col) + (row + 1);
    }
}
*/

import java.util.*;

public class BattleshipGame {
    public static void main(String[] args) throws Exception {
        //random variables
        ArrayList<String> pieces = new ArrayList<>(Arrays.asList("Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"));
        String pieceremove;
        Scanner input = new Scanner(System.in);
        
        Board board = new Board(10);
        board.clear();
        
        Fleet fleet = new Fleet();
       
        // 0=Carrier-5, 1=Battleship-4, 2=Cruiser-3, 3=Submarine-3, 4=Destroyer-2
        
        //fleet.placeShip(board, ship, x, y, direction);
        System.out.println("Welcome to Battle Ship.");
        System.out.println("To shoot a shot type the grid coordinated when prompted, e.g. a3.");
        System.out.println("To quit the game type x.\n\n\n");
        
        System.out.println("Would you like to use the preset board?(y/n)");
        String preset = input.nextLine();
        if(preset.equals("y")){
            //fleet.placeShip(board, 0, 0, 0, Board.Direction.RIGHT);
            //fleet.placeShip(board, 1, 2, 3, Board.Direction.DOWN);
            //fleet.placeShip(board, 4, 8, 8, Board.Direction.LEFT);
            //fleet.placeShip(board, 3, 9, 4, Board.Direction.LEFT);
            //fleet.placeShip(board, 2, 5, 7, Board.Direction.UP);
            
            board.placeShip(0, 0, 3, Board.Direction.RIGHT);
            
        }
        else{
            while(fleet.allPlaced() == false){
                System.out.println("\nPlease input where you would like to place the ships in format:");
                System.out.println("Ship number X posistion Y posistion Direction");
                System.out.println("Ship Numbers:\n0=Carrier\n1=Battleship\n2=Cruiser\n3=Submarine\n4=Destroyer\n");
                
                System.out.println("You still have:");
                
                for(int i = 0; i < pieces.size(); i++){
                    System.out.println(pieces.get(i) + ",");
                    pieceremove = pieces.get(i);
                    pieces.remove(pieceremove);
                }
                    
                    
                    
                String posistions = input.nextLine();
                
                int type = posistions.charAt(0) - '0';
                int xpos = posistions.charAt(2) - '0';
                int ypos = posistions.charAt(4) - '0';
                String pos = posistions.toLowerCase();
                Board.Direction dir = null;
                
                
                if (pos.contains("up")) dir = Board.Direction.UP;
                else if (pos.contains("down")) dir = Board.Direction.DOWN;
                else if (pos.contains("left")) dir = Board.Direction.LEFT;
                else if (pos.contains("right")) dir = Board.Direction.RIGHT;
                
                fleet.placeShip(board, type, xpos, ypos, dir);
                
                System.out.println("There are " + fleet.getActiveCount());
                
                
                System.out.println("\n" + BoardRenderer.renderBoth(board));
            }
        }
        
        
        
        System.out.println("\n" + BoardRenderer.renderBoth(board));
        
        //board.markHit(row, col);
        //Fleet.Ship hitShip = fleet.processHit(row, col);
        while(fleet.allSunk() == false){
            System.out.println("Where would you look to shoot?:");
            String usershot = input.nextLine();
            
            if(usershot.equals("x")){
                break;
            }
            
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

                if (hit != null) {
                    board.markHit(row, col);
                    System.out.println("Hit " + 
                        (hit.isSunk() ? "SUNK!" + hit.name: ""));
                }
                else{
                    System.out.println("Miss.");
                    board.markMiss(row, col);
                }

                // Display board
                System.out.println("\n" + BoardRenderer.renderBoth(board));
            }
        }
        
        System.out.println("Game Over");
    }
}