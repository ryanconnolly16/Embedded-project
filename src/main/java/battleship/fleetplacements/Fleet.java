
package battleship.fleetplacements;
import battleship.io.*;
import battleship.domain.Board;
import battleship.enums.Direction;
import battleship.enums.Result;
import battleship.interfaces.*;
import java.util.*;

public class Fleet {
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
    public static ArrayList<String> pieces = new ArrayList<>();
    static ArrayList<String> printinglist = new ArrayList<>();
    
    private ShipLoader shipLoader;
    private ShipPlacer presetPlacer;
    private UserPlacer userPlacer;
    
    //constructor
    public Fleet() {
        ships = new ArrayList<>();   
        
        //create components like your style
        shipLoader = new ShipFileLoader();
        
        presetPlacer = new PresetPlacer();
        userPlacer = new UserShipPlacer();
        
        List<Ship> loaded = shipLoader.loadShips("ships.txt");
        if (loaded != null) {
            ships.addAll(loaded);
        }else {
            System.out.println("File loading failed - ships list is empty");
        }
    }
    
    //sends to preset file to use function to randomly place ships
    public void Preset(Fleet fleet, Board board){
        presetPlacer.placeShips(fleet, board);
    }
    
    //sends to usershipplace file to let user place ships
    public void UserPalcement(Fleet fleet, Board board){
        userPlacer.placeShipsInteractive(fleet, board);
    }
    
    //checks if the whole fleet has been sunk
    public boolean allSunk() {
        for (Ship s : ships) 
            if (s.isPlaced() && !s.isSunk()) return false;
        return ships.stream().anyMatch(Ship::isPlaced);
    }
    
    // will minus a "health point" from the ship 
    public Ship processHit(int row, int col) {
        for (Ship s : ships) {
            if (s.contains(row, col)) {
                s.hit();
                return s;
            }
        }
        return null;
    }
    
    //error checking to see if ship can fit where it is specified
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
        else {
            return Result.INVALID_STATE;
        }
        return result;
    }
    
    //sends to ShipFileLoader to use function to load fleet from a .txt file
    public static List<Ship> loadShipsFromFile(String fileName) {
        return ShipFileLoader.loadShipsFromFile(fileName);
    }
}