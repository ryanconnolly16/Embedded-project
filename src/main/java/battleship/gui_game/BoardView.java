// battleship/gui_game/BoardView.java
package battleship.gui_game;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;

public final class BoardView {
    private BoardView() {}

    // ----- Palette -----
    private static final Color WATER = new Color(220, 235, 255);
    private static final Color SHIP  = new Color(60, 60, 60);
    private static final Color HIT   = new Color(200,  40,  40);
    private static final Color MISS  = new Color(210, 210, 210);

    // ----- Symbol fallbacks -----
    private static final char CIRCLE   = '\u25CF'; // ●
    private static final char BULLET   = '\u2022'; // •
    private static final char MULTIPLY = '\u00D7'; // ×

    private static String hitMarkFor(JButton b) {
        Font f = b.getFont();
        if (f != null) {
            if (f.canDisplay(CIRCLE)) return "●";
            if (f.canDisplay(BULLET)) return "•";
        }
        return "O";
    }

    private static String missMarkFor(JButton b) {
        Font f = b.getFont();
        if (f != null && f.canDisplay(MULTIPLY)) return "×";
        return "X";
    }

    private static Color textOn(Color bg) {
        double y = 0.2126 * bg.getRed() + 0.7152 * bg.getGreen() + 0.0722 * bg.getBlue();
        return (y < 140.0) ? Color.WHITE : Color.BLACK;
    }

    private static void colorButton(JButton b, Color bg, String text) {
        b.setText(text);
        b.setForeground(textOn(bg));
        b.setBackground(bg); // read by our custom UI
    }

    // ----- Rendering -----
    // Render player's own board (SHIPS grid).
    public static void renderShips(JButton[][] shipsButtons, Board board) {
        int n = shipsButtons.length;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < shipsButtons[r].length; c++) {
                JButton b = shipsButtons[r][c];
                Cell cell = board.cellAt(r, c, GridType.SHIPS);
                switch (cell) {
                    case SHIP -> colorButton(b, SHIP,  "");
                    case HIT  -> colorButton(b, HIT,   hitMarkFor(b));
                    case MISS -> colorButton(b, MISS,  missMarkFor(b));
                    default   -> colorButton(b, WATER, "");
                }
            }
        }
    }

    // Render fog-of-war view (SHOTS grid).
    public static void renderShots(JButton[][] shotsButtons, Board board) {
        int n = shotsButtons.length;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < shotsButtons[r].length; c++) {
                JButton b = shotsButtons[r][c];
                Cell cell = board.cellAt(r, c, GridType.SHOTS);
                switch (cell) {
                    case HIT  -> colorButton(b, HIT,  hitMarkFor(b));
                    case MISS -> colorButton(b, MISS, missMarkFor(b));
                    default   -> colorButton(b, WATER, "");
                }
            }
        }
    }

    // Convenience to update both at once.
    public static void refreshAll(JButton[][] shipsButtons, JButton[][] shotsButtons, Board board) {
        renderShips(shipsButtons, board);
        renderShots(shotsButtons, board);
    }

    // Apply the nice tile style once after grid creation.
    public static void styleGrids(JButton[][] shipsButtons, JButton[][] shotsButtons) {
        if (shipsButtons != null) applyGridStyle(shipsButtons);
        if (shotsButtons != null) applyGridStyle(shotsButtons);
    }

    // ----- Fancy cell look -----
    private static void applyGridStyle(JButton[][] grid) {
        int rows = grid.length;
        if (rows == 0) return;
        int cols = grid[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton b = grid[r][c];
                if (b == null) continue;

                b.setUI(CellButtonUI.INSTANCE);
                b.setRolloverEnabled(true);
                b.setContentAreaFilled(false);
                b.setOpaque(false);
                b.setFocusPainted(false);
                b.setBorder(new EmptyBorder(2, 2, 2, 2)); // small gap between tiles

                // Symbol font scales with size
                b.addComponentListener(new ComponentAdapter() {
                    @Override public void componentResized(ComponentEvent e) {
                        int h = Math.max(1, b.getHeight());
                        float sz = clamp(14f, h * 0.50f, 28f);
                        b.setFont(b.getFont().deriveFont(Font.BOLD, sz));
                    }
                });
                int h = Math.max(1, b.getPreferredSize().height);
                float sz = clamp(14f, h * 0.50f, 28f);
                b.setFont(b.getFont().deriveFont(Font.BOLD, sz));
            }
        }
    }

    private static float clamp(float lo, float v, float hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    // Custom UI that paints rounded tiles with gradient + hover/press.
    private static final class CellButtonUI extends BasicButtonUI {
        static final CellButtonUI INSTANCE = new CellButtonUI();
        private CellButtonUI() {}

        @Override public void paint(Graphics g, JComponent c) {
            JButton b = (JButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                int w = c.getWidth(), h = c.getHeight();
                int arc = Math.round(Math.min(w, h) * 0.25f); // 25% roundness

                Color base = b.getBackground();
                ButtonModel m = b.getModel();
                if (m.isPressed()) base = blend(base, Color.BLACK, 0.18f);
                else if (m.isRollover()) base = blend(base, Color.WHITE, 0.10f);

                Shape rr = new RoundRectangle2D.Float(1, 1, w - 2, h - 2, arc, arc);

                // Soft gradient
                Paint grad = new GradientPaint(0, 0, blend(base, Color.WHITE, 0.12f),
                                               0, h, blend(base, Color.BLACK, 0.10f));
                g2.setPaint(grad);
                g2.fill(rr);

                // Border + inner highlight
                g2.setStroke(new BasicStroke(1f));
                g2.setColor(new Color(255, 255, 255, 50));
                Shape inner = new RoundRectangle2D.Float(2, 2, w - 4, h - 4, Math.max(arc - 2, 0), Math.max(arc - 2, 0));
                g2.draw(inner);

                g2.setColor(blend(base, Color.BLACK, 0.30f));
                g2.draw(rr);

                if (!b.isEnabled()) {
                    g2.setColor(new Color(255, 255, 255, 120));
                    g2.fill(rr);
                }

                // Let BasicButtonUI draw the text (● / ×)
                super.paint(g2, c);
            } finally {
                g2.dispose();
            }
        }

        private static Color blend(Color a, Color b, float t) {
            t = Math.max(0f, Math.min(1f, t));
            int r = Math.round(a.getRed()   * (1 - t) + b.getRed()   * t);
            int g = Math.round(a.getGreen() * (1 - t) + b.getGreen() * t);
            int bl= Math.round(a.getBlue()  * (1 - t) + b.getBlue()  * t);
            return new Color(r, g, bl);
        }
    }
}
