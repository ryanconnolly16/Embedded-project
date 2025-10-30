package battleship.domain;


import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.fleetplacements.Fleet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    
    
    
    @Test
    void markHit_updatesBoard() {
    Board b = new Board(10);
    Fleet f = new Fleet();
    
    
    b.markHit(2, 3);
    assertEquals(Cell.HIT, b.cellAt(2, 3, GridType.SHOTS));
    }

}
