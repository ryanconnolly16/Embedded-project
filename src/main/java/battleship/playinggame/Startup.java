
package battleship.playinggame;

import battleship.ui.*;
import java.io.IOException;

public class Startup {
    private Console console;
    private GameLauncher gameLauncher;
    
    public Startup() {
        console = new Console();
        gameLauncher = new GameLauncher();
    }
    
    //shows game introduction then will get into the game
    public void startup() throws IOException {
        console.showwelcome();
        gameLauncher.startGame();
    }
}