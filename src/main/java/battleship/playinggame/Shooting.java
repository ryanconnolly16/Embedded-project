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
    
    //match your exact method signature and logic
    public void playerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n" + BoardRenderer.renderBoth(receiverboard, new DefaultGlyphs()));
        System.out.println("\bWhere would you like to shoot?:");
        String usershot = InputManager.GetInput(input);
        
        //checking input format is correct - same as your code
        if (usershot.length() < 1 || !Character.isLetter(usershot.charAt(0)) || !Character.isDigit(usershot.charAt(1))) {
            System.out.println("Invalid shot, try again.");
            playerShoot(shooterboard, receiverfleet, receiverboard);
            return;
        }
        
        //getting x and y position - same as your code
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
        
        //checking if out of bounds - same as your code
        if (xpos < 0 || ypos < 0) {
            System.out.println("Shot out of bounds, try again.");
            playerShoot(shooterboard, receiverfleet, receiverboard);
            return;
        }
        
        //checking chosen cell state - same as your code
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