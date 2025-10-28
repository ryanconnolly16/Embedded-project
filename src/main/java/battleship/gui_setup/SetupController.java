// battleship/gui_setup/SetupController.java
package battleship.gui_setup;

import battleship.domain.Board;
import battleship.fleetplacements.Fleet;
import battleship.navigation.Navigator;
import battleship.gui_game.OnePlayerGame;
import battleship.gui_game.OnePlayerController;

public class SetupController implements SetupActions {
    private final Navigator nav;
    private final String onePlayerCard;
    private final String menuCard;
    private final OnePlayerGame oneView;  // <-- pass the view in

    // Keep these so applyPreset() is usable later by start()
    public static Board pboard  = new Board(10);
    public static Board aiboard = new Board(10);
    public static Fleet pfleet  = new Fleet();
    public static Fleet aifleet = new Fleet();

    private boolean presetApplied = false;

    public SetupController(Navigator nav, String onePlayerCard, String menuCard, OnePlayerGame oneView) {
        this.nav = nav;
        this.oneView = oneView;
        this.onePlayerCard = onePlayerCard;
        this.menuCard = menuCard;
    }

    @Override
    public void applyPreset() {
        // If you really have a static button reference, keep it; otherwise expose a method on your Setup panel.
        // Setup.preset.setEnabled(false);

        SetupServices.setupPresetGUI(pfleet, pboard);
        SetupServices.setupPresetGUI(aifleet, aiboard);
        presetApplied = true;
        Setup.preset.setEnabled(false);
        // Optional preview in setup screen:
        // oneView.setModel(pboard);
        // oneView.refresh();
    }

    @Override
    public void start() {
        // Safety: ensure boards are populated
        if (!presetApplied) applyPreset();

        // Create the game controller using the boards produced by applyPreset()
        new OnePlayerController(oneView, pboard, aiboard);

        // (Controller already sets model + refresh on the view, but harmless to call again)
        oneView.refresh();

        // Navigate to the 1P game screen
        nav.show(onePlayerCard);
    }

    @Override
    public void back() {
        nav.show(menuCard);
    }
}
