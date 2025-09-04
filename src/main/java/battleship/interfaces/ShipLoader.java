
package battleship.interfaces;

import battleship.fleetplacements.*;
import java.util.List;

public interface ShipLoader {
    List<Fleet.Ship> loadShips(String fileName);
}
