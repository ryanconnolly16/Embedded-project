package battleship.io;

import battleship.enums.Cell;

public abstract class CellSymbolIO {
    protected Cell decode(char ch) {
        return switch (ch) {
            case 'S' -> Cell.SHIP;
            case 'X' -> Cell.HIT;
            case 'O' -> Cell.MISS;
            case '.' -> Cell.WATER;   // dot means water in files
            default  -> Cell.WATER;   // fallback to water
        };
    }

    protected char encode(Cell cell) {
        return (cell == Cell.WATER) ? '.' : cell.symbol;  // Water = '.', others keep their symbol
    }
}