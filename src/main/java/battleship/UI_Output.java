
package battleship;

import battleship.Board;
import battleship.Fleet;
import battleship.Fleet.Ship;
import java.io.*;
import java.util.*;

public class UI_Output extends FileInput{
    public static File autosave;
    public static Scanner input = new Scanner(System.in);
    
    
    //will 'clear' the screen between turns for cleaner experience
    public static void clearConsole() {
        System.out.println("\n".repeat(100));
    }

    //intial ouput for the prgram, asks how many people are playing
    public static void Startup() throws IOException{
            System.out.println("Welcome to Battle Ship.");
            System.out.println("To shoot, type the grid coordinate when prompted, e.g. a3.");
            System.out.println("To quit the game, type x.\n\n");
            playeramount();
            
            
    }
    
    public static void playeramount() throws IOException{
        System.out.println("Are you playing with one or two people?");
        String amount = getInput(input).trim();
        
        while(true){
            if(amount.equals("1") || amount.equalsIgnoreCase("one")){
                loadsavedgame("Player", "Ai",1);
                OnePlayer oneplayer = new OnePlayer();
                oneplayer.PlayGame();
            }

            else if(amount.equals("2") || amount.equalsIgnoreCase("two")){
                loadsavedgame("Player1", "Player2",1);
                TwoPlayers twoplayers = new TwoPlayers();
                twoplayers.PlayGame();
            }
            
            else{
                System.out.println("Invalid input, try agin.");
            }
        }
    }
    public static int count = 0;
    public static void loadsavedgame(String player1, String player2, int pplamount) throws IOException{
        
        if(count <1){
            System.out.println("Would you like to use a saved file?(y/n)");
            String usesavefile = getInput(input).trim();
            if(usesavefile.equals("y")){

                int loadingworked = SaveManager.checkfilesexist();
                //1 means it worked
                
                if (loadingworked == 1){
                    System.out.println("\n\nWhich file would you like to use?\n");
                    SaveManager.listSaves();

                    System.out.println("\n\n");
                    String filenum = getInput(input);
                    int filenumber = Integer.parseInt(filenum);

                    FileInput input = new FileInput();

                    Board[] boards;
                    boards = input.loadMatch(new File(SaveManager.getProjectFolderPath("saves") + "/" + SaveManager.filenames.get(filenumber-1)),
                    player1, player2);
                    // save player1 to boards 0 ect...
                    Board player1_new = boards[0];
                    Board player2_new = boards[1];
                    System.out.println(battleship.BoardRenderer.renderBoth(player1_new));
                    System.out.println(battleship.BoardRenderer.renderBoth(player2_new));

                }
                //0 means that there are no files to read from
                else if (loadingworked == 0){
                    System.out.println("No saved files to use.");
                    count = 1;
                    loadsavedgame( player1, player2, pplamount);
                }
            }
            else if (usesavefile.equals("n")){
                if(pplamount == 1){
                    OnePlayer.playersetup();
                }
                else if (pplamount == 2){
                    TwoPlayers.twoplayersetup();
                }
            }
            else{
                System.out.println("Please answer with y or n.");
                loadsavedgame( player1, player2, pplamount);
            }
        }
        else{
            if(pplamount == 1){
                OnePlayer.playersetup();
            }
            else if (pplamount == 2){
                TwoPlayers.twoplayersetup();
            }
        }
    }
    
    
    
    
    public static String usingpreset() throws IOException{
        System.out.println("Would you like to use a preset for the layout, or your own fleet?(y/n)");
        String preset = getInput(input);  
        return preset;
    }
    
    //call this instead of normal scanner object, error handling if user presses enter with nothing written
    //checks if the user presses x to end program
    public static String getInput(Scanner scanner) throws IOException{
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            System.out.println("Invalid input, try again");
            return getInput(scanner);
        }
        else if (input.trim().equalsIgnoreCase("x")) {
            System.out.println("Thanks for playing!");
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
            System.exit(0);
            

        }
        return input;
    }
    
}