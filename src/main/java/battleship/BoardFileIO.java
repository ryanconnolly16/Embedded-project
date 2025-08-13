package battleship;

import java.io.File;
import java.io.IOException;

public abstract class BoardFileIO {
    protected final File defaultFile = new File("save.log");

    protected char encode(char state) {
        if (state == Board.WATER) return '.';
        if (state == Board.SHIP)  return 'S';
        if (state == Board.HIT)   return 'X';
        if (state == Board.MISS)  return 'O';
        return '?';
    }

    protected char decode(char ch) {
        if (ch == '.') return Board.WATER;
        if (ch == 'S') return Board.SHIP;
        if (ch == 'X') return Board.HIT;
        if (ch == 'O') return Board.MISS;
        return Board.WATER; // default fallback
    }

    protected int getBoardSize(Board board) {
        return board.getSize();
    }
    
    public abstract void process(Board board) throws IOException;
}