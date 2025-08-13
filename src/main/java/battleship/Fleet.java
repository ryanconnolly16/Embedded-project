package battleship;

import java.util.*;

public class Fleet {
    public static class Ship {
        public final String name;
        public final int size;
        private int hits;
        private int row, col;
        private Board.Direction dir;
        
        public Ship(String name, int size) {
            this.name = name;
            this.size = size;
            this.hits = 0;
            this.row = -1;
        }
        
        public boolean isPlaced() { return row >= 0; }
        public boolean isSunk() { return hits >= size; }
        public int getHealth() { return size - hits; }
        
        void place(int r, int c, Board.Direction d) {
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
        void reset() { hits = 0; row = -1; col = -1; dir = null; }
    }
    
    private final List<Ship> ships;
    
    public Fleet() {
        ships = Arrays.asList(
            new Ship("Carrier", 5),
            new Ship("Battleship", 4),
            new Ship("Cruiser", 3),
            new Ship("Submarine", 3),
            new Ship("Destroyer", 2)
        );
    }
    
    // Custom fleet constructor
    public Fleet(Ship... customShips) {
        ships = Arrays.asList(customShips);
    }
    
    public List<Ship> getShips() { return new ArrayList<>(ships); }
    
    public int getActiveCount() {
        int count = 0;
        for (Ship s : ships) 
            if (s.isPlaced() && !s.isSunk()) count++;
        return count;
    }
    
    public int getSunkCount() {
        int count = 0;
        for (Ship s : ships) 
            if (s.isPlaced() && s.isSunk()) count++;
        return count;
    }
    
   
    public boolean allPlaced() {
        for (Ship s : ships) 
            if (!s.isPlaced()) return false;
        return true;
    }
    
    public boolean allSunk() {
        for (Ship s : ships) 
            if (s.isPlaced() && !s.isSunk()) return false;
        return ships.stream().anyMatch(Ship::isPlaced);
    }
    
    
    //checks if the ship fits, isnt already placed, isnt overlapping
    // Place ship by index (0-4 for standard fleet)
    public Board.Result placeShip(Board board, int shipIndex, int row, int col, Board.Direction dir) {
        if (shipIndex < 0 || shipIndex >= ships.size()) 
            return Board.Result.INVALID_STATE;
        
        Ship ship = ships.get(shipIndex);
        if (ship.isPlaced()) 
            return Board.Result.OCCUPIED;
        
        Board.Result result = board.placeShip(row, col, ship.size, dir);
        if (result == Board.Result.OK) {
            ship.place(row, col, dir);
        }
        return result;
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
    
    public void reset() {
        for (Ship s : ships) s.reset();
    }
    
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Fleet: %d active, %d sunk\n", getActiveCount(), getSunkCount()));
        for (int i = 0; i < ships.size(); i++) {
            Ship s = ships.get(i);
            char status = !s.isPlaced() ? '-' : s.isSunk() ? 'X' : 'O';
            sb.append(String.format("[%d] %s(%d): %c", i, s.name, s.size, status));
            if (s.isPlaced() && !s.isSunk()) 
                sb.append(String.format(" HP:%d/%d", s.getHealth(), s.size));
            sb.append('\n');
        }
        return sb.toString();
    }
}






