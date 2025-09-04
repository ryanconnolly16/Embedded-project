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
    
    
    public String getInput() throws IOException {
        while (true) {
            String input = this.input.nextLine();

            // Handle null input (can happen in some edge cases)
            if (input == null) {
                System.out.println("No input received, try again...");
                continue;
            }

            // Handle empty input
            if (input.trim().isEmpty()) {
                System.out.println("Invalid input, try again...");
                continue; // Use continue instead of recursive call
            }

            // Handle exit command
            if (input.trim().equalsIgnoreCase("x")) {
                System.out.println("Thanks for playing!");

                // Handle save confirmation with separate loop
                while (true) {
                    if(startedGame == 1){
                        System.out.println("Would you like to save the current turn to a file? (y/n)");
                        String saveResponse = this.input.nextLine();

                        if (saveResponse == null) {
                            System.out.println("No input received, please try again.");
                            continue;
                        }

                        String trimmedResponse = saveResponse.trim().toLowerCase();

                        if (trimmedResponse.equals("y")) {
                            SaveManager.keep(autosave);
                            break; // Exit save confirmation loop
                        } else if (trimmedResponse.equals("n")) {
                            SaveManager.discard(autosave);
                            break; // Exit save confirmation loop
                        } else {
                            System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
                            // Continue the save confirmation loop
                        }
                    }
                    else{
                        break;
                    }
                }

                System.exit(0);
                return null; // This line won't be reached, but satisfies compiler
            }

            // Return valid input
            return input.trim();
        }
    }
    
//    //same method name and logic as your original
//    public String getInput() throws IOException {
//        while (true) {
//            String input = this.input.nextLine();
//            if (input.isEmpty()) {
//                System.out.println("Invalid input, try again.............");
//                return getInput();  // Ask for input again
//            }
//            else if (input.trim().equalsIgnoreCase("x")) {
//                System.out.println("Thanks for playing!");
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
//                        return getInput();
//                    }
//                }
//                System.exit(0);
//            }
//            return input;
//        }
//    }
        
        
        
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