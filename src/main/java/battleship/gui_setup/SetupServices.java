package battleship.gui_setup;

import battleship.domain.Board;
import battleship.fleetplacements.Fleet;

public class SetupServices {
    
    public static void setupPresetGUI(Fleet fleet, Board board){
        fleet.preset(fleet, board); 
        
    }
}
