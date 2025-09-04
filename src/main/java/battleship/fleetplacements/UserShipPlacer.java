
package battleship.fleetplacements;

import battleship.domain.*;
import battleship.enums.*;
import battleship.interfaces.*;
import battleship.ui.*;
import java.io.IOException;
import java.util.Scanner;

// function to let users place the ships
class UserShipPlacer implements UserPlacer {
    private final Scanner input;
    
    public UserShipPlacer() {
        input = new Scanner(System.in);
    }
    @Override
    public void placeShipsInteractive(Fleet fleet, Board board) {
        while(Fleet.printinglist.size() < Fleet.pieces.size()){
            try{
                System.out.println("\nPlease input where you would like to place the ships in the format:");
                System.out.println("Ship number, X position, Y position, Direction");
                System.out.println("\n" + BoardRenderer.renderBoth(board, new DefaultGlyphs()));
                System.out.println("Ship Numbers;");
                
                //outputing not placed ships
                for(int i = 0; i < Fleet.pieces.size(); i++){
                    if (Fleet.printinglist.contains(Fleet.pieces.get(i))) continue;
                    System.out.println((i+1) + ". " + Fleet.pieces.get(i));
                }
                
                //seperating userinput to save to a list for each part
                System.out.println("\n");
                String posistions = UiOutput.getInput(input);
                String[] list = posistions.split(",");
                    
                int type = Integer.parseInt(list[0].trim()) -1 ;
                
                list[1].trim();
                list[2].trim();
                
                //checking if the user input format is correct i.e. didnt put a letter for the row
                if (( String.valueOf(type).length() > 1 ) ||
                        ( String.valueOf(list[1]).length() > 1 || !list[1].matches("[a-zA-z]+") ) ||
                        ( !list[2].matches("[0-9]+") )
                        ){
                    System.out.println("Invalid input, try again.");
                    placeShipsInteractive(fleet, board);
                }
                
                //saving each part of userinput to variables 
                int ypos;
                int xpos = list[1].charAt(0) - 'a';
                if(list[2].equals("10")){
                    ypos = 9;
                }else{
                    ypos = list[2].charAt(0) - '0' -1;
                }
                String pos = list[3].trim();
                
                Direction dir = null;
                
                if (pos.contains("up")) dir = Direction.UP;
                else if (pos.contains("down")) dir = Direction.DOWN;
                else if (pos.contains("left")) dir = Direction.LEFT;
                else if (pos.contains("right")) dir = Direction.RIGHT;
                
                //error checking for ship already placed, placement out of bounds, placing ship in a square allready taken
                if (Fleet.printinglist.contains(Fleet.pieces.get(type))){
                    System.out.println("Ship already placed, place another.\n\n");
                }
                else if(fleet.placeShip(board, type, xpos, ypos, dir) == Result.OK){
                    Fleet.printinglist.add(Fleet.pieces.get(type));
                }
                else if(fleet.placeShip(board, type, xpos, ypos, dir) == Result.OCCUPIED){
                    System.out.println("Space is occupied, try again.");
                }
                else if(fleet.placeShip(board, type, xpos, ypos, dir) == Result.OUT_OF_BOUNDS){
                    System.out.println("Ship is out of bounds, check your origin and direction.");
                }
            }catch(IOException | NumberFormatException e){
                System.out.println("Invalid input, try again");
            }
        }
    }
}