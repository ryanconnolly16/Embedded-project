package battleship.io;

import battleship.domain.Board;
import battleship.enums.GridType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOutput extends CellSymbolIO {

    // Saves the match to a file with a specific format
    public void saveMatch(Board player1, Board player2, File file) throws IOException {
        int n = player1.size();
        if (n != player2.size())
            throw new IllegalArgumentException("Boards must be same size.");

        try (BufferedWriter w = new BufferedWriter(new FileWriter(file))) {
            w.write(Integer.toString(n));
            w.newLine();

            writeBoard(w, "PLAYER1", player1, n);
            writeBoard(w, "PLAYER2", player2, n);
        }
    }
    
    // Saves the match to a file with a specific format
    private void writeBoard(BufferedWriter w, String playerName, Board board, int n) throws IOException {
        w.write(playerName); w.newLine();

        w.write("SHIPS"); w.newLine();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++)
                w.write(encode(board.cellAt(r, c, GridType.SHIPS)));
            w.newLine();
        }

        w.write("SHOTS"); w.newLine();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++)
                w.write(encode(board.cellAt(r, c, GridType.SHOTS)));
            w.newLine();
        }
    }
}
