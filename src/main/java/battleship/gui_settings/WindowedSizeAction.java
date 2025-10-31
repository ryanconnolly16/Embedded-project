package battleship.gui_settings;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

// Sets frame to windowed
public class WindowedSizeAction implements ActionListener {
    private final JFrame frame;

    public WindowedSizeAction(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(null); 

        if (frame.isDisplayable()) frame.dispose();
        frame.setUndecorated(false);   
        frame.setResizable(true);      

        var kit = java.awt.Toolkit.getDefaultToolkit();
        var screen = kit.getScreenSize();
        int w = screen.width / 2, h = screen.height / 2;
        frame.setSize(w, h);  
        frame.setLocation(w / 2, h / 2);
        frame.setVisible(true);  

    }
}
