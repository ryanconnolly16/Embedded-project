package battleship.gui_screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class Menu extends JPanel {
    private final Image backgroundImage;

    public Menu(ActionListener toSetup,
                ActionListener toSettings,
                ActionListener quit) {
        // Load from classpath, e.g. src/main/resources/Battleship_background.png
        backgroundImage = loadImage("/Battleship_background.png");

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel column = new JPanel(new GridLayout(0, 1, 10, 10));
        column.setOpaque(false);

        FlatButton one = new FlatButton("One Player");
        if (toSetup != null) one.addActionListener(toSetup);
        column.add(one);

        FlatButton set = new FlatButton("Settings");
        if (toSettings != null) set.addActionListener(toSettings);
        column.add(set);

        FlatButton q = new FlatButton("Quit");
        q.setBaseColor(new Color(170, 40, 40));
        if (quit != null) q.addActionListener(quit);
        column.add(q);

        JPanel padded = new JPanel(new BorderLayout());
        padded.setOpaque(false);
        padded.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        padded.add(column, BorderLayout.CENTER);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.anchor = GridBagConstraints.CENTER;
        add(padded, gc);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage == null) {
            g.setColor(new Color(15, 25, 40));
            g.fillRect(0, 0, getWidth(), getHeight());
            return;
        }
        int pw = getWidth(), ph = getHeight();
        int iw = backgroundImage.getWidth(null);
        int ih = backgroundImage.getHeight(null);
        if (iw <= 0 || ih <= 0) return;

        double sx = pw / (double) iw, sy = ph / (double) ih;
        double scale;
        if (pw >= iw && ph >= ih) {
            int intScale = Math.max((int)Math.ceil(sx), (int)Math.ceil(sy)); // integer cover on upscale
            scale = Math.max(1, intScale);
        } else {
            scale = Math.max(sx, sy); // fractional cover on downscale
        }

        int w = (int)Math.round(iw * scale);
        int h = (int)Math.round(ih * scale);
        int x = (pw - w) / 2, y = (ph - h) / 2;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.drawImage(backgroundImage, x, y, w, h, null);
        g2.dispose();
    }

    private static Image loadImage(String resourcePath) {
        URL url = Menu.class.getResource(resourcePath);
        return (url == null) ? null : new ImageIcon(url).getImage();
    }
}
