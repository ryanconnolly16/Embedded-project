package battleship.fleetplacements;

import battleship.BattleshipGUI;
import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.gui_setup.SetupController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FleetSyncTest {
    private Board board;
    private Fleet fleet;
    
    @BeforeEach
    void setUp() {
        board = new Board(10);
        fleet = new Fleet();
        
        // Clear visited arrays
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                SetupController.playervisited[r][c] = false;
            }
        }
        
        // Set the static board that repopulateFromBoard uses
        BattleshipGUI.playerBoard = board;
    }
    
    @Test
    void syncHitsFromBoard_incrementsHitCount_whenShipHit() {
        // Arrange: Place ship and mark as hit
        board.placeShip(2, 2, 3, battleship.enums.Direction.RIGHT);
        // Ensure board reference is current
        BattleshipGUI.playerBoard = board;
        fleet.repopulateFromBoard("player");
        
        // Mark ship as hit on board
        board.shipHit(2, 2);
        board.markHit(2, 2);
        
        // Get initial health
        Fleet.Ship ship = findShipAt(2, 2);
        assertNotNull(ship, "Ship should be found at (2,2) after repopulateFromBoard");
        int initialHealth = ship.getHealth();
        
        // Act
        fleet.syncHitsFromBoard(board);
        
        // Assert: Health should be decreased (hits increased)
        assertTrue(ship.getHealth() < initialHealth || ship.getHealth() == ship.size - 1);
    }
    
    @Test
    void syncHitsFromBoard_syncsMultipleHits() {
        // Arrange: Ship with multiple hits
        board.placeShip(3, 3, 4, battleship.enums.Direction.RIGHT);
        // Ensure board reference is current
        BattleshipGUI.playerBoard = board;
        fleet.repopulateFromBoard("player");
        
        // Mark multiple cells as hit
        board.shipHit(3, 3);
        board.markHit(3, 3);
        board.shipHit(3, 4);
        board.markHit(3, 4);
        board.shipHit(3, 5);
        board.markHit(3, 5);
        
        // Act
        fleet.syncHitsFromBoard(board);
        
        // Assert: All hits should be synced
        Fleet.Ship ship = findShipAt(3, 3);
        assertNotNull(ship, "Ship should be found at (3,3) after repopulateFromBoard");
        assertTrue(ship.getHealth() <= ship.size - 3);
    }
    
    @Test
    void syncHitsFromBoard_doesNotSyncMisses() {
        // Arrange
        board.placeShip(4, 4, 2, battleship.enums.Direction.RIGHT);
        // Ensure board reference is current
        BattleshipGUI.playerBoard = board;
        fleet.repopulateFromBoard("player");
        
        // Mark a miss (not on ship)
        board.markMiss(4, 6);
        board.shipMiss(4, 6);
        
        Fleet.Ship ship = findShipAt(4, 4);
        assertNotNull(ship, "Ship should be found at (4,4) after repopulateFromBoard");
        int initialHealth = ship.getHealth();
        
        // Act
        fleet.syncHitsFromBoard(board);
        
        // Assert: Misses should not decrease health
        assertEquals(initialHealth, ship.getHealth());
    }
    
    @Test
    void repopulateFromBoard_recreatesShipsFromBoardState() {
        // Arrange: Board with ships
        board.placeShip(1, 1, 3, battleship.enums.Direction.RIGHT);
        board.placeShip(5, 5, 2, battleship.enums.Direction.DOWN);
        // Ensure board reference is current
        BattleshipGUI.playerBoard = board;
        
        // Act
        fleet.repopulateFromBoard("player");
        
        // Assert: Fleet should have ships at correct locations
        Fleet.Ship ship1 = findShipAt(1, 1);
        Fleet.Ship ship2 = findShipAt(5, 5);
        
        assertNotNull(ship1, "Ship should be found at (1,1) after repopulateFromBoard");
        assertNotNull(ship2, "Ship should be found at (5,5) after repopulateFromBoard");
        assertEquals(3, ship1.size);
        assertEquals(2, ship2.size);
    }
    
    // Helper method
    private Fleet.Ship findShipAt(int row, int col) {
        for (Fleet.Ship ship : fleet.ships) {
            if (ship.isPlaced() && ship.contains(row, col)) {
                return ship;
            }
        }
        return null;
    }
}
