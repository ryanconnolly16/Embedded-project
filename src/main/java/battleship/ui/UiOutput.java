
package battleship.ui;

import battleship.io.*;
import battleship.playinggame.*;
import java.io.*;
import java.sql.Connection;
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
    public static void clearConsole() {
        console.clearconsole();
    }

    public static void startUp(Connection c) throws IOException {
        startup.startup(c);
    }
    
    public static void playerAmount(Connection c) throws IOException, Exception {
        gameLauncher.playeramount(c);
    }
    
    public static void loadSavedGame(int pplamount) throws IOException {
        loadgame.loadsavedgame(pplamount);
    }
    
    public static String usingPreset() throws IOException {
        return inputmanager.askpreset();
    }
    
    public static String getInput(Scanner scanner) throws IOException {
        return inputmanager.getinput();
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