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
        static Board pboard = new Board(10);
        static Fleet pfleet = new Fleet();
        static Board aiboard = new Board(10);
        static Fleet aifleet = new Fleet();
        public static void playersetup(){
        playersetup(pfleet,pboard);
        aisetup(aifleet, aiboard);
    }
    
    public static void playersetup(Fleet fleet, Board board){
        //UI_Output.clearConsole();
        System.out.println("Player Setup:");
        String answer = UI_Output.usingpreset();
        if(answer.equals("y")){
            fleet.preset(fleet, board);
            System.out.println("\nHere is your board;");
        }
        else if(answer.equals("n")){
            fleet.userpalcement(fleet, board);
        }
        else{
            System.out.println("Invalid answer, try again.");
            playersetup(fleet, board);
        }
    }

    public static void aisetup(Fleet aifleet, Board aiboard){
        aifleet.preset(aifleet, aiboard);
        
    }
    
    public static void PlayGame(){
        while(!pfleet.allSunk() || !aifleet.allSunk()){
            playershoot();
            aishoot();
        }
    }
    
    public static void playershoot(){
        Scanner input = new Scanner(System.in);
        System.out.println("\n" + BoardRenderer.renderBoth(pboard));
        System.out.println("\bWhere would you look to shoot?:");
        String usershot = null;
        
        usershot = UI_Output.getInput(input);
        
        if(!Character.isLetter(usershot.charAt(0)) || Character.isLetter(usershot.charAt(1))){
            System.out.println("Invalid shot, try again.");
            playershoot();
        }
        
        int xpos = usershot.charAt(0) - 'a';
        int ypos = usershot.charAt(1) - '1';
        
        
        if(xpos >9 || xpos <1 || ypos >9 || ypos < 0){
            System.out.println("Shot out of bounds try again.");
            playershoot();
        }
                
                
        char trial_shot = aiboard.cellAt(xpos, ypos, Board.GridType.SHIPS);
        
        if (trial_shot == 'X' || trial_shot == 'O'){
            System.out.println("\n\nShot already taken there.");
            System.out.println("Try again.\n\n");
            playershoot();
        }
        else if (trial_shot == ' ' || trial_shot == 'S'){
            Battle.usershot(usershot, pboard, aifleet, aiboard);
        }
    }
    
    public static void aishoot(){
        Random rand = new Random();
        int xpos = rand.nextInt(10);
        int ypos = rand.nextInt(10);
        
        char trial_shot = pboard.cellAt(xpos, ypos, Board.GridType.SHIPS);
        
        if (trial_shot == 'X' || trial_shot == 'O'){
            aishoot();
        }
        else if (trial_shot == ' ' || trial_shot == 'S'){
            ypos ++;
            char letterxpos = (char)('a' + xpos);
            String usershot = "" + letterxpos + ypos;
            System.out.println("\nThe ai shoots at " + usershot);
            Battle.usershot(usershot, aiboard, pfleet, pboard);
        }
    }
}
