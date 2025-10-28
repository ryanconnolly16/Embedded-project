package battleship.gui_game;

import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OnePlayerController implements OnePlayerActions {
    private static final int AI_DELAY_MS = 650;

    private final OnePlayerGame view;
    private final Board playerBoard; 
    private final Board aiBoard;     

    private final Random rng = new Random();
    private final Set<Point> aiTried = new HashSet<>();
    private boolean playerTurn = true;

    public OnePlayerController(OnePlayerGame view, Board playerBoard, Board aiBoard) {
        this.view = view;
        this.playerBoard = playerBoard;
        this.aiBoard = aiBoard;

        this.view.setActions(this);
        this.view.setModel(playerBoard);
        this.view.refresh();
        this.view.setShotsEnabled(true);
        this.view.setStatusText("Your turn");
    }

    @Override
    public void fireShot(int r, int c) {
        if (!playerTurn) {
            return;
        }

        boolean willHit = isShipAt(aiBoard, r, c);  // peek before applying
        if (!applyShot(playerBoard, aiBoard, r, c)) {
            return;
        }

        view.log("You fired at " + coord(r, c) + " - " + (willHit ? "HIT!" : "miss"));
        view.refresh();

        if (allShipsSunk(aiBoard)) {
            JOptionPane.showMessageDialog(view, "You win!");
            view.setShotsEnabled(false);
            view.setStatusText("Game over — you win!");
            return;
        }

        playerTurn = false;
        view.setShotsEnabled(false);
        view.setStatusText("AI thinking…");

        new Timer(AI_DELAY_MS, e -> {
            aiTurn();
            ((Timer) e.getSource()).stop();
        }).start();
    }

    @Override
    public void quitSave() {
        // TODO: persist state
        System.exit(0);
    }

    @Override
    public void quitDiscard() {
        System.exit(0);
    }

    private void aiTurn() {
        Point p = pickAiTarget();
        if (p == null) {
            return;
        }

        boolean willHit = isShipAt(playerBoard, p.x, p.y);
        applyShot(aiBoard, playerBoard, p.x, p.y);

        view.log("AI fired at " + coord(p.x, p.y) + " - " + (willHit ? "HIT!" : "miss"));
        view.refresh();

        if (allShipsSunk(playerBoard)) {
            JOptionPane.showMessageDialog(view, "AI wins!");
            view.setShotsEnabled(false);
            view.setStatusText("Game over — AI wins.");
            return;
        }

        playerTurn = true;
        view.setShotsEnabled(true);
        view.setStatusText("Your turn");
    }


    private Point pickAiTarget() {
        for (int tries = 0; tries < 500; tries++) {
            int r = rng.nextInt(playerBoard.size());
            int c = rng.nextInt(playerBoard.size());
            Point p = new Point(r, c);
            if (aiTried.add(p)) return p;
        }
        for (int r = 0; r < playerBoard.size(); r++)
            for (int c = 0; c < playerBoard.size(); c++) {
                Point p = new Point(r, c);
                if (!aiTried.contains(p)) { aiTried.add(p); return p; }
            }
        return null;
    }

    private boolean applyShot(Board shooter, Board defender, int r, int c) {
        Cell prev = shooter.cellAt(r, c, GridType.SHOTS);
        if (prev == Cell.HIT || prev == Cell.MISS) return false;

        Cell tgt = defender.cellAt(r, c, GridType.SHIPS);
        if (tgt == Cell.SHIP) {
            shooter.markHit(r, c);
            defender.shipHit(r, c);
        } else {
            shooter.markMiss(r, c);
            defender.shipMiss(r, c);
        }
        return true;
    }

    private boolean allShipsSunk(Board b) {
        int n = b.size();
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                if (b.cellAt(r, c, GridType.SHIPS) == Cell.SHIP) return false;
        return true;
    }
    
    private static String coord(int r, int c) {
        return "" + (char)('A' + r) + (c + 1);
    }

    private boolean isShipAt(Board defender, int r, int c) {
        return defender.cellAt(r, c, GridType.SHIPS) == Cell.SHIP;
    }
}
