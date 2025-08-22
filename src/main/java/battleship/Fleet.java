package battleship;

import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

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
        
        @Override
        public String toString() {
            return name + size ;
        }
    }
    
    public final List<Ship> ships;
    
    public Fleet() {
        ships = new ArrayList<>();   
        List<Ship> loaded = loadShipsFromFile("ships.txt");
        if (loaded != null) {
            ships.addAll(loaded);
            System.out.println("✓ Loaded " + loaded.size() + " ships from file");
        }else {
        System.out.println("✗ File loading failed - ships list is empty");
        }
        //ships.addAll(loadShipsFromFile("ships.txt")); 
    }
    
    public static List<Ship> loadShipsFromFile(String fileName) {  
        List<Ship> shipList = new ArrayList<>();
        
        try (InputStream inputStream = Fleet.class.getResourceAsStream("/" + fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int size = Integer.parseInt(parts[1].trim());
                    shipList.add(new Ship(name, size));
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("fail");
        }
        return shipList; 
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
    
    
    public void preset(Board board){
        placeShip(board, 0, 0, 0, Board.Direction.RIGHT);
        placeShip(board, 1, 2, 3, Board.Direction.DOWN);
        placeShip(board, 4, 8, 8, Board.Direction.LEFT);
        placeShip(board, 3, 9, 4, Board.Direction.LEFT);
        placeShip(board, 2, 5, 7, Board.Direction.UP);
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
}






