package battleship.enums;

public enum Cell {
    WATER(' '),
    SHIP('S'),
    HIT('X'),
    MISS('O');

    public final char symbol;
    Cell(char symbol) { this.symbol = symbol; }
}
