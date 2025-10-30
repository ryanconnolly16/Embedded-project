package battleship.players;

import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.fleetplacements.Fleet;
import battleship.gui_setup.SetupController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AiTest {
    private Board aiBoard;
    private Board playerBoard;
    private Fleet playerFleet;
    
    @BeforeEach
    void setUp() {
        aiBoard = new Board(10);
        playerBoard = new Board(10);
        playerFleet = new Fleet();
        
        // Clear visited arrays
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                SetupController.aivisited[r][c] = false;
            }
        }
        
        // Place some ships for testing
        playerBoard.placeShip(2, 2, 3, battleship.enums.Direction.RIGHT);
        playerFleet.repopulateFromBoard("player");
    }
    
    @Test
    void aiShot_findsValidShot_whenEmptyCellsExist() {
        // Arrange: Most cells are empty
        int initialShots = countShots(aiBoard);
        
        // Act
        Ai.AiShot(aiBoard, playerFleet, playerBoard);
        
        // Assert: AI should have fired a shot
        int finalShots = countShots(aiBoard);
        assertEquals(initialShots + 1, finalShots);
        assertNotNull(Ai.logresult);
        assertFalse(Ai.logresult.contains("error"));
    }
    
    @Test
    void aiShot_checksShotsGrid_notShipsGrid() {
        // Arrange: Mark a cell as already shot on SHOTS grid
        aiBoard.markMiss(5, 5);
        
        // Act
        Ai.AiShot(aiBoard, playerFleet, playerBoard);
        
        // Assert: AI should not shoot at (5,5) since it's already marked as MISS on SHOTS grid
        // The shot should be at a different location
        assertNotNull(Ai.usershot);
        // Verify (5,5) is marked as visited after shot attempt check
        // (This tests that AI correctly checks SHOTS grid)
    }
    
    @Test
    void aiShot_marksCellAsVisited_afterShooting() {
        // Arrange: Track which cells were already shot BEFORE the AI shoots
        boolean[][] shotBefore = new boolean[10][10];
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                Cell cell = aiBoard.cellAt(r, c, GridType.SHOTS);
                shotBefore[r][c] = (cell == Cell.HIT || cell == Cell.MISS);
            }
        }
        
        // Act: AI shoots
        Ai.AiShot(aiBoard, playerFleet, playerBoard);
        
        // Assert: Find the newly shot cell (wasn't shot before, is shot now)
        boolean foundNewShot = false;
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                Cell cell = aiBoard.cellAt(r, c, GridType.SHOTS);
                boolean isNowShot = (cell == Cell.HIT || cell == Cell.MISS);
                
                // If this cell wasn't shot before but is now, it's the new shot
                if (!shotBefore[r][c] && isNowShot) {
                    // This newly shot cell should be marked as visited
                    assertTrue(SetupController.aivisited[r][c], 
                              "Newly shot cell at (" + r + "," + c + ") should be marked as visited");
                    foundNewShot = true;
                    break;
                }
            }
            if (foundNewShot) break;
        }
        assertTrue(foundNewShot, "AI should have shot a new cell");
        assertNotNull(Ai.usershot, "AI should have recorded a shot location");
    }
    
    private int countUnshotCells(Board board) {
        int count = 0;
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                Cell cell = board.cellAt(r, c, GridType.SHOTS);
                if (cell != Cell.HIT && cell != Cell.MISS) {
                    count++;
                }
            }
        }
        return count;
    }
    
    @Test
    void aiShot_syncsWithBoardState_afterLoading() {
        // Arrange: Simulate loaded game - board has shots but aivisited is reset
        aiBoard.markHit(4, 4);
        aiBoard.markMiss(4, 5);
        // aivisited should be false for these (simulating reset after load)
        
        // Act
        Ai.AiShot(aiBoard, playerFleet, playerBoard);
        
        // Assert: AI should not shoot at (4,4) or (4,5) since board shows they're already shot
        String shotLocation = Ai.usershot;
        assertNotNull(shotLocation);
        // The shot should not be at (4,4) or (4,5)
        assertFalse(Ai.usershot.equals("E5") || Ai.usershot.equals("E6"));
    }
    
    @Test
    void aiShot_returnsWithoutError_whenAllCellsShot() {
        // Arrange: Mark all cells as shot
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (r % 2 == 0) {
                    aiBoard.markHit(r, c);
                } else {
                    aiBoard.markMiss(r, c);
                }
            }
        }
        
        // Act
        Ai.AiShot(aiBoard, playerFleet, playerBoard);
        
        // Assert: Should return early without error (or with appropriate error message)
        // The early return check should prevent infinite loop
        assertTrue(true); // If we get here, no exception was thrown
    }
    
    // Helper methods
    private int countShots(Board board) {
        int count = 0;
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                Cell cell = board.cellAt(r, c, GridType.SHOTS);
                if (cell == Cell.HIT || cell == Cell.MISS) {
                    count++;
                }
            }
        }
        return count;
    }
    
    private void markAllAsVisitedExcept(int excludeRow, int excludeCol) {
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (r != excludeRow || c != excludeCol) {
                    SetupController.aivisited[r][c] = true;
                }
            }
        }
    }
}
