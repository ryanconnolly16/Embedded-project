/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleship;
import java.io.*;
import java.util.*;
/**
 *
 * @author ryanconnolly
 */
public class TwoPlayers {
    //global variables
    static Board board1 = new Board(10);
    static Board board2 = new Board(10);
    static Fleet fleet1 = new Fleet();
    static Fleet fleet2 = new Fleet();
    
    public static void twoplayersetup() throws IOException{
        playersetup(fleet1,board1, "Player 1");
        playersetup(fleet2,board2, "Player 2");
    }
    
    //set up function asking player if they want to use a preset board or not wioth error handling
    public static void playersetup(Fleet fleet, Board board, String name) throws IOException{
        System.out.println("\n\n" + name + " Setup:");
        String answer = UI_Output.usingpreset();
        
        if(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")){
            fleet.preset(fleet, board);
            System.out.println("\nHere is your board:");
        }
        else if(answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")){
            fleet.userpalcement(fleet, board);
        }
        else{
            System.out.println("Invalid answer, try again.");
            playersetup(fleet, board, name);
        }
    }
    
    //function for player taking a shot
    public static void playershoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException{
        Scanner input = new Scanner(System.in);
        System.out.println("\n" + BoardRenderer.renderBoth(shooterboard));
        System.out.println("\bWhere would you look to shoot?:");
        
        String usershot = UI_Output.getInput(input);
        
        //checking input format is correct
        if(usershot.length() < 1 || !Character.isLetter(usershot.charAt(0)) || !Character.isDigit(usershot.charAt(1))){
            System.out.println("Invalid shot, try again.");
            playershoot(shooterboard, receiverfleet, receiverboard);
            return;
        }
        
        //getting x and y possition
        int xpos = Character.toLowerCase(usershot.charAt(0)) - 'a';
        int ypos = 0;
        
        if(usershot.length() == 3 ){
            if(Character.toLowerCase(usershot.charAt(2) - '1')+1 >0){
                System.out.println("Shot is out of bounds, try again");
                playershoot(shooterboard, receiverfleet, receiverboard);
            }
            else{
                ypos = 9;
            }
             
                
        }
        else{
            ypos = (usershot.charAt(1) - '1');
        }
        
        //checkign if out of bounds 
        if(xpos <0 || xpos <0 || ypos < 0){
            System.out.println("Shoot out of bounds try again.");
            playershoot(shooterboard, receiverfleet, receiverboard);
            return;
        }
        
        //checking chosen cell state
        char trial_shot = receiverboard.cellAt(ypos, xpos, Board.GridType.SHIPS);
        
        if (trial_shot == 'X' || trial_shot == 'O'){
            System.out.println("\n\nShot already taken there.");
            System.out.println("Try again.\n\n");
            System.out.println(trial_shot+"--");
            playershoot(shooterboard, receiverfleet, receiverboard);
            return;
        }
        else if (trial_shot == ' ' || trial_shot == 'S'){
            UI_Output.clearConsole();
            Battle.usershot(usershot, shooterboard, receiverfleet, receiverboard);
        }
        
    }
    
    //will alternate between player one and two until either fleet is all sunk
    public void PlayGame() throws IOException{
        while(!fleet1.allSunk() || !fleet2.allSunk()){
            UI_Output.clearConsole();
            System.out.println("Player ones turn;");
            playershoot(board1, fleet2, board2);
            UI_Output.clearConsole();
            System.out.println("Player twos turn;");
            playershoot(board2, fleet1, board1);
            UI_Output.autosave = SaveManager.writeTurnAutosave(board1, board2);
        }
    }
    
}
