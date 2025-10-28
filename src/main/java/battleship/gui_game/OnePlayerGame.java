package battleship.gui_game;

import battleship.domain.Board;
import javax.swing.*;
import java.awt.*;

public class OnePlayerGame extends JPanel {

    private final JButton[][] shipsGrid = new JButton[10][10];
    private final JButton[][] shotsGrid = new JButton[10][10];

    private OnePlayerActions actions;
    private Board myBoard;

    public OnePlayerGame() {
        setLayout(new BorderLayout());

        add(new JLabel("One Player", JLabel.CENTER), BorderLayout.NORTH);

        JPanel boards = new JPanel(new GridLayout(1, 2, 10, 10));

        // Left: my ships
        JPanel shipsBoard = new JPanel(new BorderLayout());
        shipsBoard.add(new JLabel("Your ships", JLabel.CENTER), BorderLayout.NORTH);
        JPanel shipsGridPanel = new JPanel(new GridLayout(10, 10, 2, 2));
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                JButton b = new JButton();
                shipsGrid[r][c] = b;
                shipsGridPanel.add(b);
            }
        }
        shipsBoard.add(shipsGridPanel, BorderLayout.CENTER);

        // Right: my shots
        JPanel shotsBoard = new JPanel(new BorderLayout());
        shotsBoard.add(new JLabel("Your shots", JLabel.CENTER), BorderLayout.NORTH);
        JPanel shotsGridPanel = new JPanel(new GridLayout(10, 10, 2, 2));
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                final int rr = r, cc = c;
                JButton b = new JButton();
                shotsGrid[rr][cc] = b;
                // action is bound via setActions()
                b.addActionListener(e -> { if (actions != null) actions.fireShot(rr, cc); });
                shotsGridPanel.add(b);
            }
        }
        shotsBoard.add(shotsGridPanel, BorderLayout.CENTER);

        boards.add(shipsBoard);
        boards.add(shotsBoard);
        add(boards, BorderLayout.CENTER);

        JButton quitSave    = new JButton("Quit & Save");
        JButton quitDiscard = new JButton("Quit & Discard");
        quitSave.addActionListener(e -> { if (actions != null) actions.quitSave(); });
        quitDiscard.addActionListener(e -> { if (actions != null) actions.quitDiscard(); });

        JPanel south = new JPanel();
        south.add(quitSave);
        south.add(quitDiscard);
        add(south, BorderLayout.SOUTH);
    }

    public void setActions(OnePlayerActions actions) {
        this.actions = actions;
    }

    public void setModel(Board board) {
        this.myBoard = board;
    }

    public void refresh() {
        if (myBoard == null) return;
        BoardView.refreshAll(shipsGrid, shotsGrid, myBoard);
        revalidate();
        repaint();
    }

    public void setShotsEnabled(boolean enabled) {
        for (var row : shotsGrid) for (var b : row) b.setEnabled(enabled);
    }
}
