package battleship;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOutput extends BoardFileIO {

    
    //Saves the match to a file with a specific format
    public void saveMatch(Board player1, Board player2, File file) throws IOException {
        int n = player1.getSize();
        if (n != player2.getSize())
            throw new IllegalArgumentException("Boards must be same size.");

        try (BufferedWriter w = new BufferedWriter(new FileWriter(file))) {
            w.write(Integer.toString(n)); 
            w.newLine();

            w.write("PLAYER1"); w.newLine();
            w.write("SHIPS");   w.newLine();
            for (int r = 0; r < n; r++) { 
                for (int c = 0; c < n; c++) 
                    w.write(encode(player1.cellAt(r,c, Board.GridType.SHIPS))); 
                w.newLine(); 
            }
            w.write("SHOTS");   w.newLine();
            for (int r = 0; r < n; r++) { 
                for (int c = 0; c < n; c++) 
                    w.write(encode(player1.cellAt(r,c, Board.GridType.SHOTS))); 
                w.newLine(); 
            }

            w.write("PLAYER2"); w.newLine();
            w.write("SHIPS");   w.newLine();
            for (int r = 0; r < n; r++) { 
                for (int c = 0; c < n; c++) 
                    w.write(encode(player2.cellAt(r,c, Board.GridType.SHIPS))); 
                w.newLine(); 
            }
            w.write("SHOTS");   w.newLine();
            for (int r = 0; r < n; r++) { 
                for (int c = 0; c < n; c++) 
                    w.write(encode(player2.cellAt(r,c, Board.GridType.SHOTS))); 
                w.newLine(); 
            }
        }
    }
}
