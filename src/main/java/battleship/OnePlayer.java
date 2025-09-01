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
    
    //global variables for each fleet and board
    static Board pboard = new Board(10);
    static Fleet pfleet = new Fleet();
    static Board aiboard = new Board(10);
    static Fleet aifleet = new Fleet();
    
    
    public static void playersetup(){
        playersetup(pfleet,pboard);
        aisetup(aifleet, aiboard);
    }

    //set up function asking player if they want to use a preset board or not wioth error handling
    public static void playersetup(Fleet fleet, Board board){
        //UI_Output.clearConsole();
        System.out.println("Player Setup:");
        String answer = UI_Output.usingpreset();
        
        if(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")){
            fleet.preset(fleet, board);
            System.out.println("\nHere is your board;");
        }
        else if(answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")){
            fleet.userpalcement(fleet, board);
        }
        else{
            System.out.println("Invalid answer, try again.");
            playersetup(fleet, board);
        }
    }

    //setting up aiboard with preset function in fleet class
    public static void aisetup(Fleet aifleet, Board aiboard){
        aifleet.preset(aifleet, aiboard);
        
    }
    
    //will alternate between player and ai shots until either fleet is sunk
    public static void PlayGame(){
        while(!pfleet.allSunk() || !aifleet.allSunk()){
            UI_Output.clearConsole();
            playershoot();
            aishoot();
        }
    }
    
    //function for player taking a shot
    public static void playershoot(){
        Scanner input = new Scanner(System.in);
        System.out.println("\n" + BoardRenderer.renderBoth(pboard));
        System.out.println("\bWhere would you look to shoot?:");
        String usershot = null;
        
        usershot = UI_Output.getInput(input);
        
        //checking input format
        if(!Character.isLetter(usershot.charAt(0)) || Character.isLetter(usershot.charAt(1))){
            System.out.println("Invalid shot, try again.");
            playershoot();
        }
        
        //getting x and y posistion
        int ypos;
        int xpos = Character.toLowerCase(usershot.charAt(0) - 'a');
        if(usershot.length() > 2){
            ypos = 9;
        }
        else{
            ypos = usershot.charAt(1) - '1';
        }
        //checking for out of bounds
        if(xpos >9 || xpos <0 || ypos >9 || ypos < 0){
            System.out.println("Shot out of bounds try again.");
            playershoot();
        }
                
        //checking what state the cell they chose is
        char trial_shot = aiboard.cellAt(ypos, xpos, Board.GridType.SHIPS);
        
        
        UI_Output.clearConsole();
        
        //checking if they already shot in that space 
        if (trial_shot == 'X' || trial_shot == 'O'){
            System.out.println("\n\nShot already taken there.");
            System.out.println("Try again.\n\n");
            playershoot();
        }
        else if (trial_shot == ' ' || trial_shot == 'S'){
            Battle.usershot(usershot, pboard, aifleet, aiboard);
        }
    }
    
    //using random function to chose a square, checks if already shot their
    public static void aishoot(){
        Random rand = new Random();
        int xpos = rand.nextInt(10);
        int ypos = rand.nextInt(10);
        //checking what state the chosen cell is
        char trial_shot = pboard.cellAt(ypos, xpos, Board.GridType.SHIPS);
        
        //checking if they already shot in that space 
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
