package battleship.ui;
import battleship.interfaces.*;

public class Console implements ConsoleOutput {
    
    public Console() {
    }
    
    public static void ClearConsole(){
        Console console = new Console();
        console.clearConsole();
    }
    
    //same method name as your original
    public void clearConsole() {
        System.out.println("\n".repeat(100));
    }
    
    @Override
    public void showWelcome() {
        System.out.println("Welcome to Battle Ship.");
        System.out.println("To shoot, type the grid coordinate when prompted, e.g. a3.");
        System.out.println("To quit the game, type x.\n\n");
    }
}