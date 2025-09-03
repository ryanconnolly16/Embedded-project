
package battleship.ui;

import battleship.io.*;
import battleship.playinggame.*;
import java.io.*;
import java.util.*;

public class UI_Output extends FileInput {
    //same static variables as your original
    public static File autosave;
    public static Scanner input = new Scanner(System.in);
    
    //components - match your simple naming
    private static Console console;
    private static InputManager inputmanager;
    private static GameLauncher gameLauncher;
    private static LoadGame loadgame;
    private static Startup startup;
    
    static {
        //initialize components like your style
        console = new Console();
        inputmanager = new InputManager();
        gameLauncher = new GameLauncher();
        loadgame = new LoadGame();
        startup = new Startup();
    }
    
    //same method names as your original - delegates to handlers
    public static void ClearConsole() {
        console.clearConsole();
    }

    public static void StartUp() throws IOException {
        startup.startup();
    }
    
    public static void PlayerAmount() throws IOException {
        gameLauncher.playerAmount();
    }
    
    public static void LoadSavedGame(int pplamount) throws IOException {
        loadgame.loadSavedGame(pplamount);
    }
    
    public static String UsingPreset() throws IOException {
        return inputmanager.askPreset();
    }
    
    public static String GetInput(Scanner scanner) throws IOException {
        return inputmanager.getInput();
    }
    
    //keep your static variables accessible
    public static int startedGame = 0;
    public static int count = 0;
    public static int chosefilenum = 0;
    
    static {
        //sync static variables with handlers
        startedGame = InputManager.startedGame;
        count = LoadGame.count;
        chosefilenum = LoadGame.chosefilenum;
    }
}