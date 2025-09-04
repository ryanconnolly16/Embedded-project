
package battleship.interfaces;
import battleship.domain.Board;
import battleship.fleetplacements.*;

public interface ShipPlacer {
    void placeShips(Fleet fleet, Board board);
}