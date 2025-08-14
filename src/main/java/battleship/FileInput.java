package battleship;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileInput extends BoardFileIO {

    public Board[] loadMatch(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String sizeLine = req(br.readLine(), "size");
            int n = Integer.parseInt(sizeLine.trim());

            expect(req(br.readLine(), "PLAYER1 header"), "PLAYER1");
            Board p1 = new Board(n);
            readSection(br, p1, "SHIPS", Board.GridType.SHIPS, n);
            readSection(br, p1, "SHOTS", Board.GridType.SHOTS, n);

            expect(req(br.readLine(), "PLAYER2 header"), "PLAYER2");
            Board p2 = new Board(n);
            readSection(br, p2, "SHIPS", Board.GridType.SHIPS, n);
            readSection(br, p2, "SHOTS", Board.GridType.SHOTS, n);

            return new Board[]{ p1, p2 };
        }
    }

    // helpers
    private static String req(String s, String what) throws IOException {
        if (s == null) throw new IOException("Corrupt save: missing " + what);
        return s;
    }

    private static void expect(String got, String want) throws IOException {
        if (!want.equals(got)) throw new IOException("Expected '" + want + "', got: '" + got + "'");
    }

    private void readSection(BufferedReader br, Board board,
                             String expectedHeader, Board.GridType which, int n) throws IOException {
        String hdr = req(br.readLine(), expectedHeader + " header");
        if (!expectedHeader.equals(hdr))
            throw new IOException("Expected '" + expectedHeader + "', got: '" + hdr + "'");

        for (int r = 0; r < n; r++) {
            String line = req(br.readLine(), "row " + r + " for " + which);
            if (line.length() < n)
                throw new IOException("Short row " + r + " for " + which + ": " + line.length() + " < " + n);
            for (int c = 0; c < n; c++) {
                char state = decode(line.charAt(c));
                Board.Result res = board.setCell(r, c, state, which);
                if (res != Board.Result.OK)
                    throw new IOException("Failed to set (" + r + "," + c + "): " + res);
            }
        }
    }
}
