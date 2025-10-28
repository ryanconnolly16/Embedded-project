package battleship.gui_screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class Menu extends JPanel {
    public Menu(ActionListener toSetup,
                ActionListener toSettings,
                ActionListener quit) {
        setLayout(new GridLayout(0, 1, 10, 10)); 

        JButton one = new JButton("One Player");
        one.addActionListener(toSetup);   
        add(one);

        JButton set = new JButton("Settings");
        set.addActionListener(toSettings);
        add(set);

        JButton q = new JButton("Quit");
        q.addActionListener(quit);
        add(q);
    }
}
