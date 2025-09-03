
package battleship;

import battleship.io.FileInput;
import battleship.io.SaveManager;
import battleship.domain.Board;
import java.io.*;
import java.util.*;

public class UI_Outputold extends FileInput{
    public static File autosave;
    public static Scanner input = new Scanner(System.in);
    
    
    //will 'clear' the screen between turns for cleaner experience
    public static void ClearConsole() {
        System.out.println("\n".repeat(100));
    }

    //intial ouput for the prgram, asks how many people are playing
    public static void StartUp() throws IOException{
            System.out.println("Welcome to Battle Ship.");
            System.out.println("To shoot, type the grid coordinate when prompted, e.g. a3.");
            System.out.println("To quit the game, type x.\n\n");
            PlayerAmount();
    }
    
    public static int startedGame = 0;
    public static void PlayerAmount() throws IOException{
        System.out.println("Are you playing with one or two people?");
        String amount = GetInput(input).trim();
        while(true){
            if(amount.equals("1") || amount.equalsIgnoreCase("one")){
                LoadSavedGame(1);
                OnePlayer oneplayer = new OnePlayer();
                startedGame = 1;
                oneplayer.PlayGame();
            }
            else if(amount.equals("2") || amount.equalsIgnoreCase("two")){
                LoadSavedGame(2);
                TwoPlayers twoplayers = new TwoPlayers();
                startedGame = 1;
                twoplayers.PlayGame();
            }
            else{
                System.out.println("Invalid input, try agin.");
                PlayerAmount();
                return;
            }
        }
    }
    
    public static int count = 0;
    public static int chosefilenum = 0;
    public static void LoadSavedGame(int pplamount) throws IOException{
        if(count <1){
            System.out.println("Would you like to use a saved file?(y/n)");
            String usesavefile = GetInput(input).trim();
            if(usesavefile.equals("y")){
                int loadingworked = SaveManager.checkfilesexist();
                //1 means it worked
                if (loadingworked == 1){
                    if (chosefilenum < 1){
                        System.out.println("\n\nWhich file would you like to use?\n");
                    }
                    SaveManager.listSaves();

                    System.out.println("\n\n");
                    String filenum = GetInput(input);
                    for (char c : filenum.toCharArray()) {
                        if (!Character.isDigit(c)) {
                            System.out.println("Please input a file number.");
                            chosefilenum = 1;
                            LoadSavedGame(pplamount);
                        }
                    }
                    int filenumber = Integer.parseInt(filenum);
                    
                    if(SaveManager.filenames.size() < filenumber || filenumber <1){
                        System.out.println("Please input a actual file number");
                        chosefilenum = 1;
                        LoadSavedGame(pplamount);
                    }
                    
                    FileInput input = new FileInput();
                    
                    Board[] boards;
                    boards = input.loadMatch(new File(SaveManager.getProjectFolderPath("saves") + "/" + SaveManager.filenames.get(filenumber-1)));
                    if (pplamount == 1){
                        OnePlayer.pboard = boards[0];
                        OnePlayer.aiboard = boards[1];
                    }
                    if (pplamount == 2){
                        OnePlayer.board1 = boards[0];
                        OnePlayer.board2 = boards[1];
                    }
                    
                }
                //0 means that there are no files to read from
                else if (loadingworked == 0){
                    System.out.println("No saved files to use.");
                    count = 1;
                    LoadSavedGame(pplamount);
                }
            }
            else if (usesavefile.equals("n")){
                if(pplamount == 1){
                    OnePlayer.OnePlayerSetup();
                }
                else if (pplamount == 2){
                    TwoPlayers.TwoPlayerSetup();
                }
            }
            else{
                System.out.println("Please answer with y or n.");
                LoadSavedGame(pplamount);
            }
        }
        else{
            if(pplamount == 1){
                OnePlayer.OnePlayerSetup();
            }
            else if (pplamount == 2){
                TwoPlayers.TwoPlayerSetup();
            }
        }
    }
    
    public static String UsingPreset() throws IOException{
        System.out.println("Would you like to use a preset for the layout?(y/n)");
        String preset = GetInput(input);  
        return preset;
    }
    
    //call this instead of normal scanner object, error handling if user presses enter with nothing written
    //checks if the user presses x to end program
    public static String GetInput(Scanner scanner) throws IOException{
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            System.out.println("Invalid input, try again");
            return GetInput(scanner);
        }
        else if (input.trim().equalsIgnoreCase("x")) {
            System.out.println("Thanks for playing!");
            if (startedGame == 1){
                System.out.println("Would you like to save the current turn to a file?(y/n)");
                String savetheturn = scanner.nextLine();
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
                        savetheturn = scanner.nextLine();
                    }
                }
            }
            System.exit(0);
        }
        return input;
    }
    
}