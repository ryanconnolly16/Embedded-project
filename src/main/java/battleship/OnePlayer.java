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
public class OnePlayer {
    static Board board = new Board(10);
    static Fleet fleet = new Fleet();
    static Board aiboard = new Board(10);
    static Fleet aifleet = new Fleet();
    public static void playersetup(){
        playersetup(fleet,board);
        aisetup(aifleet, aiboard);
    }
    
    public static void playersetup(Fleet fleet, Board board){
        System.out.println("Player Setup:");
        String answer = UI_Output.usingpreset();
        if(answer.equals("y")){
            fleet.preset(fleet, board);
        }
        else if(answer.equals("n")){
            fleet.userpalcement(fleet, board);
        }
    }

    public static void aisetup(Fleet aifleet, Board aiboard){
        aifleet.preset(aifleet, aiboard);
    }
    
    public static void playershoot(){
        Scanner input = new Scanner(System.in);
        System.out.println("Where would you look to shoot?:");
        String usershot = Test.getInput(input);
        Battle.usershot(usershot, board, aifleet, aiboard);
        System.out.println("\n" + BoardRenderer.renderBoth(board));
        System.out.println("\n" + BoardRenderer.renderBoth(aiboard));
    }
}