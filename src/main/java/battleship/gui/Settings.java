package battleship.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Settings extends JPanel {
    
    public Settings(ActionListener onBackToMenu) {
        setLayout(new BorderLayout());
        add(new JLabel("Settings Screen"), BorderLayout.CENTER);

        JButton back = new JButton("Back to Menu");
        back.addActionListener(onBackToMenu);

        JPanel south = new JPanel();
        south.add(back);
        add(south, BorderLayout.SOUTH);
    }
}