package battleship.gui_setup;

import javax.swing.*;
import java.awt.*;

public class Setup extends JPanel {

    static JButton preset;
    public Setup(SetupActions actions) {
        setLayout(new BorderLayout());
        add(new JLabel("Setup (One Player)", JLabel.CENTER), BorderLayout.NORTH);

        var center = new JPanel(new GridLayout(0, 1, 10, 10));

        preset = new JButton("Use Preset");
        preset.addActionListener(e -> actions.applyPreset());
        center.add(preset);

        
        
        JButton start = new JButton("Start Game");
        start.addActionListener(e -> actions.start());
        center.add(start);

        add(center, BorderLayout.CENTER);

        var south = new JPanel();
        JButton back = new JButton("Back");
        back.addActionListener(e -> actions.back());
        south.add(back);
        add(south, BorderLayout.SOUTH);
    }
}
