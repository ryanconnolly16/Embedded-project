
package battleship;

import battleship.Board;
import battleship.Fleet;
import battleship.Fleet.Ship;
import java.util.*;

public class UI_Output {
    String pieceremove;
    public static void clearConsole() {
        System.out.println("\n".repeat(100));
    }

    public static String Startup(){
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Battle Ship.");
        System.out.println("To shoot a shot type the grid coordinated when prompted, e.g. a3.");
        System.out.println("To quit the game type x.\n\n");
        
        
        System.out.println("Are you playing with one or two people?");
        String preset = Test.getInput(input);  
        return preset;
    }

    public static String usingpreset(){
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to use a preset for the layout of your fleet?(y/n)");
        String preset = Test.getInput(input);  
        return preset;
    }
    
}