package battleship.gui_game;

import battleship.navigation.Navigator;

public class OnePlayerController implements OnePlayerActions {
    private final Navigator nav;
    private final String menuCard;

    public OnePlayerController(Navigator nav, String menuCard) {
        this.nav = nav;
        this.menuCard = menuCard;
    }

    @Override
    public void quitSave() {
        // TODO: save game state here
        nav.show(menuCard);
    }

    @Override
    public void quitDiscard() {
        // TODO: discard game state here
        nav.show(menuCard);
    }
}
