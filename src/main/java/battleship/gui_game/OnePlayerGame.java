// battleship/gui_game/OnePlayerGame.java
package battleship.gui_game;

import battleship.domain.Board;

import javax.swing.*;
import java.awt.*;

public class OnePlayerGame extends JPanel {
    private static final Color PANEL_BG = new Color(42, 100, 165); // page background

    private JButton[][] shipsGrid;
    private JButton[][] shotsGrid;

    private OnePlayerActions actions;
    private Board myBoard;

    private final GameSideBarPanel sidebar = new GameSideBarPanel();

    public OnePlayerGame() {
        setOpaque(true);
        setBackground(PANEL_BG);
        setLayout(new BorderLayout());

        // ---------- Title bar ----------
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(true);
        north.setBackground(PANEL_BG);
        JLabel title = new JLabel("You vs AI", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(new Color(245, 245, 245));
        north.add(title, BorderLayout.NORTH);
        add(north, BorderLayout.NORTH);

        // ---------- CENTER: boards + (centered) sidebar ----------
        JPanel centerRow = new JPanel(new GridBagLayout());
        centerRow.setOpaque(true);
        centerRow.setBackground(PANEL_BG);

        // Boards container (left)
        JPanel boards = new JPanel(new GridLayout(1, 2, 16, 10));
        boards.setOpaque(true);
        boards.setBackground(PANEL_BG);

        // Ships board
        LabeledBoardPanel shipsPanel = new LabeledBoardPanel(10, 10, 2, "Your ships");
        shipsPanel.setOpaque(false);
        shipsGrid = shipsPanel.getButtons();
        boards.add(shipsPanel);

        // Shots board
        LabeledBoardPanel shotsPanel = new LabeledBoardPanel(10, 10, 2, "Your shots");
        shotsPanel.setOpaque(false);
        shotsGrid = shotsPanel.getButtons();
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                final int rr = r, cc = c;
                shotsGrid[rr][cc].addActionListener(e -> {
                    if (actions != null) actions.fireShot(rr, cc);
                });
            }
        }
        boards.add(shotsPanel);

        // Boards fill remaining space
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 12);
        centerRow.add(boards, gbc);

        // ---- Sidebar wrapper: vertically centers the sidebar next to the boards ----
        JPanel sideWrap = new JPanel(new GridBagLayout());
        sideWrap.setOpaque(true);
        sideWrap.setBackground(PANEL_BG);

        GridBagConstraints w = new GridBagConstraints();
        w.gridx = 0; w.fill = GridBagConstraints.HORIZONTAL; w.weightx = 1.0;

        // top glue
        w.gridy = 0; w.weighty = 1.0;
        sideWrap.add(Box.createVerticalGlue(), w);

        // the sidebar itself (keeps its preferred width/height)
        w.gridy = 1; w.weighty = 0.0; w.fill = GridBagConstraints.NONE; w.anchor = GridBagConstraints.CENTER;
        sideWrap.add(sidebar, w);

        // bottom glue
        w.gridy = 2; w.weighty = 1.0; w.fill = GridBagConstraints.HORIZONTAL;
        sideWrap.add(Box.createVerticalGlue(), w);

        // mount wrapper on the right column; it stretches, but the sidebar inside stays centered
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;  // wrapper grows; sidebar remains centered within
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        centerRow.add(sideWrap, gbc);

        add(centerRow, BorderLayout.CENTER);

        // ---------- Bottom bar ----------
        JPanel south = new JPanel();
        south.setOpaque(true);
        south.setBackground(PANEL_BG);
        JButton quitSave    = new JButton("Quit & Save");
        JButton quitDiscard = new JButton("Quit & Discard");
        quitSave.addActionListener(e -> { if (actions != null) actions.quitSave(); });
        quitDiscard.addActionListener(e -> { if (actions != null) actions.quitDiscard(); });
        south.add(quitSave);
        south.add(quitDiscard);
        add(south, BorderLayout.SOUTH);

        // Optional: crisp cell styling/borders/symbol sizing
        BoardView.styleGrids(shipsGrid, shotsGrid);
    }

    // ---------- Controller API ----------
    public void setActions(OnePlayerActions actions) { this.actions = actions; }

    public void setModel(Board board) { this.myBoard = board; }

    public void refresh() {
        if (myBoard == null) return;
        BoardView.refreshAll(shipsGrid, shotsGrid, myBoard);
        revalidate();
        repaint();
    }

    public void setShotsEnabled(boolean enabled) {
        for (var row : shotsGrid) for (var b : row) b.setEnabled(enabled);
    }

    public void setStatusText(String txt) { sidebar.setStatus(txt); }

    public void appendLog(String line) { sidebar.appendLog(line); }

    public JButton[][] getShipsButtons() { return shipsGrid; }
    public JButton[][] getShotsButtons() { return shotsGrid; }
}
