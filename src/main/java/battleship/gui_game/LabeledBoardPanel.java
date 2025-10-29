package battleship.gui_game;

import javax.swing.*;
import java.awt.*;

public class LabeledBoardPanel extends JPanel {
    private final SquareGridPanel grid;

    // header strip sizes (px)
    private static final int TOP_STRIP  = 22;
    private static final int LEFT_STRIP = 22;

    public LabeledBoardPanel(int rows, int cols, int gap, String title) {
        setLayout(new BorderLayout(6, 6));
        setOpaque(false);

        if (title != null && !title.isEmpty()) {
            JLabel t = new JLabel(title, JLabel.CENTER);
            t.setFont(t.getFont().deriveFont(Font.BOLD));
            add(t, BorderLayout.NORTH);
        }

        // Absolute layout so we can place grid + painters precisely
        JPanel center = new JPanel(null);
        center.setOpaque(false);

        grid = new SquareGridPanel(rows, cols, gap);
        center.add(grid);

        // Painter for TOP numbers (1..cols) in the top strip
        JComponent topPainter = new JComponent() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(new Color(30, 30, 30));
                FontMetrics fm = g2.getFontMetrics(getFont());

                JButton[][] btns = grid.getButtons();
                if (btns.length == 0 || btns[0].length == 0) { g2.dispose(); return; }

                // translate button bounds (grid-local) to this painter (center-local)
                int gx = grid.getX();
                int gy = grid.getY();

                for (int c = 0; c < btns[0].length; c++) {
                    Rectangle r = btns[0][c].getBounds();
                    int cx = gx + r.x + r.width / 2;
                    String txt = String.valueOf(c + 1);
                    int x = cx - fm.stringWidth(txt) / 2;
                    int y = gy - Math.max(4, fm.getDescent());  // draw in the TOP strip, above grid
                    g2.drawString(txt, x, Math.max(fm.getAscent(), y));
                }
                g2.dispose();
            }
        };
        topPainter.setOpaque(false);
        center.add(topPainter);

        // Painter for LEFT letters (A..rows) in the left strip
        JComponent leftPainter = new JComponent() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(new Color(30, 30, 30));
                FontMetrics fm = g2.getFontMetrics(getFont());

                JButton[][] btns = grid.getButtons();
                if (btns.length == 0 || btns[0].length == 0) { g2.dispose(); return; }

                int gx = grid.getX();
                int gy = grid.getY();

                for (int r = 0; r < btns.length; r++) {
                    Rectangle b = btns[r][0].getBounds();
                    String txt = String.valueOf((char) ('A' + r));

                    int y = gy + b.y + (b.height + fm.getAscent() - fm.getDescent()) / 2; // centered to row
                    int x = gx - 6 - fm.stringWidth(txt); // draw left of grid inside LEFT strip
                    x = Math.max(2, x);                  // stay visible if narrow
                    g2.drawString(txt, x, y);
                }
                g2.dispose();
            }
        };
        leftPainter.setOpaque(false);
        center.add(leftPainter);

        // Ensure headers are on top
        center.setComponentZOrder(topPainter, 0);
        center.setComponentZOrder(leftPainter, 1);
        center.setComponentZOrder(grid, 2);

        add(center, BorderLayout.CENTER);

        // Keep everything sized/positioned—reserve header strips explicitly
        center.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) {
                layoutCenter(center, grid, topPainter, leftPainter);
            }
        });
        layoutCenter(center, grid, topPainter, leftPainter);
    }

    private static void layoutCenter(JPanel center, JComponent grid,
                                     JComponent topPainter, JComponent leftPainter) {
        int w = center.getWidth(), h = center.getHeight();
        if (w <= 0 || h <= 0) return;

        // Reserve top/left strips for labels; grid gets the rest
        int gx = LEFT_STRIP;
        int gy = TOP_STRIP;
        int gw = Math.max(1, w - LEFT_STRIP);
        int gh = Math.max(1, h - TOP_STRIP);

        grid.setBounds(gx, gy, gw, gh);

        // Painters cover the whole center to read grid/button bounds and draw in strips
        topPainter.setBounds(0, 0, w, h);
        leftPainter.setBounds(0, 0, w, h);

        // Ensure buttons are laid out before painters draw
        grid.validate();
        center.repaint();
    }

    public JButton[][] getButtons() { return grid.getButtons(); }
}
