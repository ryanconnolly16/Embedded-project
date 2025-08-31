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
            System.out.println("\n" + BoardRenderer.renderBoth(pboard));
        }
        else if(answer.equals("n")){
            fleet.userpalcement(fleet, board);
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
        System.out.println("Where would you look to shoot?:");
        String usershot = Test.getInput(input);
        Battle.usershot(usershot, pboard, aifleet, aiboard);
        System.out.println("\n" + BoardRenderer.renderBoth(pboard));
    }
    
    public static void aishoot(){
        Random rand = new Random();
        int xpos = rand.nextInt(10);
        char letterxpos = (char)('a' + xpos);
        int ypos = rand.nextInt(10);
        String usershot = "" + letterxpos + ypos;
        char trial_shot = pboard.cellAt(xpos, ypos, Board.GridType.SHIPS);
        
        if (trial_shot == 'X' || trial_shot == 'O'){
            aishoot();
        }
        else{
            System.out.println("\nThe ai shoots at " + usershot);
            Battle.usershot(usershot, pboard, aifleet, aiboard);
        }
    }
    }
