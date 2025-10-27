package battleship.gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class ActionListenerCardSwitch implements ActionListener {
    private final CardLayout layout;
    private final JPanel container;
    private final String target;

    public ActionListenerCardSwitch(CardLayout layout, JPanel container, String target) {
        this.layout = layout;
        this.container = container;
        this.target = target;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        layout.show(container, target);
    }
}
