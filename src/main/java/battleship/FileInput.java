package battleship;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileInput extends BoardFileIO {

    @Override
    public void process(Board board, Board.GridType which) {
        try {
            loadFromFile(board, defaultFile, which);
            System.out.println("Board loaded from: " + defaultFile.getAbsolutePath());
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading board: " + e.getMessage());
        }
    }

    public void loadFromFile(Board board, File file, Board.GridType which) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String first = br.readLine();
            if (first == null) throw new IOException("Empty save file");
            int n = Integer.parseInt(first.trim());
            if (n != board.getSize()) throw new IllegalArgumentException("Board size mismatch: file=" + n + ", board=" + board.getSize());

            // Peek the next line to decide format
            br.mark(4096);
            String header = br.readLine();
            if (header == null) throw new IOException("Unexpected EOF after size");

            header = header.trim().toUpperCase();

            if ("SHIPS".equals(header) || "SHOTS".equals(header)) {
                // Format B: dual-sections present. Load both sections into the board.
                loadSection(br, board, n, headerToGridType(header)); // first section
                String header2 = br.readLine();
                if (header2 == null) throw new IOException("Missing second section header");
                header2 = header2.trim().toUpperCase();
                loadSection(br, board, n, headerToGridType(header2)); // second section
            } else {
                // Format A: single grid; the line we read is likely the first data row or an optional WHICH header
                Board.GridType target = which;
                if ("SHIPS".equalsIgnoreCase(header) || "SHOTS".equalsIgnoreCase(header)) {
                    target = headerToGridType(header);
                } else {
                    // header is actually the first data row; rewind to re-read it as data
                    br.reset();
                }
                loadBody(br, board, n, target);
            }
        }
    }

    private static Board.GridType headerToGridType(String s) {
        return "SHIPS".equalsIgnoreCase(s) ? Board.GridType.SHIPS : Board.GridType.SHOTS;
    }

    private void loadSection(BufferedReader br, Board board, int n, Board.GridType which) throws IOException {
        loadBody(br, board, n, which);
    }

    private void loadBody(BufferedReader br, Board board, int n, Board.GridType which) throws IOException {
        for (int r = 0; r < n; r++) {
            String line = br.readLine();
            if (line == null || line.length() < n)
                throw new IOException("Corrupt save: missing or short row at r=" + r);
            for (int c = 0; c < n; c++) {
                char state = decode(line.charAt(c));
                // write into the requested grid
                Board.Result res = board.setCell(r, c, state, which);
                if (res != Board.Result.OK)
                    throw new IOException("Failed to set cell ("+r+","+c+"): "+res);
            }
        }
    }
}
