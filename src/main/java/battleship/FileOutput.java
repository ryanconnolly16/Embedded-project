package battleship;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOutput extends BoardFileIO {

    @Override
    public void process(Board board, Board.GridType which) {
        try {
            saveToFile(board, defaultFile, which);
            System.out.println("Board saved to: " + defaultFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving board: " + e.getMessage());
        }
    }

    public void saveToFile(Board board, File file, Board.GridType which) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int n = getBoardSize(board);
            writer.write(Integer.toString(n));
            writer.newLine();
            writer.write(which.name()); // store which grid this is
            writer.newLine();

            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    char cell = board.cellAt(r, c, which);
                    writer.write(encode(cell));
                }
                writer.newLine();
            }
        }
    }

    public void saveBoth(Board board, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int n = getBoardSize(board);
            writer.write(Integer.toString(n)); writer.newLine();

            // SHIPS section
            writer.write("SHIPS"); writer.newLine();
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) writer.write(encode(board.cellAt(r, c, Board.GridType.SHIPS)));
                writer.newLine();
            }

            // SHOTS section
            writer.write("SHOTS"); writer.newLine();
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) writer.write(encode(board.cellAt(r, c, Board.GridType.SHOTS)));
                writer.newLine();
            }
        }
    }
}
