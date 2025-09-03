package battleship;

public abstract class BoardFileIO {

    //Converts board states to a readable output within the save file
    protected char encode(char state) {
        if (state == Board.WATER) return '.';
        if (state == Board.SHIP)  return 'S';
        if (state == Board.HIT)   return 'X';
        if (state == Board.MISS)  return 'O';
        return '?';
    }

    //Converts save file back to ship stated
    protected char decode(char ch) {
        if (ch == '.') return Board.WATER;
        if (ch == 'S') return Board.SHIP;
        if (ch == 'X') return Board.HIT;
        if (ch == 'O') return Board.MISS;
        return Board.WATER;
    }
}
