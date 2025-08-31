/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleship;
import java.util.*;
/**
 *
 * @author ryanconnolly
 */
public class TwoPlayers {
    static Board board1 = new Board(10);
    static Board board2 = new Board(10);
    static Fleet fleet1 = new Fleet();
    static Fleet fleet2 = new Fleet();
    
    public static void twoplayersetup(){
        player1setup(fleet1,board1);
        player2setup(fleet2,board2);
    }
    
    public static void player1setup(Fleet fleet1, Board board1){
        System.out.println("Player 1 Setup:");
        String answer = UI_Output.usingpreset();
        
        if(answer.equals("y")){
            fleet1.preset(fleet1, board1);
        }
        else if(answer.equals("n")){
            fleet1.userpalcement(fleet1, board1);
        }
        else{
            System.out.println("Invalid answer, try again.");
            player1setup(fleet1, board1);
        }
    }
    
    public static void player2setup(Fleet fleet2, Board board2){
        System.out.println("Player 2 Setup:");
        
        String answer = UI_Output.usingpreset();
        if(answer.equals("y")){
            fleet2.preset(fleet2, board2);
        }
        else if(answer.equals("n")){
            fleet2.userpalcement(fleet2, board2);
        }
        else{
            System.out.println("Invalid answer, try again.");
            player2setup(fleet2, board2);
        }
    }
    public static void playershoot(Board shooterboard, Fleet receiverfleet, Board receiverboard){
        Scanner input = new Scanner(System.in);
        System.out.println("\n" + BoardRenderer.renderBoth(shooterboard));
        System.out.println("\bWhere would you look to shoot?:");
        String usershot = null;
        usershot = UI_Output.getInput(input);
        
        if(!Character.isLetter(usershot.charAt(0)) || Character.isLetter(usershot.charAt(1))){
            System.out.println("Invalid shot, try again.");
            playershoot(shooterboard, receiverfleet, receiverboard);
        }
        
        int xpos = usershot.charAt(0) - 'a';
        int ypos = usershot.charAt(1) - '1';
        
        
        if(xpos >10 || xpos <1 || ypos >10 || ypos < 0){
            System.out.println("Shooot out of bounds try again.");
            playershoot(shooterboard, receiverfleet, receiverboard);
        }
        
        char trial_shot = receiverboard.cellAt(xpos, ypos, Board.GridType.SHIPS);
        
        if (trial_shot == 'X' || trial_shot == 'O'){
            System.out.println("\n\nShot already taken there.");
            System.out.println("Try again.\n\n");
            playershoot(shooterboard, receiverfleet, receiverboard);
        }
        else if (trial_shot == ' ' || trial_shot == 'S'){
            Battle.usershot(usershot, shooterboard, receiverfleet, receiverboard);
        }
        
    }
    
    public static void PlayGame(){
        while(!fleet1.allSunk() || !fleet2.allSunk()){
            UI_Output.clearConsole();
            System.out.println("Player ones turn;");
            playershoot(board1, fleet2, board2);
            UI_Output.clearConsole();
            System.out.println("Player twos turn;");
            playershoot(board2, fleet1, board1);
        }
    }
    
}
