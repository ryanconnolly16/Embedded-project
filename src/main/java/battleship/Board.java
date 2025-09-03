package battleship;

public class Board {
    public static final char WATER = ' ';
    public static final char HIT   = 'X';
    public static final char MISS  = 'O';
    public static final char SHIP  = 'S';
    
    private final char[][] shipGrid;
    private final char[][] hitMissGrid;
    private final int size;

    // Status codes for user-driven operations (no crashes)
    public enum Result { 
        OK, OUT_OF_BOUNDS, INVALID_LENGTH, INVALID_STATE, OCCUPIED 
    }

    // Cardinal directions as step vectors
    public enum Direction {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);
        public final int deltar, deltac;
        Direction(int dr, int dc) { 
            this.deltar = dr; this.deltac = dc; 
        }
    }
    
    //type of grid enum
    public enum GridType { SHIPS, SHOTS }

    //constructor
    public Board(int size) {
        if (size <= 0 && size < 20) throw new IllegalArgumentException("size must be between 0 and 20");
        this.size = size;
        this.shipGrid = new char[size][size];
        this.hitMissGrid = new char[size][size];
        fillWater();
    }

    //returns board size
    public int getSize() { return size; }
    
    //retrieves cell at a given coordinate (to be used elsewhere)
    public char cellAt(int row, int col, GridType which) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException("row=" + row + " col=" + col);
        }
        return (which == GridType.SHIPS) ? shipGrid[row][col] : hitMissGrid[row][col];
    }

    //sets a specified cell to a certain state
    public Result setCell(int row, int col, char state, GridType which) {
        if (!inBounds(row, col)) return Result.OUT_OF_BOUNDS;
        if (!isAllowed(state))   return Result.INVALID_STATE;
        if (which == GridType.SHIPS) shipGrid[row][col] = state; 
        else hitMissGrid[row][col] = state;
        return Result.OK;
    }

    //clears board (constructor)
    public final void fillWater() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                shipGrid[r][c] = WATER;
                hitMissGrid[r][c] = WATER;
            }
        }
    }

    //places a line of a certain state, is used to place ship but has error handling behind the scenes
    private Result fillLine(int r0, int c0, int length, Direction dir, char state) {
        if (!isAllowed(state)) return Result.INVALID_STATE;
        if (length <= 0)       return Result.INVALID_LENGTH;

        // Compute ship end cell and bounds check for the whole line
        int r1 = r0 + dir.deltar * (length - 1);
        int c1 = c0 + dir.deltac * (length - 1);
        if (!inBounds(r0, c0) || !inBounds(r1, c1)) return Result.OUT_OF_BOUNDS;

        // Check for ship overrlap 
        for (int i = 0; i < length; i++) {
            int rr = r0 + dir.deltar * i;
            int cc = c0 + dir.deltac * i;
            if (shipGrid[rr][cc] != WATER) return Result.OCCUPIED;
        }

        // Change state of ship cells
        for (int i = 0; i < length; i++) {
            int rr = r0 + dir.deltar * i;
            int cc = c0 + dir.deltac * i;
            shipGrid[rr][cc] = state;
        }
        return Result.OK;
    }

    //helpers
    private boolean inBounds(int row, int col) { return row>=0 && row<size && col>=0 && col<size; }
    
    private boolean isAllowed(char s) { return s==WATER || s==SHIP || s==HIT || s==MISS; }
    
    //Methods to be used elsewhere
    public Result markHit(int row, int col)  { return setCell(row, col, HIT,  GridType.SHOTS); }
    
    public Result markMiss(int row, int col) { return setCell(row, col, MISS, GridType.SHOTS); }
    
    public Result shipHit(int row, int col)  { return setCell(row, col, HIT,  GridType.SHIPS); }
    
    public Result shipMiss(int row, int col) { return setCell(row, col, MISS, GridType.SHIPS); }
    
    public Result placeShip(int r0,int c0,int length,Direction dir){ return fillLine(r0,c0,length,dir,SHIP); }
}
