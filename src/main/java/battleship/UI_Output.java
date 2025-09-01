
package battleship;

import battleship.Board;
import battleship.Fleet;
import battleship.Fleet.Ship;
import java.util.*;

public class UI_Output {
    //will 'clear' the screen between turns for cleaner experience
    public static void clearConsole() {
        System.out.println("\n".repeat(100));
    }

    //intial ouput for the prgram, asks how many people are playing
    public static String Startup(int count){
        Scanner input = new Scanner(System.in);
        if(count <1){
            System.out.println("Welcome to Battle Ship.");
            System.out.println("To shoot a shot type the grid coordinated when prompted, e.g. a3.");
            System.out.println("To quit the game type x.\n\n");
        }
        
        System.out.println("Are you playing with one or two people?");
        String preset = getInput(input);  
        return preset;
    }
    
    public static String usingpreset(){
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to use a preset for the layout of your fleet?(y/n)");
        String preset = getInput(input);  
        return preset;
    }
    
    //call this instead of normal scanner object, error handling if user presses enter with nothing written
    //checks if the user presses x to end program
    public static String getInput(Scanner scanner){
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            System.out.println("Invalid input, try again");
            getInput(scanner);
        }
        else if (input.trim().equalsIgnoreCase("x")) {
            System.out.println("Thanks for playing!");
            System.exit(0);
        }
        return input;
    }
    
}