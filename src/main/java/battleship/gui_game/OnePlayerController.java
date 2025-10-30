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

public class OnePlayerController implements OnePlayerActions {
    private static final int AI_DELAY_MS = 50;
    private final OnePlayerGame view;
    private boolean playerTurn = true;
    public static String logresults;
    public int count = 0;
    
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
    
    @Override
    public void fireShot(int r, int c) {
        if (!playerTurn) return;
        //function to fire shot for user
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
        //checks board to see if any ships left and ends game
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

    @Override
    public void quitSave() {
        //saves current board to database
        try (Connection c = Db.connect()) {
            Db.ensureSchema(c);
            InputManager.autosave = SaveManager.writeTurnAutosave(BattleshipGUI.playerBoard, BattleshipGUI.aiBoard);
            String sql = "DELETE FROM GAMESTATE WHERE slot = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setLong(1, 1);
                ps.executeUpdate();
            }

            Db.overwriteOrInsert(c, "current", InputManager.autosave);
            //deletes lock files so can look at them in services tab
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

    @Override
    public void quitDiscard() { Db.deleteDerbyLocks();System.exit(0); }

    private void aiTurn() {
        // ai shooting function
        Ai.AiShot(BattleshipGUI.aiBoard, BattleshipGUI.playerFleet, BattleshipGUI.playerBoard);

        
        //checks for any ships on board and ends game
        view.refresh();
        gamefinished(BattleshipGUI.playerBoard, "ai");

        try (Connection d = Db.connect()) {
            
            Db.ensureSchema(d);
            recordShot(d, Db.Player.PLAYER2, Ai.usershot, Battle.hitmiss);
            if (!d.getAutoCommit()) d.commit();
        }
        catch (SQLException ex) {    
            Logger.getLogger(OnePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        playerTurn = true;
        view.setShotsEnabled(true);
        view.setStatusText("Your turn");
    }

    // checks for any ships left on board
    private boolean hasAnyShips(Board b) {
        int n = b.size();
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                if (b.cellAt(r, c, GridType.SHIPS) == Cell.SHIP) return true;
        return false;
    }
    
    
    //ends the game with winners screen
    public void gamefinished(Board board,String player){
        boolean finished = hasAnyShips(board);
        
        if (!finished) {
            if("user".equals(player)){
                JOptionPane.showMessageDialog(view, "You win!");
                view.setStatusText("Game over - you win.");
            }
            else{
                JOptionPane.showMessageDialog(view, "Ai wins!");
                view.setStatusText("Game over - Ai wins.");
            }
            
            try (Connection h = Db.connect()) {
                try (java.sql.Statement st = h.createStatement()) {
                    st.executeUpdate("TRUNCATE TABLE GAMESTATE"); 
                }
                
                
                Board pBoard = new Board(10);
                Board aBoard     = new Board(10);
                Fleet pFleet = new Fleet();
                Fleet aFleet     = new Fleet();
                
                SetupServices.setupPresetGUI(pFleet,  pBoard);
                SetupServices.setupPresetGUI(aFleet, aBoard);
                
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
