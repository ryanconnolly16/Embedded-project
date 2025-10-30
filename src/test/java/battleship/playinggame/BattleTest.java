package battleship.playinggame;

import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.fleetplacements.Fleet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BattleTest {
    private Board playerBoard;
    private Board aiBoard;
    private Fleet aiFleet;
    
    @BeforeEach
    void setUp() {
        playerBoard = new Board(10);
        aiBoard = new Board(10);
        aiFleet = new Fleet();
        
        // Place a ship on AI board for testing
        aiBoard.placeShip(3, 3, 3, battleship.enums.Direction.RIGHT);
        
        // Sync fleet personality with board
        aiFleet.repopulateFromBoard("ai");
    }
    
    @Test
    void usershot_hitsShip_whenCellHasShip() {
        // Arrange: Ship is at (3,3), (3,4), (3,5)
        int row = 3;
        int col = 3;
        
        // Act
        Battle.usershot("d4", playerBoard, aiFleet, aiBoard);
        
        // Assert
        assertEquals(Cell.HIT, playerBoard.cellAt(row, col, GridType.SHOTS));
        assertEquals(Cell.HIT, aiBoard.cellAt(row, col, GridType.SHIPS));
        assertTrue(Battle.hitmiss.contains("Hit"));
    }
    
    @Test
    void usershot_misses_whenCellIsWater() {
        // Arrange: No ship at (0,0)
        int row = 0;
        int col = 0;
        
        // Act
        Battle.usershot("a1", playerBoard, aiFleet, aiBoard);
        
        // Assert
        assertEquals(Cell.MISS, playerBoard.cellAt(row, col, GridType.SHOTS));
        assertEquals(Cell.MISS, aiBoard.cellAt(row, col, GridType.SHIPS));
        assertEquals("Miss", Battle.hitmiss);
    }
    
    @Test
    void usershot_hitsShipBasedOnBoardState_notFleetState() {
        // This tests the fix where board state is source of truth
        // Arrange: Ship on board but fleet might not be synced
        aiBoard.placeShip(5, 5, 2, battleship.enums.Direction.DOWN);
        // Don't sync fleet - simulating a loaded game scenario
        
        // Act
        Battle.usershot("f6", playerBoard, aiFleet, aiBoard);
        
        // Assert: Should still detect hit based on board state
        assertEquals(Cell.HIT, playerBoard.cellAt(5, 5, GridType.SHOTS));
        assertTrue(Battle.hitmiss.contains("Hit") || Battle.hitmiss.equals("Hit"));
    }
    
    @Test
    void usershot_marksAlreadyHitCellAsHit() {
        // Arrange: Already hit ship cell
        aiBoard.placeShip(7, 7, 2, battleship.enums.Direction.RIGHT);
        aiBoard.shipHit(7, 7); // Mark as already hit
        
        // Act
        Battle.usershot("h8", playerBoard, aiFleet, aiBoard);
        
        // Assert: Should still mark as hit (not miss)
        assertEquals(Cell.HIT, playerBoard.cellAt(7, 7, GridType.SHOTS));
        assertTrue(Battle.hitmiss.contains("Hit"));
    }
    
    @Test
    void aishot_hitsShip_whenCellHasShip() {
        // Arrange
        Board playerFleetBoard = new Board(10);
        Fleet playerFleet = new Fleet();
        playerFleetBoard.placeShip(2, 2, 2, battleship.enums.Direction.RIGHT);
        playerFleet.repopulateFromBoard("player");
        
        int row = 2;
        int col = 2;
        
        // Act
        Battle.aishot(row, col, aiBoard, playerFleet, playerFleetBoard);
        
        // Assert
        assertEquals(Cell.HIT, aiBoard.cellAt(row, col, GridType.SHOTS));
        assertEquals(Cell.HIT, playerFleetBoard.cellAt(row, col, GridType.SHIPS));
        assertTrue(Battle.hitmiss.contains("Hit"));
    }
    
    @Test
    void aishot_misses_whenCellIsWater() {
        // Arrange
        Board playerFleetBoard = new Board(10);
        Fleet playerFleet = new Fleet();
        
        int row = 0;
        int col = 0;
        
        // Act
        Battle.aishot(row, col, aiBoard, playerFleet, playerFleetBoard);
        
        // Assert
        assertEquals(Cell.MISS, aiBoard.cellAt(row, col, GridType.SHOTS));
        assertEquals(Cell.MISS, playerFleetBoard.cellAt(row, col, GridType.SHIPS));
        assertEquals("Miss", Battle.hitmiss);
    }
}
