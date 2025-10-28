package battleship.gui_setup;

import battleship.domain.Board;
import battleship.fleetplacements.Fleet;
import battleship.navigation.Navigator;
import battleship.gui_game.OnePlayerGame;

public class SetupController implements SetupActions {
    private final Navigator nav;
    private final String onePlayerCard;
    private final String menuCard;

    // view to paint into
    private final OnePlayerGame onePlayerGame;

    // model used during setup
    public static Board aiboard;
    public static Fleet aifleet;
    public static Board pboard;
    public static Fleet pfleet;

    public SetupController(Navigator nav, String onePlayerCard, String menuCard,
                           OnePlayerGame onePlayerGame) {
        this.nav = nav;
        this.onePlayerCard = onePlayerCard;
        this.menuCard = menuCard;
        this.onePlayerGame = onePlayerGame;

        aiboard = new Board(10);
        aifleet = new Fleet();
        pboard = new Board(10);
        pfleet = new Fleet();
    }

    @Override
    public void start() {
        // give the view its model and render it, then navigate
        onePlayerGame.setModel(pboard);
        onePlayerGame.refresh();
        nav.show(onePlayerCard);
    }

    @Override
    public void back() {
        nav.show(menuCard);
    }

    @Override
    public void applyPreset() {
        Setup.preset.setEnabled(false);
        SetupServices.setuppresetGUI(pfleet, pboard);
        SetupServices.setuppresetGUI(aifleet, aiboard);
        
    }
    
}
