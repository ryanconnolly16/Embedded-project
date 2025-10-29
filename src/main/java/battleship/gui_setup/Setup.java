package battleship.gui_setup;

import javax.swing.*;
import java.awt.*;

/** Simple setup screen for one-player mode. */
public class Setup extends JPanel {

    private final JButton preset = new JButton("Use Preset");
    private final JButton start  = new JButton("Start Game");
    private final JButton back   = new JButton("Back");
    private final JLabel  status = new JLabel("Choose a setup option.", JLabel.CENTER);

    public Setup(SetupActions actions) {
        setLayout(new BorderLayout(10, 10));

        // Title + status
        JPanel north = new JPanel(new GridLayout(0, 1));
        JLabel title = new JLabel("Setup (One Player)", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        north.add(title);
        north.add(status);
        add(north, BorderLayout.NORTH);

        // Center buttons
        JPanel center = new JPanel(new GridLayout(0, 1, 10, 10));

        preset.addActionListener(e -> {
            // Prevent double-fire on EDT
            preset.setEnabled(false);
            preset.setText("Preset applied");
            status.setText("Preset placed. You can start the game.");
            actions.applyPreset();
        });
        center.add(preset);

        start.addActionListener(e -> {
            status.setText("Starting game…");
            actions.start();
        });
        center.add(start);

        add(center, BorderLayout.CENTER);

        // South: back
        JPanel south = new JPanel();
        back.addActionListener(e -> actions.back());
        south.add(back);
        add(south, BorderLayout.SOUTH);

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    /* ---------- Optional helpers for controller ---------- */

    /** Controller can call this to show a short message under the title. */
    public void setStatus(String text) {
        status.setText(text);
    }

    /** If you ever want to re-allow presetting (not typical), call this. */
    public void enablePresetButton() {
        preset.setEnabled(true);
        preset.setText("Use Preset");
    }

    /** Disable preset from controller after apply, if you centralize logic there. */
    public void disablePresetButton() {
        preset.setEnabled(false);
        preset.setText("Preset applied");
    }
}
