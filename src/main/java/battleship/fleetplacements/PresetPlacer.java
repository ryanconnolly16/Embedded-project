package battleship.fleetplacements;

import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.Direction;
import battleship.enums.GridType;
import battleship.enums.Result;
import battleship.interfaces.*;
import static battleship.players.OnePlayer.pboard;
import battleship.ui.BoardRenderer;
import battleship.ui.DefaultGlyphs;
import java.util.Random;

// Single Responsibility: Random ship placement only
class PresetPlacer implements ShipPlacer {
    
    public PresetPlacer() {
        //simple constructor
    }
    
    //same method name and logic as your original
    @Override
    public void placeShips(Fleet fleet, Board board) {
        Random rand = new Random();
        for(int i = 0; i < Fleet.pieces.size(); i++){
            //will loop until the ship fit into board, including not overlapping and making sure it doesnt extend over edge of board
            boolean placementgood = false;
            int attempts = 0;
            while (placementgood == false){
                attempts++;
                int rshipnum = i;
                int rxpos = rand.nextInt(10);
                int rypos = rand.nextInt(10);
                int rdirc = rand.nextInt(4);
                
                Direction dir = null;
                if (rdirc == 0) dir = Direction.UP;
                else if (rdirc == 1) dir = Direction.DOWN;
                else if (rdirc == 2) dir = Direction.LEFT;
                else if (rdirc == 3) dir = Direction.RIGHT;

                Result result = fleet.PlaceShip(board, rshipnum, rxpos, rypos, dir);
                
                if(result == Result.OK){
                    placementgood = true;
                }
            }
        }
    }
}
