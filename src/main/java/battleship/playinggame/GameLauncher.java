
package battleship.playinggame;


import battleship.interfaces.*;
import battleship.ui.*;
import battleship.io.*;
import battleship.players.*;
import java.io.IOException;

//function to get how many players are playing, thens asks if they want to load a save, then will play game
public class GameLauncher implements GameStarter {
    private InputManager input;
    private LoadGame save;
    
    public GameLauncher() {
        input = new InputManager();
        save = new LoadGame();
    }
    
    //same method name and logic as your original
    public void playerAmount() throws IOException {
        System.out.println("Are you playing with one or two people?");
        String amount = input.getInput().trim();
        while(true){
            if(amount.equals("1") || amount.equalsIgnoreCase("one")){
                save.loadSavedGame(1);
                OnePlayer oneplayer = new OnePlayer();
                oneplayer.onePlayerSetup();
                
                oneplayer.PlayGame();
            }
            else if(amount.equals("2") || amount.equalsIgnoreCase("two")){
                save.loadSavedGame(2);
                TwoPlayers twoplayers = new TwoPlayers();
                twoplayers.twoPlayerSetup();
                
                twoplayers.PlayGame();
            }
            else{
                System.out.println("Invalid input, try agin.");
                playerAmount();
                return;
            }
        }
    }
    
    @Override
    public void startGame() throws IOException {
        playerAmount();
    }
}