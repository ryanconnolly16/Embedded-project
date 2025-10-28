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
    public static Board board5;
    public static Fleet fleet5;

    public SetupController(Navigator nav, String onePlayerCard, String menuCard,
                           OnePlayerGame onePlayerGame) {
        this.nav = nav;
        this.onePlayerCard = onePlayerCard;
        this.menuCard = menuCard;
        this.onePlayerGame = onePlayerGame;

        board5 = new Board(10);
        fleet5 = new Fleet();
    }

    @Override
    public void start() {
        // give the view its model and render it, then navigate
        onePlayerGame.setModel(board5);
        onePlayerGame.refresh();
        nav.show(onePlayerCard);
    }

    @Override
    public void back() {
        nav.show(menuCard);
    }

    @Override
    public void applyPreset() {
        SetupServices.setuppresetGUI(fleet5, board5);
        onePlayerGame.setModel(board5);
        onePlayerGame.refresh();
    }
}
