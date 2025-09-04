package battleship.io;

import battleship.domain.*;
import battleship.interfaces.*;
import battleship.players.*;
import battleship.ui.*;
import java.io.*;

//fucntion to check whether the user would like to use a saved file or start a new game
public class LoadGame implements LoadGameImplement {
    
    public static int count = 0;
    public static int chosefilenum = 0;
    
    private final InputManager input;
    
    public LoadGame() {
        input = new InputManager();
    }
    
    
    public void loadsavedgame(int pplamount) throws IOException {
        if(count <1){
            System.out.println("Would you like to use a saved file?(y/n)");
            String usesavefile = input.getinput().trim();
            if(usesavefile.equals("y")){
                int loadingworked = SaveManager.checkfilesexist();
                //checks where there are files to be used
                if (loadingworked == 1){
                    if (chosefilenum < 1){
                        System.out.println("\n\nWhich file would you like to use?\n");
                    }
                    SaveManager.listSaves();

                    System.out.println("\n\n");
                    String filenum = input.getinput();
                    for (char c : filenum.toCharArray()) {
                        if (!Character.isDigit(c)) {
                            System.out.println("Please input a file number.");
                            chosefilenum = 1;
                            loadsavedgame(pplamount);
                        }
                    }
                    int filenumber = Integer.parseInt(filenum);
                    
                    if(SaveManager.filenames.size() < filenumber || filenumber <1){
                        System.out.println("Please input a actual file number");
                        chosefilenum = 1;
                        loadsavedgame(pplamount);
                    }
                    
                    FileInput input = new FileInput();
                    
                    Board[] boards;
                    boards = input.loadMatch(new File(SaveManager.getProjectFolderPath("saves") + "/" + SaveManager.filenames.get(filenumber-1)));
                    if (pplamount == 1){
                        OnePlayer.pboard = boards[0];
                        OnePlayer.aiboard = boards[1];
                    }
                    if (pplamount == 2){
                        TwoPlayers.board1 = boards[0];
                        TwoPlayers.board2 = boards[1];
                    }
                    
                }
                //there are no files to read from
                else if (loadingworked == 0){
                    System.out.println("No saved files to use.");
                    count = 1;
                    loadsavedgame(pplamount);
                }
            }
            else if (usesavefile.equals("n")){
                return;
            }
            else{
                System.out.println("Please answer with y or n.");
                loadsavedgame(pplamount);
            }
        }
        else{
            //will go to this instead of reoutputting the would you like to use a saved file if there are no files to be used
            if(pplamount == 1){
                OnePlayer.onePlayerSetup();
            }
            else if (pplamount == 2){
                TwoPlayers.twoPlayerSetup();
            }
        }
    }
}