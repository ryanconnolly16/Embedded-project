// battleship/gui_setup/Setup.java
package battleship.gui_setup;

import battleship.gui_screens.FlatButton;
import javax.swing.*;
import java.awt.*;

/** One-player setup: only New Game, Load Game, Back. */
public class Setup extends JPanel {
    private static final Color BUTTON_BASE = new Color(30, 80, 140);
    private static final Color PANEL_BG    = new Color(42, 100, 165);

    private final FlatButton newBtn  = new FlatButton("New Game");
    private final FlatButton loadBtn = new FlatButton("Load Game");
    private final FlatButton back    = new FlatButton("Back");
    private final JLabel     status  = new JLabel("Choose: New Game or Load Game.", JLabel.CENTER);

    public Setup(SetupActions actions) {
        setOpaque(true);
        setBackground(PANEL_BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // ---------- Title + status ----------
        JPanel north = new JPanel(new GridLayout(0, 1));
        north.setOpaque(true);
        north.setBackground(PANEL_BG);

        JLabel title = new JLabel("Setup (One Player)", JLabel.CENTER);
        title.setForeground(new Color(245,245,245));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        status.setForeground(new Color(230,230,230));
        status.setFont(status.getFont().deriveFont(Font.PLAIN, 14f));

        north.add(title);
        north.add(status);
        add(north, BorderLayout.NORTH);

        // ---------- Center: vertical buttons ----------
        JPanel center = new JPanel();
        center.setOpaque(true);
        center.setBackground(PANEL_BG);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        for (FlatButton b : new FlatButton[]{ newBtn, loadBtn }) {
            b.setBaseColor(BUTTON_BASE);
            addSmall(center, b);
        }
        add(center, BorderLayout.CENTER);

        // New Game: create new board, apply preset, start immediately
        newBtn.addActionListener(e -> {
            disableChoiceButtons();
            status.setText("Creating new game, applying preset, starting…");
            if (actions != null) {
                actions.newGame();
                actions.applyPreset();
                actions.start();
            }
        });

        // Load Game: load save, then start immediately
        loadBtn.addActionListener(e -> {
            disableChoiceButtons();
            status.setText("Loading save and starting…");
            if (actions != null) {
                actions.loadSave();
                actions.start();
            }
        });

        // ---------- South: Back ----------
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(true);
        south.setBackground(PANEL_BG);

        back.setBaseColor(BUTTON_BASE);
        fixSmall(back);
        back.addActionListener(e -> { if (actions != null) actions.back(); });

        south.add(back);
        add(south, BorderLayout.SOUTH);
    }

    private void disableChoiceButtons() {
        newBtn.setEnabled(false);
        loadBtn.setEnabled(false);
        back.setEnabled(false);
    }

    private static void addSmall(JPanel parent, JComponent c) {
        fixSmall(c);
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(c);
        parent.add(Box.createVerticalStrut(10));
    }

    private static void fixSmall(JComponent c) {
        Dimension d = (c.getPreferredSize() != null)
                ? c.getPreferredSize()
                : new Dimension(160, 36);
        c.setMinimumSize(d);
        c.setPreferredSize(d);
        c.setMaximumSize(d);
    }

    // Optional helper the controller can use
    public void setStatus(String text) { status.setText(text); }
}
