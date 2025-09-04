package battleship.playinggame;

import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.enums.*;
import battleship.interfaces.*;
import battleship.ui.*;
import java.io.IOException;
import java.util.Scanner;

public class Shooting implements PlayerShooter {
    
    public Shooting() {
    }
    //function to take in where the user wants to shooter, will convert from string to int, check that shot hasnt already been done, then shoot
    public void playerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n" + BoardRenderer.renderBoth(shooterboard, new DefaultGlyphs()));
        System.out.println("\bWhere would you like to shoot?:");
        String usershot = InputManager.GetInput(input);
        
        if (usershot.length() < 1 || !Character.isLetter(usershot.charAt(0)) || !Character.isDigit(usershot.charAt(1))) {
            System.out.println("Invalid shot, try again.");
            playerShoot(shooterboard, receiverfleet, receiverboard);
            return;
        }
        
        int xpos = Character.toLowerCase(usershot.charAt(0)) - 'a';
        int ypos = 0;
        if (usershot.length() == 3) {
            if (Character.toLowerCase(usershot.charAt(2) - '1') + 1 > 0) {
                System.out.println("Shot is out of bounds, try again");
                playerShoot(shooterboard, receiverfleet, receiverboard);
            }
            else {
                ypos = 9;
            }
        }
        else {
            ypos = (usershot.charAt(1) - '1');
        }
        
        if (xpos < 0 || ypos < 0) {
            System.out.println("Shot out of bounds, try again.");
            playerShoot(shooterboard, receiverfleet, receiverboard);
            return;
        }
        
        Cell trial_shot = receiverboard.cellAt(ypos, xpos, GridType.SHIPS);
        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
            System.out.println("\n\nShot already taken there.");
            System.out.println("Try again.\n\n");
            playerShoot(shooterboard, receiverfleet, receiverboard);
        }
        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
            Console.ClearConsole();
            Battle.usershot(usershot, shooterboard, receiverfleet, receiverboard);
        }
    }
}