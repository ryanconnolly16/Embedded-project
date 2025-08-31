package battleship;

import battleship.Fleet.Ship;
import java.io.IOException;
import java.io.File;
import java.util.*;
// class for testing shit

public class Test {
    
    public static String getInput(Scanner scanner){
        String input = scanner.nextLine();
        if (input.trim().equalsIgnoreCase("x")) {
            System.out.println("Thanks for playing!");
            System.exit(0);
        }
        return input;
    }
    
    
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        
        String answer = UI_Output.Startup();
        
        if(answer.equals("1") ){
            
            
            OnePlayer.playersetup();
            
            for(int i = 0; i < 10; i++){
                OnePlayer.playershoot();
            }
            
            
        }
        
        else if(answer.equals("2") || answer.equals("two")){
            
            TwoPlayers.twoplayersetup(fleet1, board1, fleet2, board2);
            System.out.println("\n" + BoardRenderer.renderBoth(board1));
            System.out.println("\n" + BoardRenderer.renderBoth(board2));
        }


        
        
        
//        for(int i = 0; i<3; i++){
//        System.out.println("Where would you look to shoot?:");
//        String usershot = Test.getInput(input);
//        
//        //player2 shooting
//        Battle.usershot(usershot, player2, fleet1, player1);
//        //Battle.usershot(usershot, player1, fleet2, player2);
//        //System.out.println(fleet1.processHit(0,0));
//        
//        //player1.markHit(0, 0);
//        System.out.println(BoardRenderer.renderBoth(player1));
//        System.out.println(BoardRenderer.renderBoth(player2));
//        }
        
        
//        while(fleet1.allSunk() == false){
//            System.out.println("Where would you look to shoot?:");
//            String usershot = Test.getInput(input);
//            
//            
//            int col = usershot.charAt(0) - 'a';
//            int row = usershot.charAt(1) - '1';
//            
//            //incase of 10 in row
//            if(usershot.length() > 2){
//               row = 9;
//            }
//            
//            if(col > 9 || row > 10){
//                System.out.println("Input is out of bounds, try again.\n\n");
//            }
//            
//            else{
//                
//                Fleet.Ship hit = fleet1.processHit(row, col);
//
//                if (hit != null) {
//                    board.markHit(row, col);
//                    System.out.println("Hit " + 
//                        (hit.isSunk() ? "SUNK!" + hit.name: ""));
//                }
//                else{
//                    System.out.println("Miss.");
//                    board.markMiss(row, col);
//                }
//
//                // Display board
//                System.out.println("\n" + BoardRenderer.renderBoth(board));
//            }
//





        // after user asks to hit (6,0) and you check that the state is hit use:
        //player1.markHit(6, 0);// marks hit on player 1's hit/miss board
        //player2.shipHit(6, 0);// marks hit on player 2's ship board
        // there are also markMiss and shipMiss

        // use renderBoth for rendering both boards for a player
        //System.out.println(BoardRenderer.renderBoth(player1));


        // after each turn use:
        //File autosave = SaveManager.writeTurnAutosave(player1, player2); // calls static method to write the current states 
        //of both players to a temp file called autosave

        // if the user presses 'x' check if they want to save the file and use discard or keep methods
        //SaveManager.keep(autosave);

        // for loading save files
        //FileInput input = new FileInput();

        // change name to whatever save file you need to use and save loadmatch into an array of boards
        //Board[] boards = input.loadMatch(new File("save.log"));
        // save player1 to boards 0 ect...
        //Board player1_new = boards[0];
        //Board player2_new = boards[1];

        //System.out.println(battleship.BoardRenderer.renderBoth(player2_new));
    }
}
