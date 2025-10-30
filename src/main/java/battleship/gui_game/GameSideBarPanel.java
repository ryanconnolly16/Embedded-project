package battleship.gui_game;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;

// Sidebar with status + auto-sized log that never collapses.
public class GameSideBarPanel extends JPanel {
    private static final Color DARK_BG   = new Color(0x15, 0x3E, 0x78);  // #153E78
    private static final Color LIGHT_FG  = new Color(235, 240, 248);
    private static final int   MIN_W     = 220;   // never smaller than this
    private static final int   MAX_W     = 420;   // clamp growth
    private static final int   PADDING_W = 28;    // border + insets + breathing room

    private final JLabel     status  = new JLabel("Your turn", JLabel.CENTER);
    private final JTextArea  logArea = new JTextArea(12, 20);
    private final JScrollPane scroll;

    public GameSideBarPanel() {
        super(new BorderLayout(6, 6));
        setOpaque(true);
        setBackground(DARK_BG);

        // --- Status header ---
        status.setForeground(LIGHT_FG);
        status.setFont(status.getFont().deriveFont(Font.BOLD, 14f));
        add(status, BorderLayout.NORTH);

        // --- Log text area ---
        logArea.setEditable(false);
        logArea.setLineWrap(false);             // IMPORTANT: no wrapping; we widen instead
        logArea.setWrapStyleWord(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        logArea.setOpaque(true);
        logArea.setBackground(Color.WHITE);
        logArea.setForeground(Color.DARK_GRAY);
        logArea.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        // Auto widen when content changes
        logArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { updateWidth(); }
            @Override public void removeUpdate(DocumentEvent e) { updateWidth(); }
            @Override public void changedUpdate(DocumentEvent e) { updateWidth(); }
        });

        // --- Scroll pane ---
        scroll = new JScrollPane(
                logArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        var titled = BorderFactory.createTitledBorder("Battle log");
        titled.setTitleColor(LIGHT_FG);
        scroll.setBorder(titled);

        // Start with a safe width so it’s visible even when empty
        scroll.setPreferredSize(new Dimension(MIN_W, 240));
        add(scroll, BorderLayout.CENTER);
    }

    // Recompute preferred width so the longest line fits without wrapping.
    private void updateWidth() {
        String longest = longestLine(logArea.getText());
        FontMetrics fm = logArea.getFontMetrics(logArea.getFont());
        int textW = (longest == null) ? 0 : fm.stringWidth(longest);
        int target = Math.max(MIN_W, Math.min(MAX_W, textW + PADDING_W));

        // Apply to scroll (the EAST panel honors preferred width)
        Dimension cur = scroll.getPreferredSize();
        if (cur.width != target) {
            scroll.setPreferredSize(new Dimension(target, cur.height));
            revalidate(); // tell layout to recompute
        }
    }

    private static String longestLine(String s) {
        if (s == null || s.isEmpty()) return null;
        String[] lines = s.split("\\R", -1);
        String best = "";
        for (String ln : lines) if (ln.length() > best.length()) best = ln;
        return best;
    }

    // ---- Public API ----
    public void setStatus(String text) { status.setText(text); }

    public void appendLog(String line) {
        if (line == null || line.isEmpty()) return;
        if (!line.endsWith("\n")) line += "\n";
        logArea.append(line);
        // width will auto-update via the DocumentListener
    }

    public void clearLog() { logArea.setText(""); }
}
