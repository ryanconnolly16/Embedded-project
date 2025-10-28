// battleship/gui_screens/SetupController.java
package battleship.gui_setup;

import battleship.navigation.Navigator;

public class SetupController implements SetupActions {
    private final Navigator nav;
    private final String onePlayerCard;
    private final String menuCard;

    public SetupController(Navigator nav, String onePlayerCard, String menuCard) {
        this.nav = nav;
        this.onePlayerCard = onePlayerCard;
        this.menuCard = menuCard;
    }

    @Override public void start()       { nav.show(onePlayerCard); }
    @Override public void back()        { nav.show(menuCard); }
    @Override public void applyPreset() { /*logic*/ }
}
