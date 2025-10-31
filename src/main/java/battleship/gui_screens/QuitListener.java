package battleship.gui_screens;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Action listener for quiting the game
public class QuitListener implements ActionListener {
    private final JFrame owner;
    public QuitListener(JFrame owner) { this.owner = owner; }
    @Override public void actionPerformed(ActionEvent e) {
        owner.dispose();
        System.exit(0);
    }
}
