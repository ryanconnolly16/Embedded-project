package battleship.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OnePlayerGame extends JPanel {
    
    public OnePlayerGame(ActionListener onBackToMenu) {
        setLayout(new BorderLayout());
        add(new JLabel("One Player Game Screen"), BorderLayout.CENTER);

        JButton back = new JButton("Back to Menu");
        back.addActionListener(onBackToMenu);

        JPanel south = new JPanel();
        south.add(back);
        add(south, BorderLayout.SOUTH);
    }
}
