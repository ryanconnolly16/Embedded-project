
package battleship.playinggame;


import battleship.interfaces.*;
import battleship.ui.*;
import battleship.io.*;
import battleship.players.*;
import java.io.IOException;

//function to get how many players are playing, thens asks if they want to load a save, then will play game
public class GameLauncher implements GameStarter {
    private final  InputManager input;
    private final LoadGame save;
    
    public GameLauncher() {
        input = new InputManager();
        save = new LoadGame();
    }
    
    public void playeramount() throws IOException {
        System.out.println("Are you playing with one or two people?");
        String amount = input.getinput().trim();
        while(true){
            if(amount.equals("1") || amount.equalsIgnoreCase("one")){
                save.loadsavedgame(1);
                OnePlayer oneplayer = new OnePlayer();
                oneplayer.oneplayerSetup();
                
                oneplayer.PlayGame();
            }
            else if(amount.equals("2") || amount.equalsIgnoreCase("two")){
                save.loadsavedgame(2);
                TwoPlayers twoplayers = new TwoPlayers();
                twoplayers.twoplayerSetup();
                
                twoplayers.playGame();
            }
            else{
                System.out.println("Invalid input, try agin.");
                playeramount();
                return;
            }
        }
    }
    
    @Override
    public void startGame() throws IOException {
        playeramount();
    }
}