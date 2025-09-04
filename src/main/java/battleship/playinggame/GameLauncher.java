
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
                OnePlayer oneplayer = new OnePlayer();
                save.loadsavedgame(1);
                
                oneplayer.PlayGame();
            }
            else if(amount.equals("2") || amount.equalsIgnoreCase("two")){
                TwoPlayers twoplayers = new TwoPlayers();
                save.loadsavedgame(2);
                
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