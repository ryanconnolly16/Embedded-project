package battleship;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOutput extends BoardFileIO {

    
    public static void main(String[] args) {

        Board New = new Board(20);
//        board.placeShip(6, 0, 5, Board.Direction.UP);
//        board.markHit(0, 0);
//
//        FileOutput saver = new FileOutput();
//        saver.process(board); // Save


        FileInput loader = new FileInput();
//        loader.process(board); // Load  
        loader.process(New);

//        System.out.println(BoardRenderer.render(board));
    }
    
    @Override
    public void process(Board board) {
        try {
            saveToFile(board, defaultFile);
            System.out.println("Board saved to: " + defaultFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving board: " + e.getMessage());
        }
    }

    public void saveToFile(Board board, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int size = getBoardSize(board);
            writer.write(Integer.toString(size));
            writer.newLine();
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    writer.write(encode(board.cellState(r, c)));
                }
                writer.newLine();
            }
        }
        System.out.println("Board saved to: " + file.getAbsolutePath());
    }
}