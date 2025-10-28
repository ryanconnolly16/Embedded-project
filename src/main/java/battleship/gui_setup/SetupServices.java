
package battleship.gui_setup;

import battleship.domain.Board;
import battleship.fleetplacements.Fleet;
import battleship.ui.BoardRenderer;
import battleship.ui.DefaultGlyphs;

public class SetupServices {
    public static void setuppresetGUI(Fleet fleet, Board board){
        fleet.preset(fleet, board); 

        System.out.println("\n" + BoardRenderer.renderBoth(board, new DefaultGlyphs()));
    }
}
