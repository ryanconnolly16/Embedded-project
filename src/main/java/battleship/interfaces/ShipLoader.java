
package battleship.interfaces;

import battleship.domain.Board;
import battleship.enums.Direction;
import battleship.enums.Result;
import java.util.List;

public interface ShipLoader {
    List<Fleet.Ship> loadShips(String fileName);
}
