package battleship.gui_game;

import javax.swing.*;
import java.awt.*;

public class LabeledBoardPanel extends JPanel {
    private final SquareGridPanel grid;

    public LabeledBoardPanel(int rows, int cols, int gap, String title) {
        setLayout(new BorderLayout(6, 6));
        setOpaque(false);

        if (title != null && !title.isEmpty()) {
            JLabel t = new JLabel(title, JLabel.CENTER);
            t.setFont(t.getFont().deriveFont(Font.BOLD));
            add(t, BorderLayout.NORTH);
        }

        JPanel center = new JPanel(new BorderLayout(4, 4));
        center.setOpaque(false);

        // Top header: 1..cols (with a leading blank corner to line up with left letters)
        JPanel top = new JPanel(new GridLayout(1, cols + 1, 2, 2));
        top.setOpaque(false);
        top.add(new JLabel("")); // top-left corner blank
        for (int c = 1; c <= cols; c++) {
            JLabel lab = new JLabel(String.valueOf(c), JLabel.CENTER);
            top.add(lab);
        }
        center.add(top, BorderLayout.NORTH);

        // Left header: A..rows
        JPanel left = new JPanel(new GridLayout(rows, 1, 2, 2));
        left.setOpaque(false);
        for (int r = 0; r < rows; r++) {
            JLabel lab = new JLabel(String.valueOf((char) ('A' + r)), JLabel.CENTER);
            left.add(lab);
        }
        center.add(left, BorderLayout.WEST);

        // Square grid in the middle
        grid = new SquareGridPanel(rows, cols, gap);
        center.add(grid, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    public JButton[][] getButtons() { return grid.getButtons(); }
}
