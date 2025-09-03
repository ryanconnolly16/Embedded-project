package battleship.interfaces;

import battleship.enums.Cell;
import battleship.enums.GridType;

public interface ReadOnlyBoard {
    int size();
    Cell cellAt(int row, int col, GridType which);
}
