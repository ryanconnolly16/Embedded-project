package battleship.gui_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;

public final class BoardView {
    private BoardView() {}

    // Palette
    private static final Color WATER = new Color(220, 235, 255);
    private static final Color SHIP  = new Color( 80, 110, 160);
    private static final Color HIT   = new Color(200,  40,  40);
    private static final Color MISS  = new Color(210, 210, 210);
    private static final Color TEXT  = Color.BLACK;

    private static void colorButton(JButton b, Color bg, String text) {
        b.setText(text);
        b.setForeground(TEXT);
        b.setBackground(bg);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
    }

    /** Render player's own board (SHIPS grid). */
    public static void renderShips(JButton[][] shipsButtons, Board board) {
        int n = shipsButtons.length;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < shipsButtons[r].length; c++) {
                JButton b = shipsButtons[r][c];
                Cell cell = board.cellAt(r, c, GridType.SHIPS);
                switch (cell) {
                    case SHIP -> colorButton(b, SHIP,  "");
                    case HIT  -> colorButton(b, HIT,   "●");
                    case MISS -> colorButton(b, MISS,  "×");
                    default   -> colorButton(b, WATER, "");
                }
            }
        }
    }

    /** Render fog-of-war view (SHOTS grid). */
    public static void renderShots(JButton[][] shotsButtons, Board board) {
        int n = shotsButtons.length;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < shotsButtons[r].length; c++) {
                JButton b = shotsButtons[r][c];
                Cell cell = board.cellAt(r, c, GridType.SHOTS);
                switch (cell) {
                    case HIT  -> colorButton(b, HIT,  "●");
                    case MISS -> colorButton(b, MISS, "×");
                    default   -> colorButton(b, WATER, "");
                }
            }
        }
    }

    /** Convenience to update both at once. */
    public static void refreshAll(JButton[][] shipsButtons, JButton[][] shotsButtons, Board board) {
        renderShips(shipsButtons, board);
        renderShots(shotsButtons, board);
    }

    /** Optional chrome: apply crisp borders/fonts once after grid creation. */
    public static void styleGrids(JButton[][] shipsButtons, JButton[][] shotsButtons) {
        if (shipsButtons != null)  applyGridStyle(shipsButtons);
        if (shotsButtons != null)  applyGridStyle(shotsButtons);
    }

    private static void applyGridStyle(JButton[][] grid) {
        int rows = grid.length;
        if (rows == 0) return;
        int cols = grid[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton b = grid[r][c];
                if (b == null) continue;

                // Base look
                b.setMargin(new Insets(0, 0, 0, 0));
                b.setFocusPainted(false);
                b.setContentAreaFilled(true);
                b.setOpaque(true);

                // Light grid lines with thicker outer border
                int top    = (r == 0)      ? 2 : 1;
                int left   = (c == 0)      ? 2 : 1;
                int bottom = (r == rows-1) ? 2 : 0;
                int right  = (c == cols-1) ? 2 : 0;

                Color line  = new Color(205, 214, 226);
                Color outer = new Color(170, 180, 195);

                b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(top, 0, 0, 0, (r == 0) ? outer : line),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, left, 0, 0, (c == 0) ? outer : line),
                        BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, bottom, 0, (r == rows - 1) ? outer : line),
                            BorderFactory.createMatteBorder(0, 0, 0, right, (c == cols - 1) ? outer : line)
                        )
                    )
                ));

                // Symbol size (approx) — will be overridden when rendered
                int h = Math.max(1, b.getPreferredSize().height);
                float sz = Math.max(14f, Math.min(26f, h * 0.44f));
                b.setFont(b.getFont().deriveFont(Font.BOLD, sz));

                // Hover removed to avoid color trails
            }
        }
    }
}
