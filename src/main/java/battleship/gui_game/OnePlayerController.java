package battleship.gui_game;

import battleship.BattleshipGUI;
import battleship.fleetplacements.Fleet;
import battleship.database.Db;
import static battleship.database.Db.recordShot;
import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.io.SaveManager;
import battleship.players.Ai;
import battleship.playinggame.Battle;
import battleship.playinggame.Shooting;
import battleship.ui.BoardRenderer;
import battleship.ui.DefaultGlyphs;
import battleship.ui.InputManager;
import battleship.gui_setup.SetupServices;
import java.sql.SQLException;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

// Controller for one-player battleship game mode.
// Handles turn-based gameplay between human player and AI, including shot processing,
// game state management, database operations, and UI updates.
public class OnePlayerController implements OnePlayerActions {
    // Delay in milliseconds before AI takes its turn (for visual feedback)
    private static final int AI_DELAY_MS = 50;
    
    // Reference to the game view for UI updates
    private final OnePlayerGame view;
    
    // Tracks whether it's currently the player's turn (true) or AI's turn (false)
    private boolean playerTurn = true;
    
    // Accumulated log results for display in the game log
    public static String logresults;
    
    public int count = 0;
    
    // Constructor for OnePlayerController.
    // Parameter: view - The OnePlayerGame view instance to control
    public OnePlayerController(OnePlayerGame view) {
        this.view = view;
        view.setActions(this);
        view.setModel(BattleshipGUI.playerBoard);
        view.refresh();
        view.setShotsEnabled(true);
        view.setStatusText("Your turn");

        // Disable shots if AI has no ships yet 
        boolean aiReady = !hasAnyShips(BattleshipGUI.aiBoard);
        this.view.setShotsEnabled(aiReady);
        this.view.setStatusText(aiReady ? "Your turn" : "Apply preset first…");
    }
    
    // Handles the player's shot attempt at the specified coordinates.
    // Processes the shot, updates boards, records to database, checks for game end,
    // and triggers AI turn after a short delay.
    // Parameters: r - Row coordinate (0-based) where player wants to shoot
    //             c - Column coordinate (0-based) where player wants to shoot
    @Override
    public void fireShot(int r, int c) {
        // Ignore shot attempts when it's not the player's turn
        if (!playerTurn) return;
        try {
            Shooting.playershooting(r, c, BattleshipGUI.playerBoard, BattleshipGUI.aiFleet, BattleshipGUI.aiBoard);
            if (Shooting.value == false) return;
            
        } catch (IOException ex) {
            Logger.getLogger(OnePlayerController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        // String for the log textbox saying where user shot and result
        logresults = Shooting.logresult + Battle.hitmiss;
        view.appendLog(logresults);

        
        try (Connection d = Db.connect()) {
            Db.ensureSchema(d);
            recordShot(d, Db.Player.PLAYER1, Shooting.usershots, Battle.hitmiss);
            if (!d.getAutoCommit()) d.commit();
        }
        catch (SQLException ex) {    
            Logger.getLogger(OnePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        view.refresh();
        // Check if game is over (no ships remaining on AI's board)
        gamefinished(BattleshipGUI.aiBoard, "user");

        playerTurn = false;
        view.setShotsEnabled(false);
        view.setStatusText("AI thinking…");
        System.out.println("\n" + BoardRenderer.renderBoth(BattleshipGUI.aiBoard, new DefaultGlyphs()));
        Timer t = new Timer(AI_DELAY_MS, e -> { 
            aiTurn(); 
            // Only append hitmiss if AI actually fired (not an error)
            String logMsg = Ai.logresult;
            if (Ai.logresult != null && !Ai.logresult.contains("error")) {
                logMsg += Battle.hitmiss;
            }
            view.appendLog(logMsg);
        });
        t.setRepeats(false);
        t.start();
        view.refresh();
        view.revalidate();
        view.repaint();
    }

    // Saves the current game state to the database and exits the application.
    // Clears the current game slot, writes the autosave, and deletes Derby lock files.
    @Override
    public void quitSave() {
        try (Connection c = Db.connect()) {
            Db.ensureSchema(c);
            InputManager.autosave = SaveManager.writeTurnAutosave(BattleshipGUI.playerBoard, BattleshipGUI.aiBoard);
            String sql = "DELETE FROM GAMESTATE WHERE slot = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setLong(1, 1);
                ps.executeUpdate();
            }

            Db.overwriteOrInsert(c, "current", InputManager.autosave);
            // Delete lock files so they can be examined in services tab
            Db.deleteDerbyLocks();
            System.exit(0);
        } catch (java.sql.SQLException | java.io.IOException e) {
            JOptionPane.showMessageDialog(view, "Save failed: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Save failed: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Quits the game without saving. Deletes Derby lock files and exits.
    @Override
    public void quitDiscard() { 
        Db.deleteDerbyLocks();
        System.exit(0); 
    }

    // Handles the AI's turn. Makes the AI shoot, updates the UI, checks for game end,
    // records the shot to the database, and returns control to the player.
    private void aiTurn() {
        Ai.AiShot(BattleshipGUI.aiBoard, BattleshipGUI.playerFleet, BattleshipGUI.playerBoard);

        
        //checks for any ships on board and ends game
        view.refresh();
        // Check if game is over (no ships remaining on player's board)
        gamefinished(BattleshipGUI.playerBoard, "ai");

        // Record AI's shot to the database
        try (Connection d = Db.connect()) {
            Db.ensureSchema(d);
            recordShot(d, Db.Player.PLAYER2, Ai.usershot, Battle.hitmiss);
            if (!d.getAutoCommit()) d.commit();
        }
        catch (SQLException ex) {    
            Logger.getLogger(OnePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Return control to the player
        playerTurn = true;
        view.setShotsEnabled(true);
        view.setStatusText("Your turn");
    }

    // Checks if there are any ships remaining on the specified board.
    // Parameter: b - The board to check
    // Returns: true if at least one SHIP cell exists, false otherwise
    private boolean hasAnyShips(Board b) {
        int n = b.size();
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                if (b.cellAt(r, c, GridType.SHIPS) == Cell.SHIP) return true;
        return false;
    }
    
    
    // Checks if the game is finished (no ships remaining) and handles game end logic.
    // Displays winner message, resets game state in database, and exits the application.
    // Parameters: board - The board to check for remaining ships
    //             player - String identifying the winner ("user" for player, "ai" for AI)
    public void gamefinished(Board board, String player){
        // Check if any ships remain (game is finished when no ships found)
        boolean finished = hasAnyShips(board);
        
        if (!finished) {
            // Game is over - display winner and reset game state
            if("user".equals(player)){
                JOptionPane.showMessageDialog(view, "You win!");
                view.setStatusText("Game over - you win.");
            }
            else{
                JOptionPane.showMessageDialog(view, "Ai wins!");
                view.setStatusText("Game over - Ai wins.");
            }
            
            // Reset game state in database for next game
            try (Connection h = Db.connect()) {
                // Clear existing game state
                try (java.sql.Statement st = h.createStatement()) {
                    st.executeUpdate("TRUNCATE TABLE GAMESTATE"); 
                }
                
                // Create fresh boards and fleets for next game
                Board pBoard = new Board(10);
                Board aBoard = new Board(10);
                Fleet pFleet = new Fleet();
                Fleet aFleet = new Fleet();
                
                // Place ships on fresh boards using preset placement
                SetupServices.setupPresetGUI(pFleet, pBoard);
                SetupServices.setupPresetGUI(aFleet, aBoard);
                
                // Save the new game state to database
                Db.ensureSchema(h);
                InputManager.autosave = SaveManager.writeTurnAutosave(pBoard, aBoard);
                Db.overwriteOrInsert(h, "current", InputManager.autosave);
                
                
            }catch (java.sql.SQLException | java.io.IOException e) {
                JOptionPane.showMessageDialog(view, "Save failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Save failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            
            
            
            view.setShotsEnabled(false);
            
            Db.deleteDerbyLocks();
            System.exit(0);
            return;
        }
    }
}
