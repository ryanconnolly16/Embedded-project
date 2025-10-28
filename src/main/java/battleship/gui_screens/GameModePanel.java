package battleship.gui_screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class GameModePanel extends JPanel {

    public GameModePanel(String title, ActionListener onBack) {
        setLayout(new BorderLayout());

        // top bar
        var top = new JPanel(new BorderLayout());
        top.add(new JLabel(title, JLabel.CENTER), BorderLayout.CENTER);
        var back = new JButton("Back");
        back.addActionListener(onBack);
        top.add(back, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);

        // center
        add(createBoardCenter(title), BorderLayout.CENTER);
    }

    /** Your subclass provides the main content. */
    protected abstract JComponent createBoardCenter(String title);
}
