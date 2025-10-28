package battleship.gui_game;

import java.awt.Color;
import javax.swing.JButton;
import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.domain.Board;

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
}
