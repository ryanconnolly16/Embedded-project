// battleship/gui_screens/SetupController.java
package battleship.gui_setup;

import battleship.domain.Board;
import battleship.gui_setup.SetupServices;
import battleship.fleetplacements.Fleet;
import battleship.fleetplacements.*;
import battleship.fleetplacements.PresetPlacer;
import battleship.navigation.Navigator;
import battleship.ui.BoardRenderer;
import battleship.ui.DefaultGlyphs;

public class SetupController implements SetupActions {
    private final Navigator nav;
    private final String onePlayerCard;
    private final String menuCard;
    public static Board board5;
    public static Fleet fleet5;

    public SetupController(Navigator nav, String onePlayerCard, String menuCard) {
        this.nav = nav;
        this.onePlayerCard = onePlayerCard;
        this.menuCard = menuCard;
        board5 = new Board(10);
        fleet5 = new Fleet();
    }

    @Override public void start()       { nav.show(onePlayerCard); }
    @Override public void back()        { nav.show(menuCard); }
    @Override public void applyPreset() { SetupServices.setuppresetGUI(fleet5,board5);};
}
