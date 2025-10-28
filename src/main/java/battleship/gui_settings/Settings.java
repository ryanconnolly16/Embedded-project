package battleship.gui_settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class Settings extends JPanel {
    public Settings(ActionListener onBack,
                    ActionListener onWindowed,
                    ActionListener onFullscreen) {
        setLayout(new BorderLayout());
        add(new JLabel("Settings", JLabel.CENTER), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton windowedBtn   = new JButton("Windowed");
        JButton fullscreenBtn = new JButton("Fullscreen");

        if (onWindowed != null)   windowedBtn.addActionListener(onWindowed);
        if (onFullscreen != null) fullscreenBtn.addActionListener(onFullscreen);

        center.add(windowedBtn);
        center.add(fullscreenBtn);
        add(center, BorderLayout.CENTER);

        JButton back = new JButton("Back");
        if (onBack != null) back.addActionListener(onBack);
        JPanel south = new JPanel();
        south.add(back);
        add(south, BorderLayout.SOUTH);
    }
}

