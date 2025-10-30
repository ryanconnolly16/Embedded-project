
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

// Fleet class manages a collection of ships and their placement/state on the board.
// Handles ship placement, hit processing, and synchronization with board state.
public class Fleet {
    // Inner class representing a single ship in the fleet.
    // Tracks ship name, size, hits taken, and placement location/direction.
    public static class Ship {
        // The name of the ship (e.g., "Destroyer", "Battleship")
        public final String name;
        
        // The size/length of the ship in cells
        public final int size;
        
        // Number of hits this ship has taken
        private int hits;
        
        // Row and column coordinates where the ship's first cell is placed (-1 if not placed)
        private int row, col;
        
        // Direction the ship is oriented (RIGHT, DOWN, etc.)
        private Direction dir;
        
        // Constructor for a Ship.
        // Parameters: name - Name of the ship
        //             size - Length/size of the ship in cells
        public Ship(String name, int size) {
            this.name = name;
            this.size = size;
            this.hits = 0;
            this.row = -1; // -1 indicates ship is not placed yet
        }
        
        // Checks if this ship has been placed on the board.
        public boolean isPlaced() { return row >= 0; }
        
        // Checks if this ship has been sunk (hits >= size).
        public boolean isSunk() { return hits >= size; }
        
        // Gets the current health of the ship (remaining unhit cells).
        public int getHealth() { return size - hits; }
        
        // Places the ship at the specified coordinates with the given direction.
        // Parameters: r - Row coordinate for the ship's starting position
        //             c - Column coordinate for the ship's starting position
        //             d - Direction the ship is oriented
        void place(int r, int c, Direction d) {
            row = r; col = c; dir = d;
        }
        
        // Checks if the ship occupies the specified cell coordinates.
        // Parameters: r - Row coordinate to check
        //             c - Column coordinate to check
        // Returns: true if the ship occupies this cell
        boolean contains(int r, int c) {
            if (!isPlaced()) return false;
            // Check each cell of the ship to see if it matches the given coordinates
            for (int i = 0; i < size; i++) {
                if (row + dir.deltar * i == r && col + dir.deltac * i == c) 
                    return true;
            }
            return false;
        }
        
        // Records a hit on this ship (increments hit counter if not already at max).
        void hit() { if (hits < size) hits++; }
        
        // Adds a hit to this ship if it's not already sunk.
        public void addHit() {
            if (!isSunk()) {
                hits++;
            }
        }
        
        // Returns a string representation of the ship (name + size).
        @Override
        public String toString() {
            return name + size ;
        }
    }
    
    // List of all ships in this fleet
    public final List<Ship> ships;
    
    // Static list used for ship piece tracking (may be unused)
    public static ArrayList<String> pieces = new ArrayList<>();
    
    // Static list used for printing ship information (may be unused)
    static ArrayList<String> printinglist = new ArrayList<>();
    
    // Loader for reading ship definitions from files
    private ShipLoader shipLoader;
    
    // Placer for automatically placing ships using preset configurations
    private ShipPlacer presetPlacer;
    
    // Placer for interactive user ship placement
    private UserPlacer userPlacer;
    
    // Constructor for Fleet. Initializes the fleet by loading ships from "ships.txt" file.
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
    
    // Places all ships in the fleet using preset/random placement.
    // Parameters: fleet - The fleet to place ships for
    //             board - The board to place ships on
    public void preset(Fleet fleet, Board board){
        presetPlacer.placeShips(fleet, board);
    }
    
    // Places ships interactively, allowing the user to choose positions.
    // Parameters: fleet - The fleet to place ships for
    //             board - The board to place ships on
    public void userPalcement(Fleet fleet, Board board){
        userPlacer.placeShipsInteractive(fleet, board);
    }
    
    // Checks if all placed ships in the fleet have been sunk.
    // Returns: true if all placed ships are sunk, false otherwise
    public boolean allSunk() {
        for (Ship s : ships) 
            if (s.isPlaced() && !s.isSunk()) return false;
        return ships.stream().anyMatch(Ship::isPlaced);
    }
    
    // Processes a hit at the specified coordinates.
    // Finds the ship at that location and increments its hit counter.
    // Parameters: row - Row coordinate where the hit occurred
    //             col - Column coordinate where the hit occurred
    // Returns: The ship that was hit, or null if no ship was at that location
    public Ship processHit(int row, int col) {
        for (Ship s : ships) {
            if (s.contains(row, col)) {
                s.hit();
                return s;
            }
        }
        return null;
    }
    
    // Attempts to place a ship at the specified location.
    // Performs error checking to ensure the ship can fit where specified.
    // Parameters: board - The board to place the ship on
    //             shipIndex - Index of the ship in the fleet to place
    //             col - Column coordinate for ship placement
    //             row - Row coordinate for ship placement
    //             dir - Direction the ship should be oriented
    // Returns: Result indicating success or the reason for failure
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
        if("player".equals(player)){visited = SetupController.playervisited; board = BattleshipGUI.playerBoard;}
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
    // Counts a hit whenever SHIPS has a ship (or hit ship) and SHOTS marks a hit.
    public void syncHitsFromBoard(Board board) {
        final int n = board.size();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                Cell shipCell = board.cellAt(r, c, GridType.SHIPS);
                Cell shotCell = board.cellAt(r, c, GridType.SHOTS);
                // Check if there's a ship (or hit ship) and it's been shot at
                if ((shipCell == Cell.SHIP || shipCell == Cell.HIT)
                 && shotCell == Cell.HIT) {
                    // This will increment the matching ship's hit counter
                    processHit(r, c);
                }
            }
        }
    }


    
    
    
}