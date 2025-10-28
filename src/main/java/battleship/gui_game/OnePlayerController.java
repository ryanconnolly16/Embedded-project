package battleship.gui_game;

import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.fleetplacements.Fleet;
import battleship.gui_setup.SetupController;
import battleship.players.Ai;
import battleship.playinggame.Battle;
import battleship.playinggame.Shooting;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OnePlayerController implements OnePlayerActions {
    private static final int AI_DELAY_MS = 650;

    private final OnePlayerGame view; 

    public Board playerBoard = SetupController.pboard;
    public Fleet playerFleet = SetupController.pfleet;
    public Board aiBoard = SetupController.aiboard;
    public Fleet aiFleet = SetupController.aifleet;
    
    
    private final Random rng = new Random();
    private final Set<Point> aiTried = new HashSet<>();
    private boolean playerTurn = true;

    public OnePlayerController(OnePlayerGame view, Board playerBoard, Board aiBoard) {
        this.view = view;
        this.view.setActions(this);
        this.view.setModel(playerBoard);
        this.view.refresh();
        this.view.setShotsEnabled(true);
    }

    @Override
    public void fireShot(int r, int c) {
        if (!playerTurn) return;
        try {
            if (!Shooting.playershooting(r, c, playerBoard, aiFleet, aiBoard)) return;
        } catch (IOException ex) {
            Logger.getLogger(OnePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        view.refresh();

        if (allShipsSunk(aiBoard)) {
            JOptionPane.showMessageDialog(view, "You win!");
            view.setShotsEnabled(false);
            return;
        }

        playerTurn = false;
        view.setShotsEnabled(false);

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
        Ai.AiShot(aiBoard, playerFleet, playerBoard);
        view.refresh();

        if (allShipsSunk(playerBoard)) {
            JOptionPane.showMessageDialog(view, "AI wins!");
            view.setShotsEnabled(false);
            return;
        }

        playerTurn = true;
        view.setShotsEnabled(true);
    }

    

    private boolean allShipsSunk(Board b) {
        int n = b.size();
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                if (b.cellAt(r, c, GridType.SHIPS) == Cell.SHIP) return false;
        return true;
    }
}
