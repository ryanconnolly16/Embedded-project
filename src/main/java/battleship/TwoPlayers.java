package battleship;

import battleship.io.SaveManager;
import battleship.ui.BoardRenderer;
import battleship.ui.DefaultGlyphs;
import battleship.domain.Board;
import battleship.enums.Cell;
import battleship.enums.GridType;

import java.io.*;
import java.util.*;

public class TwoPlayers {
    //global variables
    static Board board1 = new Board(10);
    static Board board2 = new Board(10);
    static Fleet fleet1 = new Fleet();
    static Fleet fleet2 = new Fleet();
    
    public static void TwoPlayerSetup() throws IOException {
        PlayerSetup(fleet1, board1, "Player 1");
        PlayerSetup(fleet2, board2, "Player 2");
    }
    
    //set up function asking player if they want to use a preset board or not wioth error handling
    public static void PlayerSetup(Fleet fleet, Board board, String name) throws IOException {
        System.out.println("\n\n" + name + " Setup:");
        String answer = UI_Output.UsingPreset();
        
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
    
    //function for player taking a shot
    public static void PlayerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n" + BoardRenderer.renderBoth(receiverboard, new DefaultGlyphs()));
        System.out.println("\bWhere would you like to shoot?:");
        String usershot = UI_Output.GetInput(input);
        
        //checking input format is correct
        if (usershot.length() < 1 || !Character.isLetter(usershot.charAt(0)) || !Character.isDigit(usershot.charAt(1))) {
            System.out.println("Invalid shot, try again.");
            PlayerShoot(shooterboard, receiverfleet, receiverboard);
            return;
        }
        
        //getting x and y position
        int xpos = Character.toLowerCase(usershot.charAt(0)) - 'a';
        int ypos = 0;
        if (usershot.length() == 3) {
            if (Character.toLowerCase(usershot.charAt(2) - '1') + 1 > 0) {
                System.out.println("Shot is out of bounds, try again");
                PlayerShoot(shooterboard, receiverfleet, receiverboard);
            }
            else {
                ypos = 9;
            }
        }
        else {
            ypos = (usershot.charAt(1) - '1');
        }
        
        //checking if out of bounds 
        if (xpos < 0 || ypos < 0) {
            System.out.println("Shot out of bounds, try again.");
            PlayerShoot(shooterboard, receiverfleet, receiverboard);
            return;
        }
        
        //checking chosen cell state
        Cell trial_shot = receiverboard.cellAt(ypos, xpos, GridType.SHIPS);
        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
            System.out.println("\n\nShot already taken there.");
            System.out.println("Try again.\n\n");
            PlayerShoot(shooterboard, receiverfleet, receiverboard);
        }
        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
            UI_Output.ClearConsole();
            Battle.usershot(usershot, shooterboard, receiverfleet, receiverboard);
        }
    }
    
    //will alternate between player one and two until either fleet is all sunk
    public void PlayGame() throws IOException {
        while (!fleet1.allSunk() || !fleet2.allSunk()) {
            UI_Output.ClearConsole();
            System.out.println("Player one's turn:");
            PlayerShoot(board1, fleet2, board2);
            UI_Output.ClearConsole();
            System.out.println("Player two's turn:");
            PlayerShoot(board2, fleet1, board1);
            UI_Output.autosave = SaveManager.writeTurnAutosave(board1, board2);
        }
    }
}
