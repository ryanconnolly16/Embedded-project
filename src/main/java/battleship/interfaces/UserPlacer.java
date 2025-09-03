
package battleship.interfaces;
import battleship.domain.Board;
import battleship.enums.*;
import battleship.fleetplacements.*;
import java.util.List;

public interface UserPlacer {
    void placeShipsInteractive(Fleet fleet, Board board);
}