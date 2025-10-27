package battleship.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Menu extends JPanel {
    
    public Menu(ActionListener onOnePlay, ActionListener onTwoPlay, ActionListener onSettings, ActionListener onQuit) {

        setLayout(new GridLayout(4, 1, 10, 10));

        JButton buttonOnePlay  = new JButton("One player game");
        JButton buttonTwoPlay  = new JButton("Two player game");
        JButton buttonSettings = new JButton("Settings");
        JButton buttonQuit     = new JButton("Quit");

        buttonOnePlay.addActionListener(onOnePlay);
        buttonTwoPlay.addActionListener(onTwoPlay);
        buttonSettings.addActionListener(onSettings);
        buttonQuit.addActionListener(onQuit);

        add(buttonOnePlay);
        add(buttonTwoPlay);
        add(buttonSettings);
        add(buttonQuit);
    }
}
