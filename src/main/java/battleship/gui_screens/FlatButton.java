package battleship.gui_screens;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

// Reusable flat, rounded button with hover/press states (no LAF gradients).
public class FlatButton extends JButton {

    private Color base = new Color(30, 80, 140);
    private int arc = 14;

    public FlatButton(String text) {
        super(text);
        commonInit();
    }

    // Use this to set the base color (the button will lighten/darken on hover/press).
    public void setBaseColor(Color c) {
        if (c != null) this.base = c;
        repaint();
    }

    // Change the corner radius.
    public void setCornerRadius(int arc) {
        this.arc = Math.max(0, arc);
        repaint();
    }

    private void commonInit() {
        setFocusPainted(false);
        setBorder(new LineBorder(new Color(0, 0, 0, 60), 1, true));
        setForeground(new Color(245, 245, 245));
        setBackground(base);            // not used by LAF; we paint ourselves
        setOpaque(false);               // custom painter
        setContentAreaFilled(false);    // no default gradient
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(getFont().deriveFont(Font.BOLD, 14f));
        setMargin(new Insets(6, 16, 6, 16));
        setPreferredSize(new Dimension(160, 36));
        setRolloverEnabled(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // derive current background from base color
        Color bg = base;
        ButtonModel m = getModel();
        if (!isEnabled())          bg = bg.darker();
        else if (m.isPressed())    bg = bg.darker();
        else if (m.isRollover())   bg = bg.brighter();

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // subtle inner stroke
        g2.setColor(new Color(255, 255, 255, 25));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);

        g2.dispose();
        super.paintComponent(g); // draws text/icon
    }
}
