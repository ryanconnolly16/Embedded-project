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

        this.pboard  = pboard;   // shared
        this.pfleet  = pfleet;   // shared
        this.aiboard = aiboard;  // shared
        this.aifleet = aifleet;  // shared
    }

    @Override
    public void applyPreset() {
        if (presetRunning) return;   // ignore double-clicks
        presetRunning = true;        // flip BEFORE doing work

        try {
            // If needed: clear boards first to avoid “double place”
            // pboard.clear(); aiboard.clear();

            SetupServices.setupPresetGUI(pfleet,  pboard);
            SetupServices.setupPresetGUI(aifleet, aiboard);

            presetApplied = true;
            oneView.refresh();       // show placed ships
        } finally {
            // Keep it one-shot; if you want to allow re-preset, set back to false.
            // presetRunning = false;
        }
    }

    @Override
    public void start() {
        if (started) return;         // one-shot
        started = true;
        if (!presetApplied) applyPreset();
        oneView.setShotsEnabled(true);
        oneView.refresh();
        nav.show(onePlayerCard);
    }

    @Override
    public void back() { nav.show(menuCard); }
}
