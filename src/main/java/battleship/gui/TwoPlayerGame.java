package battleship.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TwoPlayerGame extends JPanel {
    
    public TwoPlayerGame(ActionListener onBackToMenu) {
        setLayout(new BorderLayout());
        add(new JLabel("Two Player Game Screen"), BorderLayout.CENTER);

        JButton back = new JButton("Back to Menu");
        back.addActionListener(onBackToMenu);

        JPanel south = new JPanel();
        south.add(back);
        add(south, BorderLayout.SOUTH);
    }
}