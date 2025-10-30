
package battleship.fleetplacements;
import battleship.BattleshipGUI;
import battleship.io.*;
import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.Direction;
import battleship.enums.GridType;
import battleship.enums.Result;
import battleship.gui_setup.SetupController;
import static battleship.gui_setup.SetupController.playervisited;
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
        shipLoader = new ShipFileLoader();
        
        presetPlacer = new PresetPlacer();
        //userPlacer = new UserShipPlacer();
        
        List<Ship> loaded = shipLoader.loadShips("ships.txt");
        if (loaded != null) {
            ships.addAll(loaded);
        }else {
            //System.out.println("File loading failed - ships list is empty");
        }
    }
    
    //sends to preset file to use function to randomly place ships
    public void preset(Fleet fleet, Board board){
        presetPlacer.placeShips(fleet, board);
    }
    
    //sends to usershipplace file to let user place ships
    public void userPalcement(Fleet fleet, Board board){
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
    public Result placeShip(Board board, int shipIndex, int col, int row, Direction dir) {
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
    
    
    
    
    
    // ---- Add to Fleet.java ----

    // Reset all placements and hits (keeps names/sizes)
    public void clearPlacementsAndHits() {
        for (Ship s : ships) {
            // we're inside Fleet, so we can touch Ship's private fields
            s.hits = 0;
            s.row  = -1;
            s.col  = 0;
            s.dir  = Direction.RIGHT; // arbitrary default
        }
    }

    // Find an unplaced Ship with a given size
    private Ship findUnplacedBySize(int size) {
        for (Ship s : ships) {
            if (!s.isPlaced() && s.size == size) return s;
        }
        return null;
    }

    // Rebuild fleet placements by scanning the board’s SHIPS grid.
    // Assumes Board.cellAt(r,c,GridType.SHIPS) returns Cell.SHIP for ship cells.
    // Uses RIGHT for horizontal and DOWN for vertical orientation.
    
    
    
    public boolean repopulateFromBoard(String player) {
        clearPlacementsAndHits();
        boolean[][] visited;

        Board board;
        if(player == "player"){visited = SetupController.playervisited; board = BattleshipGUI.playerBoard;}
        else{visited = SetupController.aivisited;board = BattleshipGUI.aiBoard;}
        final int n = 10;
        

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
               
                if(visited[r][c])continue;
                
                if(board.cellAt(r, c, GridType.SHIPS) == Cell.MISS){
                    visited[r][c] = true;
                }
                if (board.cellAt(r, c, GridType.SHIPS) == Cell.MISS || 
                        board.cellAt(r, c, GridType.SHIPS) == Cell.WATER) continue;
                
                // Check if this is the LEFTMOST cell of a horizontal segment
                boolean leftIsShip = (c > 0) && board.cellAt(r, c - 1, GridType.SHIPS) == Cell.SHIP;
                // Check if this is the TOPMOST cell of a vertical segment
                boolean upIsShip   = (r > 0) && board.cellAt(r - 1, c, GridType.SHIPS) == Cell.SHIP;

                // Only start a segment at a canonical start (leftmost or topmost)
                if (leftIsShip || upIsShip) continue;

                // Measure horizontal length
                int lenRight = 0;
                while (c + lenRight < n && board.cellAt(r, c + lenRight, GridType.SHIPS) == Cell.SHIP || 
                        c + lenRight < n && board.cellAt(r, c + lenRight, GridType.SHIPS)== Cell.HIT) {
                    lenRight++;
                    
                }
                
                // Measure vertical length
                int lenDown = 0;
                while (r + lenDown < n && board.cellAt(r + lenDown, c, GridType.SHIPS) == Cell.SHIP ||
                        r + lenDown < n && board.cellAt(r + lenDown, c, GridType.SHIPS) == Cell.HIT){
                    
                    lenDown++;
                    
                }

                final boolean isHorizontal = lenRight >= lenDown;
                final int length = isHorizontal ? lenRight : lenDown;

                // Mark visited along this segment
                if (isHorizontal) {
                    for (int k = 0; k < length; k++) visited[r][c + k] = true;
                } else {
                    for (int k = 0; k < length; k++) visited[r + k][c] = true;
                }

                // Match to an unplaced Ship of the same size
                Ship s = findUnplacedBySize(length);
                if (s == null) {
                    // No matching ship left; inconsistent board vs fleet definition
                    return false;
                }
                
                

                // Place without touching the board again
                s.place(r, c, isHorizontal ? Direction.RIGHT : Direction.DOWN);
                
                
            }
        }
        
        return true;
        
    }

    // After placements are set, sync hits from the SHOTS grid.
    // Counts a hit whenever both SHIPS has a ship and SHOTS marks a hit.
    public void syncHitsFromBoard(Board board) {
        final int n = board.size();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (board.cellAt(r, c, GridType.SHIPS) == Cell.SHIP
                 && board.cellAt(r, c, GridType.SHOTS) == Cell.HIT) {
                    // This will increment the matching ship’s hit counter
                    processHit(r, c);
                }
            }
        }
    }


    
    
    
}