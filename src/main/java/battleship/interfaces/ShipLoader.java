
package battleship.interfaces;

import battleship.domain.Board;
import battleship.enums.*;
import battleship.fleetplacements.*;
import java.util.List;

public interface ShipLoader {
    List<Fleet.Ship> loadShips(String fileName);
}
