
package battleship.interfaces;
import battleship.domain.Board;
import battleship.fleetplacements.*;

public interface UserPlacer {
    void placeShipsInteractive(Fleet fleet, Board board);
}