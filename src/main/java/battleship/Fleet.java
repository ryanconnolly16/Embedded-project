package battleship;

import battleship.domain.Board;
import battleship.enums.Direction;
import battleship.enums.Result;
import battleship.ui.BoardRenderer;
import battleship.ui.DefaultGlyphs;

import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Fleet {
    //contructor
    public static class Ship {
        public final String name;
        public final int size;
        private int hits;
        private int row, col;
        private Direction dir;
        
        public Ship(String name, int size) {
            this.name = name;
            this.size = size;
            this.hits = 0;
            this.row = -1;
        }
        
        public boolean isPlaced() { return row >= 0; }
        public boolean isSunk() { return hits >= size; }
        public int getHealth() { return size - hits; }
        
        void place(int r, int c, Direction d) {
            row = r; col = c; dir = d;
        }
        
        boolean contains(int r, int c) {
            if (!isPlaced()) return false;
            for (int i = 0; i < size; i++) {
                if (row + dir.deltar * i == r && col + dir.deltac * i == c) 
                    return true;
            }
            return false;
        }
        
        void hit() { if (hits < size) hits++; }
        public void addHit() {
            if (!isSunk()) {
                hits++;
            }
        }
        
        
        @Override
        public String toString() {
            return name + size ;
        }
    }
    
    //global variables
    public final List<Ship> ships;
    static ArrayList<String> pieces = new ArrayList<>();
    static ArrayList<String> printinglist = new ArrayList<>();
    Scanner input = new Scanner(System.in);
    
    //adding ships from txt file to ships
    public Fleet() {
        ships = new ArrayList<>();   
        List<Ship> loaded = loadShipsFromFile("ships.txt");
        if (loaded != null) {
            ships.addAll(loaded);
            
        }else {
        System.out.println("File loading failed - ships list is empty");
        }
    }
    
    //getting ships and their size from txt file
    public static List<Ship> loadShipsFromFile(String fileName) {  
        List<Ship> shipList = new ArrayList<>();
        pieces.clear();
            try (InputStream inputStream = Fleet.class.getResourceAsStream("/" + fileName);
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String line;
                //splits the ship from their size and saves to ship
                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        int size = Integer.parseInt(parts[1].trim());
                        shipList.add(new Ship(name, size));
                        //list of ship names for later use
                        pieces.add(name);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("fail");
            }
        
        return shipList; 
    }
    
    public boolean allSunk() {
        for (Ship s : ships) 
            if (s.isPlaced() && !s.isSunk()) return false;
        return ships.stream().anyMatch(Ship::isPlaced);
    }
    
    
    
    // Returns ship if hit, null if miss
    public Ship processHit(int row, int col) {
        for (Ship s : ships) {
            if (s.contains(row, col)) {
                s.hit();
                return s;
            }
        }
        return null;
    }
        
    
    //uses random function to place ships on board
    public void Preset(Fleet fleet, Board board){
        Random rand = new Random();
        for(int i = 0; i < pieces.size(); i++){
            //will loop until the ship fit into board, including not overlapping and making sure it doesnt extend over edge of board
            boolean placementgood = false;
            while (placementgood == false){
                int rshipnum = i;
                int rxpos = rand.nextInt(10);
                int rypos = rand.nextInt(10);
                int rdirc = rand.nextInt(4);
                
                Direction dir = null;
                if (rdirc == 0) dir = Direction.UP;
                else if (rdirc == 1) dir = Direction.DOWN;
                else if (rdirc == 2) dir = Direction.LEFT;
                else if (rdirc == 3) dir = Direction.RIGHT;

                Result result = fleet.PlaceShip(board, rshipnum, rxpos, rypos, dir);
                //end loop
                if(result == Result.OK){
                    placementgood = true;
                }   
            }
        }
    }
    
    //for when user wants to place their own pieces
    public void UserPalcement(Fleet fleet, Board board){
        while(printinglist.size() < pieces.size()){
            try{
                System.out.println("\nPlease input where you would like to place the ships in format:");
                System.out.println("Ship number, X posistion, Y posistion, Direction");
                System.out.println("\n" + BoardRenderer.renderBoth(board, new DefaultGlyphs()));
                System.out.println("Ship Numbers;");
                
                //outputing not placed ships
                for(int i = 0; i < pieces.size(); i++){
                    if (printinglist.contains(pieces.get(i))) continue;
                    System.out.println((i+1) + ". " + pieces.get(i));
                }
                
                //seperating userinput to save to a list for each part
                System.out.println("\n");
                String posistions = UI_Output.GetInput(input);
                String[] list = posistions.split(",");
                    
                int type = Integer.parseInt(list[0].trim()) -1 ;
                
                list[1].trim();
                list[2].trim();
                
                //checking if the user input format is correct i.e. didnt put a letter for the row
                if (( String.valueOf(type).length() > 1 ) ||
                        ( String.valueOf(list[1]).length() > 1 || !list[1].matches("[a-zA-z]+") ) ||
                        ( !list[2].matches("[0-9]+") )
                        ){
                    System.out.println("Invalid input try again.");
                    UserPalcement( fleet, board);
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
                if (printinglist.contains(pieces.get(type))){
                    System.out.println("Ship already placed, place another.\n\n");
                }
                else if(fleet.PlaceShip(board, type, xpos, ypos, dir) == Result.OK){
                    printinglist.add(pieces.get(type));
                }
                else if(fleet.PlaceShip(board, type, xpos, ypos, dir) == Result.OCCUPIED){
                    System.out.println("Space is occupied, try again.");
                }
                else if(fleet.PlaceShip(board, type, xpos, ypos, dir) == Result.OUT_OF_BOUNDS){
                    System.out.println("Ship is out of bounds, check you origin and direction.");
                }
            }catch(Exception e){
                System.out.println("Invalid input try again");
            }
        }
    }
    
    //placing the ship no the board
    public Result PlaceShip(Board board, int shipIndex, int col, int row, Direction dir) {
        if (shipIndex < 0 || shipIndex >= ships.size()) 
            return Result.INVALID_STATE;
        
        Ship ship = ships.get(shipIndex);
        if (ship.isPlaced()) 
            return Result.OCCUPIED;
        
        Result result = board.placeShip(row, col, ship.size, dir);
        if (result == Result.OK) {
            ship.place(row, col, dir);
        }
        return result;
    }
}