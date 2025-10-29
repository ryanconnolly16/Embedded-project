package battleship.playinggame;

import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.enums.*;
import battleship.interfaces.*;
import battleship.ui.*;
import java.io.IOException;
import java.util.Scanner;

public class Shooting implements PlayerShooter {
    public static String logresult = null;
    
    
    public Shooting() {
    }
    //function to take in where the user wants to shooter, will convert from string to int, check that shot hasnt already been done, then shoot
    public String playershoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n" + BoardRenderer.renderBoth(shooterboard, new DefaultGlyphs()));
        System.out.println("\bWhere would you like to shoot?:");
        String usershot = InputManager.getInput(input);
        String result = null;
        
        
        
        
        if (usershot.length() < 1 || !Character.isLetter(usershot.charAt(0)) || !Character.isDigit(usershot.charAt(1))) {
            System.out.println("Invalid shot, try again.");
            result = "bad1";
            playershoot(shooterboard, receiverfleet, receiverboard);
        }
        
        int xpos = Character.toLowerCase(usershot.charAt(0)) - 'a';
        int ypos = 0;
        if (usershot.length() == 3) {
            if (Character.toLowerCase(usershot.charAt(2) - '1') + 1 > 0) {
                System.out.println("Shot is out of bounds, try again");
                result = "bad2";
                playershoot(shooterboard, receiverfleet, receiverboard);
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
            result = "bad3";
            playershoot(shooterboard, receiverfleet, receiverboard);
        }
        
        
        
        String xposis = Character.toString(usershot.charAt(0));
        String yposis = String.valueOf(ypos);
        String pos = (xposis + yposis);
        
        Cell trial_shot = receiverboard.cellAt(ypos, xpos, GridType.SHIPS);
        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
            System.out.println("\n\nShot already taken there.");
            System.out.println("Try again.\n\n");
            playershoot(shooterboard, receiverfleet, receiverboard);
        }
        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
            Console.clearConsole();
            if (trial_shot == Cell.SHIP){result = (pos +",hit");}
            if(trial_shot == Cell.WATER){result = (pos +",miss");}
            Battle.usershot(usershot, shooterboard, receiverfleet, receiverboard);
            
            
        }
        return result;
    }
    
    
    
    
    
    
    public static boolean playershooting(int ypos, int xpos, Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException {
        String x = String.valueOf((char)('a' + (xpos)));
        String y = Integer.toString(ypos+1);
        String usershot = x + y;
        System.out.println(usershot);
        
        Cell trial_shot = receiverboard.cellAt(ypos, xpos, GridType.SHIPS);
        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
            return false;
            
        }
        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
            
            char letterxpos = (char)('A' + xpos);
            String usershots = "" + letterxpos + y;
            logresult = ("\nYou fired at " + usershots + " - ");

            Battle.usershot(usershot, shooterboard, receiverfleet, receiverboard);
            return true;
            
        }
        return false;
    }
}