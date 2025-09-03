package battleship.players;

import battleship.Fleet;
import battleship.UI_Output;
import battleship.playinggame.Shooting;
import battleship.setup.Setup;

import battleship.domain.Board;
import battleship.io.SaveManager;
import java.io.IOException;

// OnePlayer as standalone class - no inheritance confusion
public class OnePlayer {
    //simple naming like your original - no parent class variables to worry about
    private Board pboard;
    private Fleet pfleet;
    private Board aiboard;
    private Fleet aifleet;
    
    //handlers for SOLID design
    private Setup setup;
    private Shooting shooting;
    private Ai ai;
    
    public OnePlayer() {
        //clean and simple - no parent variables to set
        pboard = new Board(10);
        pfleet = new Fleet();
        aiboard = new Board(10);
        aifleet = new Fleet();
        
        //create handlers
        setup = new Setup();
        shooting = new Shooting();
        ai = new Ai(pboard, aiboard, pfleet);
    }
    
    //same method name as your original
    public void OnePlayerSetup() throws IOException {
        setup.PlayerSetup(pfleet, pboard, "Player");
        AiSetup(aifleet, aiboard);
    }
    
    //same method name as your original
    public static void AiSetup(Fleet aifleet, Board aiboard) {
        aifleet.Preset(aifleet, aiboard);
    }
    
    //same method name as your original - but no inheritance issues
    public void PlayerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        shooting.playerShoot(shooterboard, receiverfleet, receiverboard);
    }
    
    //same method name as your original
    public void PlayGame() throws IOException {
        //match your while loop structure exactly
        while (!pfleet.allSunk() || !aifleet.allSunk()) {
            ai.aiShoot();
            PlayerShoot(pboard, aifleet, aiboard);
            UI_Output.autosave = SaveManager.writeTurnAutosave(pboard, aiboard);
        }
    }
    
    //same method name as your original
    public static void AiShoot() {
        //kept for compatibility
        System.out.println("Use instance method aiHandler.aiShoot() instead");
    }
    
}