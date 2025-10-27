package battleship.gui;

import javax.swing.*;
import java.awt.*;

public class BattleshipGUI extends JFrame {

    public static final String CARD_MENU        = "menu";
    public static final String CARD_ONEGAME     = "oneplayergame";
    public static final String CARD_TWOGAME     = "twoplayergame";
    public static final String CARD_SETTINGS    = "settings";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    public BattleshipGUI() {
        // Sizing per slides
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screen = kit.getScreenSize();
        int width = screen.width / 2, height = screen.height / 2;

        setTitle("Battleship");
        setSize(width, height);
        setLocation(width / 2, height / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Build listeners in separate classes
        ActionListenerCardSwitch toMenu              = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_MENU);
        ActionListenerCardSwitch toOnePlayerGame     = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_ONEGAME);
        ActionListenerCardSwitch toTwoPlayerGame     = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_TWOGAME);
        ActionListenerCardSwitch toSettings          = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_SETTINGS);
        QuitListener quit = new QuitListener(this);

        // Create panels
        Menu menu = new Menu(toOnePlayerGame, toTwoPlayerGame, toSettings, quit);
        OnePlayerGame onePlayerGame = new OnePlayerGame(toMenu);
        TwoPlayerGame twoPlayerGame = new TwoPlayerGame(toMenu);
        Settings settings = new Settings(toMenu);

        // Add to CardLayout container
        cardPanel.add(menu, CARD_MENU);
        cardPanel.add(onePlayerGame, CARD_ONEGAME);
        cardPanel.add(twoPlayerGame, CARD_TWOGAME);
        cardPanel.add(settings, CARD_SETTINGS);

        add(cardPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BattleshipGUI::new);
    }
}