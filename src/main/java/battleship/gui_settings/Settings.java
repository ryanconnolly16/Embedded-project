package battleship.gui_settings;

import battleship.gui_screens.FlatButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Settings extends JPanel {
    private static final Color BUTTON_BASE = new Color(30, 80, 140);
    private static final Color PANEL_BG    = new Color(42, 100, 165); // slightly lighter

    public Settings(ActionListener onBack,
                    ActionListener onWindowed,
                    ActionListener onFullscreen) {
        setLayout(new BorderLayout(10, 10));
        setOpaque(true);
        setBackground(PANEL_BG);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Settings", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setForeground(new Color(245,245,245));
        add(title, BorderLayout.NORTH);

        // Center: vertical BoxLayout with small buttons
        JPanel center = new JPanel();
        center.setOpaque(true);
        center.setBackground(PANEL_BG);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        FlatButton windowedBtn   = new FlatButton("Windowed");
        FlatButton fullscreenBtn = new FlatButton("Fullscreen");
        windowedBtn.setBaseColor(BUTTON_BASE);
        fullscreenBtn.setBaseColor(BUTTON_BASE);

        addSmall(center, windowedBtn);
        addSmall(center, fullscreenBtn);

        if (onWindowed != null)   windowedBtn.addActionListener(onWindowed);
        if (onFullscreen != null) fullscreenBtn.addActionListener(onFullscreen);

        add(center, BorderLayout.CENTER);

        // South: back
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(true);
        south.setBackground(PANEL_BG);
        FlatButton back = new FlatButton("Back");
        back.setBaseColor(BUTTON_BASE);
        fixSmall(back);
        if (onBack != null) back.addActionListener(onBack);
        south.add(back);
        add(south, BorderLayout.SOUTH);
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
}