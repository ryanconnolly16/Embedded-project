
package battleship.gui_setup;

import battleship.domain.Board;
import battleship.fleetplacements.Fleet;
import battleship.ui.BoardRenderer;
import battleship.ui.DefaultGlyphs;
import battleship.gui_setup.*;
public class SetupServices {
    public static void setuppresetGUI(Fleet fleet, Board board){
        fleet.preset(fleet, board); 
        
    }
}
