package battleship.gui_setup;

import battleship.gui_screens.FlatButton;   // <-- NOTE: correct import
import javax.swing.*;
import java.awt.*;

/** Simple setup screen for one-player mode with compact flat buttons. */
public class Setup extends JPanel {
    private static final Color BUTTON_BASE = new Color(30, 80, 140);
    private static final Color PANEL_BG    = new Color(42, 100, 165); // slightly lighter

    private final FlatButton preset   = new FlatButton("Use Preset");
    private final FlatButton loadBtn  = new FlatButton("Load Save");
    private final FlatButton newBtn   = new FlatButton("New Game");
    private final FlatButton start    = new FlatButton("Start Game");
    private final FlatButton back     = new FlatButton("Back");
    private final JLabel     status   = new JLabel("Choose a setup option.", JLabel.CENTER);

    // local gating state (view-side)
    private boolean presetDone = false;
    private boolean sourceChosen = false;

    public Setup(SetupActions actions) {
        // panel background (slightly lighter than buttons)
        setOpaque(true);
        setBackground(PANEL_BG);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Title + status
        JPanel north = new JPanel(new GridLayout(0, 1));
        north.setOpaque(true);
        north.setBackground(PANEL_BG);
        JLabel title = new JLabel("Setup (One Player)", JLabel.CENTER);
        title.setForeground(new Color(245,245,245));
        status.setForeground(new Color(230,230,230));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        north.add(title);
        north.add(status);
        add(north, BorderLayout.NORTH);

        // Center: vertical BoxLayout so buttons stay small
        JPanel center = new JPanel();
        center.setOpaque(true);
        center.setBackground(PANEL_BG);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // match buttons’ base color (optional — default already 30,80,140)
        for (FlatButton b : new FlatButton[]{preset, loadBtn, newBtn, start, back}) {
            b.setBaseColor(BUTTON_BASE);
        }

        // preset
        addSmall(center, preset);
        preset.addActionListener(e -> {
            if (!presetDone) {
                presetDone = true;
                preset.setEnabled(false);
                preset.setText("Preset applied");
                status.setText("Preset placed. Now choose Load or New.");
                actions.applyPreset();
                updateStartEnabled();
            }
        });

        // load save
        addSmall(center, loadBtn);
        loadBtn.addActionListener(e -> {
            sourceChosen = true;
            status.setText("Save selected. You can Start when preset is applied.");
            actions.loadSave();
            updateStartEnabled();
        });

        // new game
        addSmall(center, newBtn);
        newBtn.addActionListener(e -> {
            sourceChosen = true;
            status.setText("New game selected. You can Start when preset is applied.");
            actions.newGame();
            updateStartEnabled();
        });

        // start (gated)
        addSmall(center, start);
        start.setEnabled(false);  // gate until both conditions met
        start.addActionListener(e -> {
            status.setText("Starting game…");
            actions.start();
        });

        add(center, BorderLayout.CENTER);

        // South: back
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(true);
        south.setBackground(PANEL_BG);
        fixSmall(back);
        back.addActionListener(e -> actions.back());
        south.add(back);
        add(south, BorderLayout.SOUTH);
    }

    private void updateStartEnabled() {
        start.setEnabled(presetDone && sourceChosen);
    }

    private static void addSmall(JPanel parent, JComponent c) {
        fixSmall(c);
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(c);
        parent.add(Box.createVerticalStrut(10));
    }
    private static void fixSmall(JComponent c) {
        Dimension d = (c.getPreferredSize() != null) ? c.getPreferredSize() : new Dimension(160, 36);
        c.setMinimumSize(d); c.setPreferredSize(d); c.setMaximumSize(d);
    }

    // Optional helpers if controller wants to influence UI text/buttons later
    public void setStatus(String text) { status.setText(text); }
    public void enablePresetButton() { preset.setEnabled(true); preset.setText("Use Preset"); presetDone = false; updateStartEnabled(); }
    public void disablePresetButton() { preset.setEnabled(false); preset.setText("Preset applied"); presetDone = true; updateStartEnabled(); }
}
