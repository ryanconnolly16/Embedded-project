package battleship.gui_settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ScreenSizeUtils {
    private ScreenSizeUtils() {}

    // Make the frame go exclusive fullscreen
    public static ActionListener fullscreen(JFrame frame) {
        return e -> {
            GraphicsDevice gd = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice();

            if (frame.isDisplayable()) frame.dispose();  // needed before toggling decoration
            frame.setUndecorated(true);
            frame.setResizable(false);
            gd.setFullScreenWindow(frame);
            if (!frame.isVisible()) frame.setVisible(true);
        };
    }

    // Return to normal windowed mode with a target size/center
    public static ActionListener windowed(JFrame frame, Dimension size) {
        return e -> {
            GraphicsDevice gd = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice();

            gd.setFullScreenWindow(null);

            if (frame.isDisplayable()) frame.dispose();
            frame.setUndecorated(false);
            frame.setResizable(true);
            frame.setExtendedState(JFrame.NORMAL);

            if (size != null) frame.setPreferredSize(size);
            frame.pack();                   

            centerOnPrimary(frame);
            frame.setVisible(true);

            frame.getContentPane().revalidate();
            frame.repaint();
        };
    }

    // Bind ESC to trigger the provided windowed action
    public static void bindEscapeToExit(JFrame frame, ActionListener windowedAction) {
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ESCAPE"), "exitFS");
        frame.getRootPane().getActionMap().put("exitFS", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                windowedAction.actionPerformed(null);
            }
        });
    }

    private static void centerOnPrimary(JFrame frame) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width  - frame.getWidth())  / 2;
        int y = (screen.height - frame.getHeight()) / 2;
        frame.setLocation(Math.max(0, x), Math.max(0, y));
    }
}
