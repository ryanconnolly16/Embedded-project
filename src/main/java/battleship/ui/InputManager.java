package battleship.ui;

import battleship.interfaces.*;
import battleship.io.SaveManager;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;

// Single Responsibility: User input handling only
public class InputManager implements UserInput {
    //same variable names as your original
    public static Scanner input = new Scanner(System.in);
    public static int startedGame = 0;
    public static File autosave;
    
    public InputManager() {
        //simple constructor
    }
    
    //same method name and logic as your original
    public String getInput() throws IOException {
        while (true) {
            String input = this.input.nextLine();
            if (input.isEmpty()) {
                System.out.println("Invalid input, try again.............");
                return getInput();  // Ask for input again
            }
            else if (input.trim().equalsIgnoreCase("x")) {
                System.out.println("Thanks for playing!");
                System.out.println("Would you like to save the current turn to a file?(y/n)");
                String savetheturn = this.input.nextLine();
                while(true){
                    if (savetheturn.equals("y")){
                        SaveManager.keep(autosave);
                        break;
                    }
                    else if(savetheturn.equals("n")){
                        SaveManager.discard(autosave);
                        break;
                    }
                    else {  
                        System.out.println("Invalid, only input y or n please.");
                        savetheturn = this.input.nextLine();
                    }
                }
                System.exit(0);
            }
            return input;
        }
    }
        
        
        
//        String input = this.input.nextLine();
//        if (input.isEmpty()) {
//            System.out.println("Invalid input, try again");
//            return getInput();
//        }
//        else if (input.trim().equalsIgnoreCase("x")) {
//            System.out.println("Thanks for playing!");
//            if (startedGame == 1){
//                System.out.println("Would you like to save the current turn to a file?(y/n)");
//                String savetheturn = this.input.nextLine();
//                while(true){
//                    if (savetheturn.equals("y")){
//                        SaveManager.keep(autosave);
//                        break;
//                    }
//                    else if(savetheturn.equals("n")){
//                        SaveManager.discard(autosave);
//                        break;
//                    }
//                    else {  
//                        System.out.println("Invalid, only input y or n please.");
//                        savetheturn = this.input.nextLine();
//                    }
//                }
//            }
//            System.exit(0);
//        }
//        return input;
    
    
    public static String AskPreset() throws IOException{
        InputManager input = new InputManager();
        return input.askPreset();
        
    }
    
    
    //same method name as your original
    public String askPreset() throws IOException {
        System.out.println("Would you like to use a preset for the layout?(y/n)");
        String preset = getInput();  
        return preset;
    }
    
    //static method like your style
    public static String GetInput(Scanner scanner) throws IOException {
        InputManager handler = new InputManager();
        return handler.getInput();
    }
}