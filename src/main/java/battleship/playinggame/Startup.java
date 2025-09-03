
package battleship.playinggame;

import battleship.ui.*;
import java.io.IOException;

// Single Responsibility: Startup flow only
public class Startup {
    private Console console;
    private GameLauncher gameLauncher;
    
    public Startup() {
        console = new Console();
        gameLauncher = new GameLauncher();
    }
    
    //same method name as your original
    public void startup() throws IOException {
        console.showWelcome();
        gameLauncher.startGame();
    }
}