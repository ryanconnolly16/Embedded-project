package battleship;

import battleship.domain.Board;
import battleship.fleetplacements.Fleet;
import battleship.gui_screens.ActionListenerCardSwitch;
import battleship.gui_screens.Menu;
import battleship.gui_screens.QuitListener;
import battleship.gui_setup.Setup;
import battleship.gui_setup.SetupController;
import battleship.navigation.CardNavigator;
import battleship.navigation.Navigator;
import battleship.gui_game.OnePlayerGame;
import battleship.gui_game.OnePlayerController;
import battleship.gui_settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BattleshipGUI extends JFrame {

    public static final String CARD_MENU     = "menu";
    public static final String CARD_SETUP    = "setup";
    public static final String CARD_ONEGAME  = "oneplayergame";
    public static final String CARD_SETTINGS = "settings";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    public BattleshipGUI() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screen = kit.getScreenSize();
        int width  = screen.width / 2, height = screen.height / 2;
        Dimension defaultSize = new Dimension(width, height);

        setTitle("Battleship");
        setSize(width, height);
        setLocation((screen.width - width) / 2, (screen.height - height) / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // existing card switchers for Menu buttons
        ActionListenerCardSwitch toMenu     = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_MENU);
        ActionListenerCardSwitch toSetup    = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_SETUP);
        ActionListenerCardSwitch toSettings = new ActionListenerCardSwitch(cardLayout, cardPanel, CARD_SETTINGS);
        QuitListener quit = new QuitListener(this);

        // screens
        Menu menu = new Menu(toSetup, toSettings, quit);
        Navigator navigator = new CardNavigator(cardLayout, cardPanel);
        
        Board playerBoard = new Board(10);
        Board aiBoard     = new Board(10);
        Fleet playerFleet = new Fleet();
        Fleet aiFleet     = new Fleet();

        OnePlayerGame one = new OnePlayerGame();
        OnePlayerController controller = new OnePlayerController(one, playerBoard, playerFleet, aiBoard, aiFleet);

        Setup setup = new Setup(new SetupController(navigator, CARD_ONEGAME, CARD_MENU, one, playerBoard, playerFleet, aiBoard, aiFleet));

        ActionListener goFullscreen = battleship.gui_settings.ScreenSizeUtils.fullscreen(this);
        ActionListener goWindowed   = battleship.gui_settings.ScreenSizeUtils.windowed(this, defaultSize);
        battleship.gui_settings.ScreenSizeUtils.bindEscapeToExit(this, goWindowed);
        Settings settings = new Settings(toMenu, goWindowed, goFullscreen);

        // add cards
        cardPanel.add(menu,          CARD_MENU);
        cardPanel.add(setup,    CARD_SETUP);
        cardPanel.add(one,           CARD_ONEGAME);
        cardPanel.add(settings, CARD_SETTINGS);

        add(cardPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BattleshipGUI::new);
    }
}
