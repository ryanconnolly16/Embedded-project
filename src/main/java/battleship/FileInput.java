package battleship;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileInput extends BoardFileIO {

    @Override
    public void process(Board player1, Board player2) {
        try {
            Board[] boards = loadMatch(defaultFile);
            copyBoard(boards[0], player1);
            copyBoard(boards[1], player2);
            System.out.println("Boards loaded from: " + defaultFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error loading boards: " + e.getMessage());
        }
    }

    public Board[] loadMatch(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sizeLine = br.readLine();
            if (sizeLine == null) throw new IOException("Empty save file");
            int n = Integer.parseInt(sizeLine.trim());

            Board p1 = new Board(n);
            Board p2 = new Board(n);

            readSection(br, p1, "SHIPS", Board.GridType.SHIPS, n);
            readSection(br, p1, "SHOTS", Board.GridType.SHOTS, n);
            readSection(br, p2, "SHIPS", Board.GridType.SHIPS, n);
            readSection(br, p2, "SHOTS", Board.GridType.SHOTS, n);

            return new Board[]{p1, p2};
        }
    }

    private void readSection(BufferedReader br, Board board,
                              String expectedHeader, Board.GridType which, int n) throws IOException {
        String hdr = br.readLine();
        if (!expectedHeader.equals(hdr))
            throw new IOException("Expected " + expectedHeader + " header, got: " + hdr);
        for (int r = 0; r < n; r++) {
            String line = br.readLine();
            if (line == null || line.length() < n)
                throw new IOException("Corrupt row " + r + " for " + which);
            for (int c = 0; c < n; c++) {
                char state = decode(line.charAt(c));
                Board.Result res = board.setCell(r, c, state, which);
                if (res != Board.Result.OK)
                    throw new IOException("Failed to set (" + r + "," + c + "): " + res);
            }
        }
    }

    private void copyBoard(Board src, Board dest) {
        int n = src.getSize();
        for (Board.GridType type : Board.GridType.values()) {
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    dest.setCell(r, c, src.cellAt(r, c, type), type);
                }
            }
        }
    }
}
