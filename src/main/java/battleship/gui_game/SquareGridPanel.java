package battleship.gui_game;

import javax.swing.*;
import java.awt.*;
//centers grids of buttons with fixed gaps
public class SquareGridPanel extends JPanel {
    private final int rows, cols, gap;
    private final JButton[][] buttons;

    private int lastCell = 32;
    private int lastStartX = 0, lastStartY = 0;

    public SquareGridPanel(int rows, int cols, int gap) {
        this.rows = rows;
        this.cols = cols;
        this.gap = gap;
        this.buttons = new JButton[rows][cols];

        setLayout(null);
        setOpaque(false);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton b = new JButton();
                b.setMargin(new Insets(0, 0, 0, 0));
                buttons[r][c] = b;
                add(b);
            }
        }
    }

    public JButton[][] getButtons() { return buttons; }
    public int getCellSize() { return lastCell; }
    public int getStartX() { return lastStartX; }
    public int getStartY() { return lastStartY; }
    public int getGap()    { return gap; }
    public int getRows()   { return rows; }
    public int getCols()   { return cols; }

    @Override
    public Dimension getPreferredSize() {
        int cell = 32;
        int w = cols * cell + (cols - 1) * gap;
        int h = rows * cell + (rows - 1) * gap;
        return new Dimension(w, h);
    }

    @Override
    public void doLayout() {
        Insets in = getInsets();
        int availW = getWidth() - in.left - in.right;
        int availH = getHeight() - in.top - in.bottom;

        int cellW = (availW - (cols - 1) * gap) / cols;
        int cellH = (availH - (rows - 1) * gap) / rows;
        int cell  = Math.min(cellW, cellH);

        int gridW = cols * cell + (cols - 1) * gap;
        int gridH = rows * cell + (rows - 1) * gap;
        int startX = in.left + (availW - gridW) / 2;
        int startY = in.top  + (availH - gridH) / 2;

        // remember metrics for headers
        lastCell   = cell;
        lastStartX = startX;
        lastStartY = startY;

        for (int r = 0; r < rows; r++) {
            int y = startY + r * (cell + gap);
            for (int c = 0; c < cols; c++) {
                int x = startX + c * (cell + gap);
                buttons[r][c].setBounds(x, y, cell, cell);
            }
        }
    }
}
