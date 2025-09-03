
package battleship.interfaces;
import battleship.domain.Board;
import battleship.enums.*;
import battleship.fleetplacements.*;
import java.util.List;

public interface ShipPlacer {
    void placeShips(Fleet fleet, Board board);
}