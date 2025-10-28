package battleship;

import battleship.gui_game.*;

import battleship.gui_screens.ActionListenerCardSwitch;
import battleship.gui_screens.Menu;
import battleship.gui_screens.QuitListener;

import battleship.gui_settings.Settings;              // Settings panel (not in slides)
import battleship.gui_settings.WindowedSizeAction;    // not in slides
import battleship.gui_settings.FullScreenSizeAction;  // not in slides

import javax.swing.*;
import java.awt.*;

public class BattleshipGUI extends JFrame {

    public static final String CARD_MENU     = "menu";
    public static final String CARD_ONEGAME  = "oneplayergame";
    public static final String CARD_TWOGAME  = "twoplayergame";
    public static final String CARD_SETTINGS = "settings";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    public BattleshipGUI() {
        // ---- Frame setup (slides) ----
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screen = kit.getScreenSize();
        int width  = screen.width / 2;
        int height = screen.height / 2;

        setTitle("Battleship");
        setSize(width, height);
        setLocation((screen.width - width) / 2, (screen.height - height) / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // ---- Card switch listeners (slides pattern) ----
        ActionListenerCardSwitch toMenu          = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_MENU);
        ActionListenerCardSwitch toOnePlayerGame = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_ONEGAME);
        ActionListenerCardSwitch toTwoPlayerGame = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_TWOGAME);
        ActionListenerCardSwitch toSettings      = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_SETTINGS);
        QuitListener quit = new QuitListener(this);

        // ---- Screens ----
        Menu menu = new Menu(toOnePlayerGame, toTwoPlayerGame, toSettings, quit);

        OnePlayerGame one = new OnePlayerGame(toMenu);
        TwoPlayerGame two = new TwoPlayerGame(toMenu);

        // These actions and panel are NOT in the slides, but use the same JButton+ActionListener idea
        var windowed   = new WindowedSizeAction(this);
        var fullscreen = new FullScreenSizeAction(this);
        Settings settingsPanel = new Settings(toMenu, windowed, fullscreen);

        // ---- Add cards ----
        cardPanel.add(menu,          CARD_MENU);
        cardPanel.add(one,           CARD_ONEGAME);
        cardPanel.add(two,           CARD_TWOGAME);
        cardPanel.add(settingsPanel, CARD_SETTINGS);   // use the correct variable name

        add(cardPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BattleshipGUI::new); // slides pattern
    }
}
