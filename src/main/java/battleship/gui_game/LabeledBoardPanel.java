// battleship/gui_game/LabeledBoardPanel.java
package battleship.gui_game;

import javax.swing.*;
import java.awt.*;

public class LabeledBoardPanel extends JPanel {
    private final SquareGridPanel grid;

    // Small offsets so labels fit; keep the grid near the top/left
    private static final int TOP_STRIP  = 24;   // space reserved above grid
    private static final int LEFT_STRIP = 28;   // space reserved left of grid

    public LabeledBoardPanel(int rows, int cols, int gap, String title) {
        setLayout(new BorderLayout());
        setOpaque(false);

        if (title != null && !title.isEmpty()) {
            JLabel t = new JLabel(title, JLabel.CENTER);
            t.setFont(t.getFont().deriveFont(Font.BOLD));
            add(t, BorderLayout.NORTH);
        }

        // Center uses absolute layout so we can paint labels precisely
        JPanel center = new JPanel(null);
        center.setOpaque(false);

        grid = new SquareGridPanel(rows, cols, gap);
        center.add(grid);

        // ---- TOP painter: A..J above the first row ----
        JComponent topPainter = new JComponent() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(new Color(30, 30, 30));
                FontMetrics fm = g2.getFontMetrics(getFont());

                JButton[][] btns = grid.getButtons();
                if (btns.length == 0 || btns[0].length == 0) { g2.dispose(); return; }

                int gx = grid.getX(), gy = grid.getY();
                Rectangle first = btns[0][0].getBounds();

                // Baseline just ABOVE the first row so letters don't overlap cells
                int baseline = gy + first.y - Math.max(2, fm.getDescent());

                for (int c = 0; c < btns[0].length; c++) {
                    Rectangle r = btns[0][c].getBounds();
                    int cx = gx + r.x + r.width / 2;
                    String txt = String.valueOf((char) ('A' + c));
                    int x = cx - fm.stringWidth(txt) / 2;
                    g2.drawString(txt, x, baseline);
                }
                g2.dispose();
            }
        };
        topPainter.setOpaque(false);
        center.add(topPainter);

        // ---- LEFT painter: 1..10 centered next to each row ----
        JComponent leftPainter = new JComponent() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(new Color(30, 30, 30));
                FontMetrics fm = g2.getFontMetrics(getFont());

                JButton[][] btns = grid.getButtons();
                if (btns.length == 0 || btns[0].length == 0) { g2.dispose(); return; }

                int gx = grid.getX(), gy = grid.getY();

                for (int r = 0; r < btns.length; r++) {
                    Rectangle b = btns[r][0].getBounds();
                    String txt = String.valueOf(r + 1);
                    int y = gy + b.y + (b.height + fm.getAscent() - fm.getDescent()) / 2;
                    int x = gx - 6 - fm.stringWidth(txt);      // a few px left of the grid
                    if (x < 2) x = 2;
                    g2.drawString(txt, x, y);
                }
                g2.dispose();
            }
        };
        leftPainter.setOpaque(false);
        center.add(leftPainter);

        // Ensure painters are above the grid for clean overlap
        center.setComponentZOrder(topPainter, 0);
        center.setComponentZOrder(leftPainter, 1);
        center.setComponentZOrder(grid, 2);

        add(center, BorderLayout.CENTER);

        // Keep everything positioned on resize
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

        int gx = LEFT_STRIP, gy = TOP_STRIP;
        int gw = Math.max(1, w - LEFT_STRIP);
        int gh = Math.max(1, h - TOP_STRIP);

        grid.setBounds(gx, gy, gw, gh);

        // Painters cover the whole center so they can read button bounds and draw anywhere
        topPainter.setBounds(0, 0, w, h);
        leftPainter.setBounds(0, 0, w, h);

        grid.validate();
        center.revalidate();
        center.repaint();
    }

    public JButton[][] getButtons() {
        return grid.getButtons();
    }
}
