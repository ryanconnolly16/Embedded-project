package battleship.gui_setup;

import battleship.BattleshipGUI;
import battleship.database.Db;
import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;
import battleship.navigation.Navigator;
import battleship.gui_game.OnePlayerGame;
import battleship.io.FileInput;
import battleship.io.SaveManager;

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
    
    private boolean presetRunning   = false; 
    private boolean savedRunning    = false;
    private boolean started         = false;

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
         
            SetupServices.setupPresetGUI(BattleshipGUI.playerFleet,  BattleshipGUI.playerBoard);
            SetupServices.setupPresetGUI(BattleshipGUI.aiFleet, BattleshipGUI.aiBoard);
            
            oneView.refresh(); // show placed ships
            
            try (Connection d = Db.connect()) {
                Db.ensureSchema(d);
                Db.clearOnStartup(d);
            }catch (SQLException ex) {    
                Logger.getLogger(SetupController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } finally {
        }
    }

    @Override
    public void loadSave() {
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
            BattleshipGUI.playerFleet.repopulateFromBoard("player");
            
            
            
            // Sync aivisited array with the board's SHOTS grid to match actual shot history
            for (int r = 0; r < 10; r++) {
                for (int col = 0; col < 10; col++) {
                    // Mark as visited if the AI has already shot at this location (HIT or MISS on SHOTS grid)
                    Cell shotCell = BattleshipGUI.aiBoard.cellAt(r, col, GridType.SHOTS);
                    aivisited[r][col] = (shotCell == Cell.HIT || shotCell == Cell.MISS);
                }
            }
            
            BattleshipGUI.aiFleet.repopulateFromBoard("ai");

            // Recount hits so allSunk/health is consistent with saved shots:
            BattleshipGUI.playerFleet.syncHitsFromBoard(BattleshipGUI.playerBoard);
            BattleshipGUI.aiFleet.syncHitsFromBoard(BattleshipGUI.aiBoard);
            
            
            oneView.setModel(BattleshipGUI.playerBoard);
           
            
        } catch (SQLException | IOException ex) {
            Logger.getLogger(SetupController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Ensure model is set and refresh to show all hits/misses from loaded game
        oneView.setModel(BattleshipGUI.playerBoard);
        oneView.refresh(); // show placed ships and shots
        // Force repaint to ensure GUI updates
        oneView.revalidate();
        oneView.repaint();      
    }


    @Override
    public void start() {
        if (started) return;
        started = true;
        
        // Ensure model is up to date
        oneView.setModel(BattleshipGUI.playerBoard);
        oneView.setShotsEnabled(true);
        nav.show(onePlayerCard);
        
        // Refresh after showing the card to ensure view is visible and all hits/misses are displayed
        oneView.refresh();
        
        // Force repaint to ensure all loaded hits/misses are displayed
        javax.swing.SwingUtilities.invokeLater(() -> {
            oneView.revalidate();
            oneView.repaint();
        });
    }

    @Override
    public void back() { nav.show(menuCard); }
}
