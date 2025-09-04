package battleship.ui;

import battleship.interfaces.*;
import battleship.io.SaveManager;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;

//function that is called whenever there is input to check whether it is an x- they want to quit the game
public class InputManager implements UserInput {
    public static Scanner input = new Scanner(System.in);
    public static int startedGame = 0;
    public static File autosave;
    
    public InputManager() {
    }
    
    
    public String getInput() throws IOException {
        while (true) {
            String input = this.input.nextLine();
            if (input == null) {
                System.out.println("No input received, try again...");
                continue;
            }
            if (input.trim().isEmpty()) {
                System.out.println("Invalid input, try again...");
                continue;
            }
            if (input.trim().equalsIgnoreCase("x")) {
                System.out.println("Thanks for playing!");
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
                            break;
                        } else if (trimmedResponse.equals("n")) {
                            SaveManager.discard(autosave);
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
                        }
                    }
                    else{
                        break;
                    }
                }

                System.exit(0);
                return null;
            }
            return input.trim();
        }
    }
    
    
    public static String AskPreset() throws IOException{
        InputManager input = new InputManager();
        return input.askPreset();
        
    }
    public String askPreset() throws IOException {
        System.out.println("Would you like to use a preset for the layout?(y/n)");
        String preset = getInput();  
        return preset;
    }
    public static String GetInput(Scanner scanner) throws IOException {
        InputManager handler = new InputManager();
        return handler.getInput();
    }
}