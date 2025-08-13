package battleship;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileInput extends BoardFileIO {

    @Override
    public void process(Board board) {
        try {
            loadFromFile(board, defaultFile);
            System.out.println("Board loaded from: " + defaultFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error loading board: " + e.getMessage());
        }
    }

    public void loadFromFile(Board board, File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int size = Integer.parseInt(reader.readLine().trim());
            if (size != board.getSize()) {
                throw new IllegalArgumentException("Board size mismatch!");
            }

            for (int r = 0; r < size; r++) {
                String line = reader.readLine();
                for (int c = 0; c < size; c++) {
                    char state = decode(line.charAt(c));
                    board.setCell(r, c, state);
                }
            }
        }
        System.out.println("Board loaded from: " + file.getAbsolutePath());
    }
}
