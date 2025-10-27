package battleship.io;

import battleship.enums.Cell;

public abstract class CellSymbolIO {
    protected Cell decode(char ch) {
        return switch (ch) {
            case 'S' -> Cell.SHIP;
            case 'X' -> Cell.HIT;
            case 'O' -> Cell.MISS;
            default  -> Cell.WATER;
        };
    }

    protected char encode(Cell cell) {
        return cell.symbol;  // Use the enum's symbol field
    }
}
