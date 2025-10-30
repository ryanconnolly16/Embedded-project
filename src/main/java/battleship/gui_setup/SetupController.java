package battleship.gui_setup;

import battleship.BattleshipGUI;
import battleship.database.Db;
import battleship.domain.Board;
import battleship.fleetplacements.Fleet;
import battleship.navigation.Navigator;
import battleship.gui_game.OnePlayerGame;
import battleship.io.FileInput;
import battleship.io.SaveManager;
import battleship.players.OnePlayer;
import battleship.ui.BoardRenderer;
import battleship.ui.DefaultGlyphs;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupController implements SetupActions {
    private final Navigator nav;
    private final String onePlayerCard, menuCard;
    private final OnePlayerGame oneView;

//    public static Board pboard  = BattleshipGUI.playerBoard;
//    public static Board aiboard = BattleshipGUI.aiBoard;
//    public static Fleet pfleet  = BattleshipGUI.playerFleet;
//    public static Fleet aifleet = BattleshipGUI.aiFleet;

    private boolean presetRunning = false; // re-entry guard
    private boolean presetApplied = false; // placed at least once
    private boolean savedRunning = false;
    private boolean savedApplied  = false;
    private boolean started       = false; // start() one-shot

    public static boolean[][] playervisited = new boolean[10][10];
    public static boolean[][] aivisited = new boolean[10][10];
    
    
    public SetupController(Navigator nav,
                           String onePlayerCard,
                           String menuCard,
                           OnePlayerGame oneView) {
        this.nav = nav;
        this.onePlayerCard = onePlayerCard;
        this.menuCard = menuCard;
        this.oneView = oneView;

    }

    @Override
    public void applyPreset() {
        if (presetRunning) return;
        presetRunning = true;
        try {
            // If you clear grids, do it here to avoid double-place
            // pboard.clear(); aiboard.clear();
            
            
            SetupServices.setupPresetGUI(BattleshipGUI.playerFleet,  BattleshipGUI.playerBoard);
            SetupServices.setupPresetGUI(BattleshipGUI.aiFleet, BattleshipGUI.aiBoard);
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
        if (savedRunning) return;
        savedRunning = true;
        
        try (Connection c = Db.connect()) {
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT content FROM GameState WHERE slot=?")) {
                Path outFile = Path.of("saves","export.txt");
                ps.setString(1, "current");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        try (Reader r = rs.getCharacterStream(1);
                            Writer w = Files.newBufferedWriter(outFile, StandardCharsets.UTF_8)) {
                          r.transferTo(w);
                        } catch (IOException ex) {
                            Logger.getLogger(SetupController.class.getName()).log(Level.SEVERE, null, ex);
                        }    
                    }
                }
            }

            FileInput input = new FileInput();

            Board[] boards;
            boards = input.loadMatch(new File(SaveManager.getProjectFolderPath("saves"),"export.txt"));

            BattleshipGUI.playerBoard = boards[0];
            BattleshipGUI.aiBoard = boards[1];
            
            
            for (int r = 0; r < 10; r++) {
                for (int col = 0; col < 10; col++) {
                    playervisited[r][col] = false;
                }
            }
            // Rebuild fleets to match those boards:
            System.out.println("player");
            BattleshipGUI.playerFleet.repopulateFromBoard(BattleshipGUI.playerBoard, playervisited);
            
            
            
            for (int r = 0; r < 10; r++) {
                for (int col = 0; col < 10; col++) {
                    aivisited[r][col] = false;
                }
            }
            System.out.println("ai");
            BattleshipGUI.aiFleet.repopulateFromBoard(BattleshipGUI.aiBoard, aivisited);

            
            
            
            
            // Recount hits so allSunk/health is consistent with saved shots:
            BattleshipGUI.playerFleet.syncHitsFromBoard(BattleshipGUI.playerBoard);
            BattleshipGUI.aiFleet.syncHitsFromBoard(BattleshipGUI.aiBoard);
            
            
            oneView.setModel(BattleshipGUI.playerBoard);
            //--------------//--------------//--------------//--------------//--------------//--------------
//            System.out.println("player\n" + BoardRenderer.renderBoth(BattleshipGUI.playerBoard, new DefaultGlyphs()));
//            System.out.println("ai\n" + BoardRenderer.renderBoth(BattleshipGUI.aiBoard, new DefaultGlyphs()));
            
            
        } catch (SQLException ex) {
            Logger.getLogger(SetupController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SetupController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        savedApplied = true;
        oneView.refresh(); // show placed ships
        
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
        oneView.setShotsEnabled(true);
        oneView.refresh();
        nav.show(onePlayerCard);
    }

    @Override
    public void back() { nav.show(menuCard); }
}
