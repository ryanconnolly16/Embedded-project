package battleship;

public final class BoardRenderer {
    private BoardRenderer() {}

    // Default
    public static String render(Board board) {
        return renderAsciiGrid(board, 3, 1, true, true, false, grid);
    }

    // Don't use
    public static String renderAsciiGrid(Board b, int cellW, int cellH, boolean showTopLetters, boolean showBottomLetters, boolean hideShips, char[][] grid) {
        int n = b.getSize();
        int rowW = Integer.toString(n).length();
        if (cellW < 1) cellW = 1;
        if (cellH < 1) cellH = 1;

        

        StringBuilder sb = new StringBuilder(n * (n * (cellW + 2) * cellH + 64));

        // Top letters
        if (showTopLetters) {
            pad(sb, rowW + 2);
            for (int c = 0; c < n; c++) {
                sb.append(centerStr(String.valueOf((char) ('A' + c)), cellW));
                if (c < n - 1) sb.append(' ');
            }
            sb.append('\n');
        }

        // Top border
        pad(sb, rowW + 1);
        sb.append('+');
        for (int c = 0; c < n; c++) {
            repeat(sb, '-', cellW);
            sb.append(c == n - 1 ? '+' : '+');
        }
        sb.append('\n');

        // Rows
        for (int r = 0; r < n; r++) {
            for (int inner = 0; inner < cellH; inner++) {
                // Row label on the middle inner line
                if (inner == cellH / 2) {
                    sb.append(String.format("%" + rowW + "d", r + 1)).append(' ');
                } else {
                    pad(sb, rowW + 1);
                }

                // Left border
                sb.append('|');

                // Cells
                for (int c = 0; c < n; c++) {
                    char raw = b.getCell(r, c, grid);
                    if (hideShips && raw == 'S') raw = ' '; // fog of war (change to whatever we want)
                    char glyph = (inner == cellH / 2) ? mapAsciiGlyph(raw) : ' ';
                    String content = (inner == cellH / 2) ? centerStr(String.valueOf(glyph), cellW) : spaces(cellW);
                    sb.append(content);

                    // Column separator
                    sb.append('|');
                }

                sb.append('\n');
            }

            // Row separator
            if (r != n - 1) {
                pad(sb, rowW + 1);
                sb.append('+');
                for (int c = 0; c < n; c++) {
                    repeat(sb, '-', cellW);
                    sb.append('+');
                }
                sb.append('\n');
            }
        }

        // Bottom border
        pad(sb, rowW + 1);
        sb.append('+');
        for (int c = 0; c < n; c++) {
            repeat(sb, '-', cellW);
            sb.append('+');
        }
        sb.append('\n');

        // Bottom letters
        if (showBottomLetters) {
            pad(sb, rowW + 2);
            for (int c = 0; c < n; c++) {
                sb.append(centerStr(String.valueOf((char) ('a' + c)), cellW));
                if (c < n - 1) sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Incase we want to change later
    private static char mapAsciiGlyph(char cell) {
        if (cell == Board.WATER) return ' ';  // water
        if (cell == Board.SHIP) return 'S';  // ship
        if (cell == Board.HIT) return 'X';  // hit
        if (cell == Board.MISS) return 'O';  // miss
        return cell;
    }
    // Helpers
    private static void repeat(StringBuilder sb, char ch, int count) {
        for (int i = 0; i < count; i++) sb.append(ch);
    }

    private static String spaces(int n) {
        return (n <= 0) ? "" : " ".repeat(n);
    }

    private static void pad(StringBuilder sb, int n) {
        for (int i = 0; i < n; i++) sb.append(' ');
    }

    private static String centerStr(String s, int width) {
        if (width <= 1) return s.substring(0, Math.min(1, s.length()));
        if (s.length() >= width) return s.substring(0, width);
        int left = (width - s.length()) / 2;
        int right = width - s.length() - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }
}
