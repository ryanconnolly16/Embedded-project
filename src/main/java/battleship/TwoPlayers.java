/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleship;

/**
 *
 * @author ryanconnolly
 */
public class TwoPlayers {
    public static void twoplayersetup(Fleet fleet1, Board board1, Fleet fleet2, Board board2){
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
    }
    
    public static void player2setup(Fleet fleet2, Board board2){
        System.out.println("Player 2 Setup");
        String answer = UI_Output.usingpreset
        ();
        if(answer.equals("y")){
            fleet2.preset(fleet2, board2);
        }
        else if(answer.equals("n")){
            fleet2.userpalcement(fleet2, board2);
        }
    }
    
    
    
}
