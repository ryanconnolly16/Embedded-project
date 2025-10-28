package battleship.gui_settings;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class FullScreenSizeAction implements ActionListener {
    private final JFrame frame;

    public FullScreenSizeAction(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (frame.isDisplayable()) {
            frame.dispose();
        }

        frame.setUndecorated(true); 
        frame.setResizable(false);   

        gd.setFullScreenWindow(frame); 

        if (!frame.isVisible()) frame.setVisible(true);
    }
}
