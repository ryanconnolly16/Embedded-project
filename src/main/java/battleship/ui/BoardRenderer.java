package battleship.ui;


import battleship.enums.Cell;
import battleship.interfaces.Glyphs;
import battleship.enums.GridType;
import battleship.interfaces.*;
import battleship.domain.Board;
import static battleship.enums.GridType.*;
import java.util.ArrayList;
import java.util.List;
public final class BoardRenderer {
    private BoardRenderer() {}

    public static String renderBoth(ReadOnlyBoard b, Glyphs g) {
        return renderBoth(b, g, 3, 1, true, true);
    }

    public static String renderBoth(
        ReadOnlyBoard b, 
        Glyphs g,
        int cellW, int cellH,
        boolean showTopLetters,
        boolean showBottomLetters) {
        
        List<GridType> stringList = new ArrayList<>();
        stringList.add(SHIPS);
        stringList.add(SHOTS);

        String left  = renderGrid(b, GridType.SHIPS, g, cellW, cellH, showTopLetters, showBottomLetters);
        String right = renderGrid(b, GridType.SHOTS, g, cellW, cellH, showTopLetters, showBottomLetters);

        String[] L = left.split("\n");
        String[] R = right.split("\n");
        
        int lw = (L.length == 0 ? 0 : L[0].length());
        int rw = (R.length == 0 ? 0 : R[0].length());
        
        String titleL = center("[ Ships ]", lw);
        String titleR = center("[ Hits / Misses ]", rw);
        
        String gap = "   ";

        StringBuilder out = new StringBuilder((L.length + 2) * (lw + rw + gap.length() + 1));
        out.append(titleL).append(gap).append(titleR).append('\n');
        
        int rows = Math.max(L.length, R.length);
        
        for (int i = 0; i < rows; i++) {
            String l = (i < L.length) ? L[i] : " ".repeat(lw);
            String r = (i < R.length) ? R[i] : " ".repeat(rw);
            
            out.append(l).append(gap).append(r).append('\n');
        }
        return out.toString();
    }

    public static String renderGrid(
        ReadOnlyBoard board,
        GridType which,
        Glyphs glyphs,
        int cellW, int cellH,
        boolean showTopLetters,
        boolean showBottomLetters) {
    
        int n = board.size();
        int rowWidth = Integer.toString(n).length();
        
        if (cellW < 1) cellW = 1;
        if (cellH < 1) cellH = 1;

        StringBuilder sb = new StringBuilder(n * (n * (cellW + 2) * cellH + 64));

        // Top letters
        if (showTopLetters) {
            pad(sb, rowWidth + 2);
            for (int c = 0; c < n; c++) {
                sb.append(center(String.valueOf((char) ('A' + c)), cellW));
                if (c < n - 1) sb.append(' ');
            }
            sb.append(' ').append('\n');
        }

        // Top border
        pad(sb, rowWidth + 1); sb.append('+');
        for (int c = 0; c < n; c++) { repeat(sb, '-', cellW); sb.append('+'); }
        sb.append('\n');

        // Rows
        for (int r = 0; r < n; r++) {
            for (int inner = 0; inner < cellH; inner++) {
                if (inner == cellH / 2) sb.append(String.format("%" + rowWidth + "d", r + 1)).append(' ');
                else pad(sb, rowWidth + 1);
                sb.append('|');
                for (int c = 0; c < n; c++) {
                    char glyph = (inner == cellH / 2)
                            ? glyphs.glyph(board.cellAt(r, c, which))
                            : ' ';
                    String content = (inner == cellH / 2)
                            ? center(String.valueOf(glyph), cellW)
                            : " ".repeat(cellW);
                    sb.append(content).append('|');
                }
                sb.append('\n');
            }
            // Row separator
            if (r != n - 1) {
                pad(sb, rowWidth + 1); sb.append('+');
                for (int c = 0; c < n; c++) { repeat(sb, '-', cellW); sb.append('+'); }
                sb.append('\n');
            }
        }

        // Bottom border
        pad(sb, rowWidth + 1); sb.append('+');
        for (int c = 0; c < n; c++) { repeat(sb, '-', cellW); sb.append('+'); }
        sb.append('\n');

        // Bottom letters
        if (showBottomLetters) {
            pad(sb, rowWidth + 2);
            for (int c = 0; c < n; c++) {
                sb.append(center(String.valueOf((char) ('a' + c)), cellW));
                if (c < n - 1) sb.append(' ');
            }
            sb.append(' ').append('\n');
        }

        return sb.toString();
    }

    // helpers
    private static void repeat(StringBuilder sb, char ch, int count) {
        for (int i = 0; i < count; i++) sb.append(ch);
    }

    private static void pad(StringBuilder sb, int n) {
        for (int i = 0; i < n; i++) sb.append(' ');
    }

    private static String center(String string, int width) {
        if (width <= 1) return string.substring(0, Math.min(1, string.length()));
        if (string.length() >= width) return string.substring(0, width);
        int left = (width - string.length()) / 2;
        int right = width - string.length() - left;
        return " ".repeat(left) + string + " ".repeat(right);
    }
}
