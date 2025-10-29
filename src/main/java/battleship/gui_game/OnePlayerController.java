package battleship.gui_game;

import battleship.database.Db;
import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.fleetplacements.Fleet;
import battleship.io.SaveManager;
import battleship.players.Ai;
import static battleship.players.Ai.logresult;
import battleship.playinggame.Battle;
import battleship.playinggame.Shooting;
import battleship.ui.BoardRenderer;
import battleship.ui.DefaultGlyphs;
import battleship.ui.InputManager;

import javax.swing.*;
import java.awt.Point;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OnePlayerController implements OnePlayerActions {
    private static final int AI_DELAY_MS = 650;

    private final OnePlayerGame view;

    private final Board playerBoard;
    private final Fleet playerFleet;
    private final Board aiBoard;
    private final Fleet aiFleet;

    private boolean playerTurn = true;

    public static String logresults;

    public OnePlayerController(OnePlayerGame view,
                               Board playerBoard,  Fleet playerFleet,
                               Board aiBoard,      Fleet aiFleet) {
        this.view = view;
        this.playerBoard = playerBoard;
        this.playerFleet = playerFleet;
        this.aiBoard     = aiBoard;
        this.aiFleet     = aiFleet;

        view.setActions(this);
        view.setModel(playerBoard);
        view.refresh();
        view.setShotsEnabled(true);
        view.setStatusText("Your turn");

        // Disable shots if AI has no ships yet (defensive; should be placed in Setup)
        boolean aiReady = hasAnyShips(aiBoard);
        this.view.setShotsEnabled(aiReady);
        this.view.setStatusText(aiReady ? "Your turn" : "Apply preset first…");
    }

    @Override
    public void fireShot(int r, int c) {
        if (!playerTurn) return;

        boolean willHit = isShipAt(aiBoard, r, c);
        try {
            // returns false if already tried this cell
            if (!Shooting.playershooting(r, c, playerBoard, aiFleet, aiBoard)) return;
        } catch (IOException ex) {
            Logger.getLogger(OnePlayerController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        // Log using your existing strings
        logresults = Shooting.logresult + Battle.hitmiss;
        view.appendLog(logresults);

        view.refresh();
        System.out.println("\n" + BoardRenderer.renderBoth(aiBoard, new DefaultGlyphs()));
        if (aiFleet.allSunk()) {
            JOptionPane.showMessageDialog(view, "You win!");
            view.setShotsEnabled(false);
            view.setStatusText("Game over - you win!");
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
    }

    @Override
    public void quitSave() {
        try (Connection c = Db.connect()) {
            Db.ensureSchema(c);
            String home = System.getProperty("derby.system.home");
            String dbDir = java.nio.file.Path.of(home, "BattleshipDb").toAbsolutePath().toString();

            InputManager.autosave = SaveManager.writeTurnAutosave(playerBoard, aiBoard);

            String sql = "DELETE FROM GAMESTATE WHERE slot = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setLong(1, 1);
                ps.executeUpdate();
            }

            Db.overwriteOrInsert(c, "current", InputManager.autosave);
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
    public void quitDiscard() { System.exit(0); }

    private void aiTurn() {
        // Let your AI choose + apply its shot
        Ai.AiShot(aiBoard, playerFleet, playerBoard);

        view.refresh();

        if (playerFleet.allSunk()) {
            JOptionPane.showMessageDialog(view, "AI wins!");
            view.setShotsEnabled(false);
            view.setStatusText("Game over - AI wins.");
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

    @SuppressWarnings("unused")
    private boolean allShipsSunk(Board b) {
        int n = b.size();
        int ships = 0, hits = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                Cell cell = b.cellAt(r, c, GridType.SHIPS);
                if (cell == Cell.SHIP) ships++;
                else if (cell == Cell.HIT) hits++;
            }
        }
        return ships > 0 && hits >= ships;
    }

    private static String coord(int r, int c) { return "" + (char)('A' + r) + (c + 1); }

    private boolean isShipAt(Board defender, int r, int c) {
        return defender.cellAt(r, c, GridType.SHIPS) == Cell.SHIP;
    }
}
