package battleship;
//testing
public class Board {
    // Cell symbols (stick with chars for now)
    public static final char WATER = ' ';
    public static final char HIT   = 'X';
    public static final char MISS  = 'O';
    public static final char SHIP  = 'S';

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

    private final char[][] grid;
    private final int size;

    public Board(int size) {
        if (size <= 0 && size < 20) throw new IllegalArgumentException("size must be between 0 and 20");
        this.size = size;
        this.grid = new char[size][size];
        clear();
    }

    public int getSize() { return size; }

    public char getCell(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException("row=" + row + ", col=" + col + " out of bounds for size " + size);
        }
        return grid[row][col];
    }

    public Result setCell(int row, int col, char state) {
        if (!inBounds(row, col)) return Result.OUT_OF_BOUNDS;
        if (!isAllowed(state))   return Result.INVALID_STATE;
        grid[row][col] = state;
        return Result.OK;
    }

    // Fill entire board with water
    public void clear() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c] = WATER;
            }
        }
    }

    //Fill a straight line starting at (r0,c0) with length cells, going in 'dir', using 'state'.
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
            if (grid[rr][cc] != WATER) return Result.OCCUPIED;
        }

        // Change state of ship cells
        for (int i = 0; i < length; i++) {
            int rr = r0 + dir.deltar * i;
            int cc = c0 + dir.deltac * i;
            grid[rr][cc] = state;
        }
        return Result.OK;
    }

    
    
    
    
    
    
    
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private boolean isAllowed(char state) {
        return state == WATER || state == SHIP || state == HIT || state == MISS;
    }
    
    public Result markHit(int row, int col) { 
        return setCell(row, col, HIT); 
    }
    
    public Result markMiss(int row, int col) { 
        return setCell(row, col, MISS); 
    }
    
    public Result placeShip(int r0, int c0, int length, Direction dir) {
        return fillLine(r0, c0, length, dir, SHIP);
}

    @Override public String toString() {
        StringBuilder sb = new StringBuilder(size * (2 * size + 1));
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                sb.append(grid[r][c]).append(' ');
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
