package battleship.gui_screens;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class Menu extends JPanel {

    // --- Flat rounded button (no gradient) ---
    private static class FlatButton extends JButton {
        FlatButton(String text) {
            super(text);
            setFocusPainted(false);
            setBorder(new LineBorder(new Color(0, 0, 0, 60), 1, true));
            setForeground(new Color(245, 245, 245));
            setBackground(new Color(30, 80, 140)); // solid fill
            setOpaque(false);             // we'll paint the background ourselves
            setContentAreaFilled(false);  // prevent LAF gradient
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(getFont().deriveFont(Font.BOLD, 14f));
            setMargin(new Insets(6, 16, 6, 16));
            setPreferredSize(new Dimension(160, 36)); // smaller button
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // background (hover/press states)
            Color bg = getBackground();
            if (!isEnabled()) {
                bg = bg.darker();
            } else if (getModel().isPressed()) {
                bg = bg.darker();
            } else if (getModel().isRollover()) {
                bg = bg.brighter();
            }

            int arc = 14;
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

            // optional subtle inner highlight
            g2.setColor(new Color(255, 255, 255, 25));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);

            g2.dispose();

            // draw text/icon
            super.paintComponent(g);
        }
    }

    private final Image backgroundImage;

    public Menu(ActionListener toSetup,
                ActionListener toSettings,
                ActionListener quit) {
        // Load background image from resources (e.g., src/main/resources/images/menu_bg.jpg)
        backgroundImage = loadImage("/Battleship_background.png");

        // Transparent content over bg
        setOpaque(false);
        setLayout(new GridBagLayout()); // easy centering

        // Button column
        JPanel column = new JPanel();
        column.setOpaque(false);
        column.setLayout(new GridLayout(0, 1, 10, 10));

        FlatButton one = new FlatButton("One Player");
        if (toSetup != null) one.addActionListener(toSetup);
        column.add(one);

        FlatButton set = new FlatButton("Settings");
        if (toSettings != null) set.addActionListener(toSettings);
        column.add(set);

        FlatButton q = new FlatButton("Quit");
        if (quit != null) q.addActionListener(quit);
        column.add(q);

        // Add some padding around the column
        JPanel padded = new JPanel(new BorderLayout());
        padded.setOpaque(false);
        padded.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        padded.add(column, BorderLayout.CENTER);

        // Center the padded column
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        add(padded, gc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage == null) {
            g.setColor(new Color(15, 25, 40));
            g.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        int pw = getWidth(), ph = getHeight();
        int iw = backgroundImage.getWidth(null);
        int ih = backgroundImage.getHeight(null);
        if (iw <= 0 || ih <= 0) {
            return;
        }

        double sx = pw / (double) iw;
        double sy = ph / (double) ih;

        // --- COVER scaling ---
        // If we are enlarging, use an INTEGER scale (2x, 3x, ...) to keep pixels perfectly square.
        // If we are shrinking, allow fractional (still nearest-neighbor) so it fits.
        double scale;
        if (pw >= iw && ph >= ih) {
            int intScale = Math.max(
                    (int) Math.ceil(sx),
                    (int) Math.ceil(sy)
            );                    // integer cover when upscaling
            scale = Math.max(1, intScale);
        } else {
            scale = Math.max(sx, sy);  // fractional cover when downscaling
        }

        int w = (int) Math.round(iw * scale);
        int h = (int) Math.round(ih * scale);
        int x = (pw - w) / 2;          // center (can be negative; cropped by panel)
        int y = (ph - h) / 2;

        Graphics2D g2 = (Graphics2D) g.create();
        // keep pixel art crisp
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        g2.drawImage(backgroundImage, x, y, w, h, null);
        g2.dispose();
    }

    private static Image loadImage(String resourcePath) {
        URL url = Menu.class.getResource(resourcePath);
        if (url == null) return null;
        return new ImageIcon(url).getImage();
    }
}
