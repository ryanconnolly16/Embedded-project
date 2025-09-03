package battleship.domain;

import battleship.enums.*;
import battleship.interfaces.ReadOnlyBoard;

public class Board implements ReadOnlyBoard {
    private final Cell[][] shipGrid;
    private final Cell[][] hitMissGrid;
    private final int size;

    public Board(int size) {
        if (size < 1 || size > 20) throw new IllegalArgumentException("size 1–20 only");
        this.size = size;
        shipGrid = new Cell[size][size];
        hitMissGrid = new Cell[size][size];
        fillWater();
    }

    private void fillWater() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                shipGrid[r][c] = Cell.WATER;
                hitMissGrid[r][c] = Cell.WATER;
            }
        }
    }

    @Override
    public int size() { return size; }

    @Override
    public Cell cellAt(int row, int col, GridType which) {
        checkBounds(row, col);
        return (which == GridType.SHIPS) ? shipGrid[row][col] : hitMissGrid[row][col];
    }

    private void checkBounds(int r, int c) {
        if (r < 0 || r >= size || c < 0 || c >= size)
            throw new IndexOutOfBoundsException("row=" + r + " col=" + c);
    }

    public Result setCell(int row, int col, Cell state, GridType which) {
        if (row < 0 || row >= size || col < 0 || col >= size) return Result.OUT_OF_BOUNDS;
        if (which == GridType.SHIPS) shipGrid[row][col] = state;
        else hitMissGrid[row][col] = state;
        return Result.OK;
    }

    // ship placement
    public Result placeShip(int r0, int c0, int length, Direction dir) {
        if (length <= 0) return Result.INVALID_LENGTH;
        int r1 = r0 + dir.deltar * (length - 1);
        int c1 = c0 + dir.deltac * (length - 1);
        if (!inBounds(r0, c0) || !inBounds(r1, c1)) return Result.OUT_OF_BOUNDS;

        for (int i = 0; i < length; i++) {
            int rr = r0 + dir.deltar * i;
            int cc = c0 + dir.deltac * i;
            if (shipGrid[rr][cc] != Cell.WATER) return Result.OCCUPIED;
        }

        for (int i = 0; i < length; i++) {
            int rr = r0 + dir.deltar * i;
            int cc = c0 + dir.deltac * i;
            shipGrid[rr][cc] = Cell.SHIP;
        }
        return Result.OK;
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }

    // mark hits/misses
    public Result markHit(int row, int col) { return setCell(row, col, Cell.HIT, GridType.SHOTS); }
    public Result markMiss(int row, int col){ return setCell(row, col, Cell.MISS, GridType.SHOTS); }
    public Result shipHit(int row, int col) { return setCell(row, col, Cell.HIT, GridType.SHIPS); }
    public Result shipMiss(int row, int col){ return setCell(row, col, Cell.MISS, GridType.SHIPS); }
}
