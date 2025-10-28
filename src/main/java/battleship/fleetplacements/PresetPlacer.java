package battleship.fleetplacements;

import battleship.domain.Board;
import battleship.enums.*;
import battleship.interfaces.*;
import java.util.Random;

public class PresetPlacer implements ShipPlacer {
    
    public PresetPlacer() {}
    
    //function to randomly pllace ships
    @Override
    public void placeShips(Fleet fleet, Board board) {
        Random rand = new Random();
        for(int i = 0; i < Fleet.pieces.size(); i++){
            boolean placementgood = false;
            int attempts = 0;
            while (placementgood == false){
                attempts++;
                int rshipnum = i;
                int rxpos = rand.nextInt(10);
                int rypos = rand.nextInt(10);
                int rdirc = rand.nextInt(4);
                
                Direction dir = null;
                switch (rdirc) {
                    case 0 -> dir = Direction.UP;
                    case 1 -> dir = Direction.DOWN;
                    case 2 -> dir = Direction.LEFT;
                    case 3 -> dir = Direction.RIGHT;
                    default -> {
                    }
                }

                Result result = fleet.placeShip(board, rshipnum, rxpos, rypos, dir);
                
                if(result == Result.OK){
                    placementgood = true;
                }
            }
        }
    }
}
