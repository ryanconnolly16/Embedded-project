package battleship.domain;

import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.io.FileInput;
import battleship.io.FileOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;

public class BoardLoadSaveTest {
    private Board playerBoard;
    private Board aiBoard;
    private File testSaveFile;
    
    @BeforeEach
    void setUp() throws IOException {
        playerBoard = new Board(10);
        aiBoard = new Board(10);
        
        // Create test file
        testSaveFile = File.createTempFile("test_save", ".txt");
        testSaveFile.deleteOnExit();
    }
    
    @Test
    void saveAndLoad_preservesHitStates() throws IOException {
        // Arrange: Create boards with ships and hits
        playerBoard.placeShip(2, 2, 3, battleship.enums.Direction.RIGHT);
        aiBoard.placeShip(5, 5, 2, battleship.enums.Direction.DOWN);
        
        // Mark some hits
        playerBoard.markHit(3, 3);
        aiBoard.markMiss(6, 6);
        
        // Save using FileOutput directly
        new FileOutput().saveMatch(playerBoard, aiBoard, testSaveFile);
        
        // Load
        FileInput input = new FileInput();
        Board[] loaded = input.loadMatch(testSaveFile);
        Board loadedPlayer = loaded[0];
        Board loadedAi = loaded[1];
        
        // Assert: Hit states are preserved
        assertEquals(Cell.HIT, loadedPlayer.cellAt(3, 3, GridType.SHOTS));
        assertEquals(Cell.MISS, loadedAi.cellAt(6, 6, GridType.SHOTS));
    }
    
    @Test
    void saveAndLoad_preservesShipPlacements() throws IOException {
        // Arrange
        playerBoard.placeShip(1, 1, 4, battleship.enums.Direction.RIGHT);
        aiBoard.placeShip(8, 8, 2, battleship.enums.Direction.DOWN);
        
        // Save and load
        new FileOutput().saveMatch(playerBoard, aiBoard, testSaveFile);
        
        FileInput input = new FileInput();
        Board[] loaded = input.loadMatch(testSaveFile);
        
        // Assert: Ship placements are preserved
        assertEquals(Cell.SHIP, loaded[0].cellAt(1, 1, GridType.SHIPS));
        assertEquals(Cell.SHIP, loaded[0].cellAt(1, 2, GridType.SHIPS));
        assertEquals(Cell.SHIP, loaded[1].cellAt(8, 8, GridType.SHIPS));
        assertEquals(Cell.SHIP, loaded[1].cellAt(9, 8, GridType.SHIPS));
    }
    
    @Test
    void saveAndLoad_preservesShipHits() throws IOException {
        // Arrange: Ship with some hits
        playerBoard.placeShip(3, 3, 3, battleship.enums.Direction.RIGHT);
        playerBoard.shipHit(3, 3); // Mark ship as hit
        playerBoard.markHit(3, 3); // Mark shot as hit
        
        // Save and load
        new FileOutput().saveMatch(playerBoard, aiBoard, testSaveFile);
        
        FileInput input = new FileInput();
        Board[] loaded = input.loadMatch(testSaveFile);
        
        // Assert: Ship hit state is preserved
        assertEquals(Cell.HIT, loaded[0].cellAt(3, 3, GridType.SHIPS));
        assertEquals(Cell.HIT, loaded[0].cellAt(3, 3, GridType.SHOTS));
    }
    
    @Test
    void load_handlesBothGridsCorrectly() throws IOException {
        // Arrange: Board with both SHIPS and SHOTS grids populated
        playerBoard.placeShip(0, 0, 2, battleship.enums.Direction.RIGHT);
        playerBoard.markHit(0, 0);
        playerBoard.markMiss(0, 5);
        
        // Save and load
        new FileOutput().saveMatch(playerBoard, aiBoard, testSaveFile);
        
        FileInput input = new FileInput();
        Board[] loaded = input.loadMatch(testSaveFile);
        Board loadedPlayer = loaded[0];
        
        // Assert: Both grids are correct
        assertEquals(Cell.SHIP, loadedPlayer.cellAt(0, 0, GridType.SHIPS));
        assertEquals(Cell.HIT, loadedPlayer.cellAt(0, 0, GridType.SHOTS));
        assertEquals(Cell.WATER, loadedPlayer.cellAt(0, 5, GridType.SHIPS));
        assertEquals(Cell.MISS, loadedPlayer.cellAt(0, 5, GridType.SHOTS));
    }
}
