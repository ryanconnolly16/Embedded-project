package battleship.gui_game;

import battleship.BattleshipGUI;
import battleship.fleetplacements.Fleet;
import battleship.database.Db;
import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.io.SaveManager;
import battleship.players.Ai;
import static battleship.players.Ai.logresult;
import battleship.playinggame.Battle;
import battleship.playinggame.Shooting;
import battleship.ui.BoardRenderer;
import battleship.ui.DefaultGlyphs;
import battleship.ui.InputManager;
import battleship.gui_setup.SetupController;

import javax.swing.*;
import java.awt.Point;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OnePlayerController implements OnePlayerActions {
    private static final int AI_DELAY_MS = 650;

    private final OnePlayerGame view;

    private final Random rng = new Random();
    private final Set<Point> aiTried = new HashSet<>();
    private boolean playerTurn = true;

    public static String logresults;
    
    public OnePlayerController(OnePlayerGame view) {
        this.view = view;

        view.setActions(this);
        view.setModel(BattleshipGUI.playerBoard);
        
        view.refresh();
        view.setShotsEnabled(true);
        view.setStatusText("Your turn");

        // Disable shots if AI has no ships yet (defensive; should be placed in Setup)
        boolean aiReady = hasAnyShips(BattleshipGUI.aiBoard);
        this.view.setShotsEnabled(aiReady);
        this.view.setStatusText(aiReady ? "Your turn" : "Apply preset first…");
    }

    
    private void probe(int r, int c, Board b, Fleet f, String tag) {
        System.out.printf("[%s] ids: b=%d staticAI=%d%n",
            tag, System.identityHashCode(b), System.identityHashCode(BattleshipGUI.aiBoard));

        Cell sShips = b.cellAt(r, c, GridType.SHIPS);
        Cell sShots = b.cellAt(r, c, GridType.SHOTS);

        System.out.printf("[%s] (r=%d,c=%d) SHIPS=%s SHOTS=%s ",
            tag, r, c, sShips, sShots);

        // transpose check
        if (sShips != Cell.SHIP && c < b.size() && r < b.size()
            && b.cellAt(c, r, GridType.SHIPS) == Cell.SHIP) {
            System.out.println("[NOTE] Board looks transposed (ship at (c,r)).");
        }
    }
    
    
    @Override
    public void fireShot(int r, int c) {
        if (!playerTurn) return;

        try {
            Shooting.playershooting(r, c, BattleshipGUI.playerBoard, BattleshipGUI.aiFleet, BattleshipGUI.aiBoard);
            if (Shooting.value == false) return;
            
        } catch (IOException ex) {
            Logger.getLogger(OnePlayerController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        // Log using your existing strings
        logresults = Shooting.logresult + Battle.hitmiss;
        view.appendLog(logresults);

        view.refresh();
        
        int count = 0;
        for (int r1 = 0; r1 < 10; r1++) {
            for (int c1 = 0; c1 < 10; c1++) {
                if(BattleshipGUI.aiBoard.cellAt(r1, c1, GridType.SHIPS) == Cell.SHIP){
                    count++;
                }
            }
        }
        
        if (count ==0) {
            JOptionPane.showMessageDialog(view, "You win!");
            view.setShotsEnabled(false);
            view.setStatusText("Game over - you win.");
            Db.deleteDerbyLocks();
            System.exit(0);
            return;
        }

        playerTurn = false;
        view.setShotsEnabled(false);
        view.setStatusText("AI thinking…");

        new Timer(AI_DELAY_MS, e -> {
            aiTurn();
            
            
            logresults = Ai.logresult + Battle.hitmiss;
            view.appendLog(logresults);
            ((Timer) e.getSource()).stop();
        }).start();
        
        
        System.out.println("\nhi" + BoardRenderer.renderBoth(BattleshipGUI.aiBoard, new DefaultGlyphs()));
        
        
        
        
        view.refresh();
        view.revalidate();
        view.repaint();
    }

    @Override
    public void quitSave() {
        try (Connection c = Db.connect()) {
            Db.ensureSchema(c);
            String home = System.getProperty("derby.system.home");
            String dbDir = java.nio.file.Path.of(home, "BattleshipDb").toAbsolutePath().toString();
            
            
            InputManager.autosave = SaveManager.writeTurnAutosave(BattleshipGUI.playerBoard, BattleshipGUI.aiBoard);
            String sql = "DELETE FROM GAMESTATE WHERE slot = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setLong(1, 1);
                ps.executeUpdate();
            }

            Db.overwriteOrInsert(c, "current", InputManager.autosave);
            
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
        // Let your AI choose + apply its shot
        Ai.AiShot(BattleshipGUI.aiBoard, BattleshipGUI.playerFleet, BattleshipGUI.playerBoard);

        
        
        view.refresh();
        int count = 0;
        for (int r2 = 0; r2 < 10; r2++) {
            for (int c2 = 0; c2 < 10; c2++) {
                if(BattleshipGUI.playerBoard.cellAt(r2, c2, GridType.SHIPS) == Cell.WATER){
                    count++;
                }
            }
        }
        
        if (count ==0) {
            JOptionPane.showMessageDialog(view, "AI wins!");
            view.setShotsEnabled(false);
            view.setStatusText("Game over - AI wins.");
            Db.deleteDerbyLocks();
            System.exit(0);
            return;
        }

        playerTurn = true;
        view.setShotsEnabled(true);
        view.setStatusText("Your turn");
    }

    // helpers
    private boolean hasAnyShips(Board b) {
        int n = b.size();
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                if (b.cellAt(r, c, GridType.SHIPS) == Cell.SHIP) return true;
        return false;
    }


    private static String coord(int r, int c) { return "" + (char)('A' + r) + (c + 1); }

    private boolean isShipAt(Board defender, int r, int c) {
        return defender.cellAt(r, c, GridType.SHIPS) == Cell.SHIP;
    }
}
