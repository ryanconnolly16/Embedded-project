
package battleship.ui;

import battleship.io.*;
import battleship.playinggame.*;
import java.io.*;
import java.util.*;

public class UiOutput extends FileInput {
    public static File autosave;
    public static Scanner input = new Scanner(System.in);
    
    private static Console console;
    private static InputManager inputmanager;
    private static GameLauncher gameLauncher;
    private static LoadGame loadgame;
    private static Startup startup;
    
    static {
        console = new Console();
        inputmanager = new InputManager();
        gameLauncher = new GameLauncher();
        loadgame = new LoadGame();
        startup = new Startup();
    }
    //static methods to call all the non-static methods by creating instances
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
    
    public static int startedGame = 0;
    public static int count = 0;
    public static int chosefilenum = 0;
    
    static {
        startedGame = InputManager.startedGame;
        count = LoadGame.count;
        chosefilenum = LoadGame.chosefilenum;
    }
}