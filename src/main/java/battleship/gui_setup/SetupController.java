package battleship.gui_setup;

import battleship.domain.Board;
import battleship.fleetplacements.Fleet;
import battleship.navigation.Navigator;
import battleship.gui_game.OnePlayerGame;

public class SetupController implements SetupActions {
    private final Navigator nav;
    private final String onePlayerCard, menuCard;
    private final OnePlayerGame oneView;

    private final Board pboard;
    private final Board aiboard;
    private final Fleet pfleet;
    private final Fleet aifleet;

    private boolean presetRunning = false; // re-entry guard
    private boolean presetApplied = false; // placed at least once
    private boolean started       = false; // start() one-shot

    public SetupController(Navigator nav,
                           String onePlayerCard,
                           String menuCard,
                           OnePlayerGame oneView,
                           Board pboard,  Fleet pfleet,
                           Board aiboard, Fleet aifleet) {
        this.nav = nav;
        this.onePlayerCard = onePlayerCard;
        this.menuCard = menuCard;
        this.oneView = oneView;

        this.pboard  = pboard;
        this.pfleet  = pfleet;
        this.aiboard = aiboard;
        this.aifleet = aifleet;
    }

    @Override
    public void applyPreset() {
        if (presetRunning) return;
        presetRunning = true;
        try {
            // If you clear grids, do it here to avoid double-place
            // pboard.clear(); aiboard.clear();
            SetupServices.setupPresetGUI(pfleet,  pboard);
            SetupServices.setupPresetGUI(aifleet, aiboard);
            presetApplied = true;
            oneView.refresh(); // show placed ships
        } finally {
            // keep one-shot; flip back to false if you want to allow re-preset
            // presetRunning = false;
        }
    }

    @Override
    public void loadSave() {
        // TODO: show file chooser / pick last save / etc.
        // Leave gating to the Setup view (it enables Start once user clicked).
    }

    @Override
    public void newGame() {
        // TODO: reset/initialize new save metadata if you keep any.
        // Leave gating to the Setup view.
    }

    @Override
    public void start() {
        if (started) return;
        started = true;
        if (!presetApplied) applyPreset(); // safety
        oneView.setShotsEnabled(true);
        oneView.refresh();
        nav.show(onePlayerCard);
    }

    @Override
    public void back() { nav.show(menuCard); }
}
