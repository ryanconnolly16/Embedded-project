package battleship.enums;

public enum Direction {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);
    public final int deltar, deltac;
    Direction(int dr, int dc) { this.deltar = dr; this.deltac = dc; }
}
