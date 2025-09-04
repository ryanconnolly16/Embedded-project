
package battleship.fleetplacements;
import battleship.io.*;
import battleship.domain.Board;
import battleship.enums.Direction;
import battleship.enums.Result;
import battleship.interfaces.*;
import java.util.*;

public class Fleet {
    //same Ship inner class as your original
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
    
    //same global variables as your original
    public final List<Ship> ships;
    public static ArrayList<String> pieces = new ArrayList<>();
    static ArrayList<String> printinglist = new ArrayList<>();
    
    //components - match your simple naming
    private ShipLoader shipLoader;
    private ShipPlacer presetPlacer;
    private UserPlacer userPlacer;
    
    //same constructor logic as your original
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
    
    //same method name as your original - delegates to file loader
    public void Preset(Fleet fleet, Board board){
        presetPlacer.placeShips(fleet, board);
    }
    
    //same method name as your original - delegates to user placer
    public void UserPalcement(Fleet fleet, Board board){
        userPlacer.placeShipsInteractive(fleet, board);
    }
    
    //same methods as your original - core fleet logic stays here
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
    
    //same method signature as your original
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
    
    //keep static method like your original for backward compatibility
    public static List<Ship> loadShipsFromFile(String fileName) {
        return ShipFileLoader.loadShipsFromFile(fileName);
    }
}