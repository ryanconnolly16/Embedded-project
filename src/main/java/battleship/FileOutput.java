package battleship;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOutput extends BoardFileIO {
    
    @Override
    public void process(Board board, char[][] grid) {
        try {
            saveToFile(board, defaultFile, grid);
            System.out.println("Board saved to: " + defaultFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving board: " + e.getMessage());
        }
    }

    public void saveToFile(Board board, File file, char[][] grid) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int size = getBoardSize(board);
            writer.write(Integer.toString(size));
            writer.newLine();
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    writer.write(encode(board.cellState(r, c, grid)));
                }
                writer.newLine();
            }
        }
        System.out.println("Board saved to: " + file.getAbsolutePath());
    }
}