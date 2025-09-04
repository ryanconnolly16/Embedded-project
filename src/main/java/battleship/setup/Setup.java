package battleship.setup;

import battleship.fleetplacements.*;
import battleship.domain.Board;
import battleship.interfaces.*;
import battleship.ui.*;
import java.io.IOException;

public class Setup implements GameSetup {
    
    public Setup() {
    }
    //asking the user if they want a preset board or make one
    public void PlayerSetup(Fleet fleet, Board board, String name) throws IOException {
        System.out.println("\n\n" + name + " Setup:");
        String answer = InputManager.AskPreset();
        
        if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
            fleet.Preset(fleet, board);
            System.out.println("\nHere is your board:");
        }
        else if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) {
            fleet.UserPalcement(fleet, board);
        }
        else {
            System.out.println("Invalid answer, try again.");
            PlayerSetup(fleet, board, name);
        }
    }
    
    @Override
    public void setupPlayers() throws IOException {
        //interface implementation 
    }
}