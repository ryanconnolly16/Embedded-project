
package battleship;

import battleship.Board;
import battleship.Fleet;
import battleship.Fleet.Ship;
import java.util.*;

public class UI_Output {
    
    ArrayList<String> pieces = new ArrayList<>(Arrays.asList("Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"));
    String pieceremove;
    


    public static String Startup(){
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Battle Ship.");
        System.out.println("To shoot a shot type the grid coordinated when prompted, e.g. a3.");
        System.out.println("To quit the game type x.\n\n");
        
        System.out.println("Would you like to use the preset board?(y/n)");
        String preset = input.nextLine();  
        return preset;
    }

    
    


}
    

  /* 
}

        
        // 0=Carrier-5, 1=Battleship-4, 2=Cruiser-3, 3=Submarine-3, 4=Destroyer-2
        
        //fleet.placeShip(board, ship, x, y, direction);
List<Ship> ships = Fleet.loadShipsFromFile("Fleet_ships.txt");

        String preset = input.nextLine();
        if(preset.equals("y")){
            fleet.placeShip(board, 0, 0, 0, Board.Direction.RIGHT);
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
*/
