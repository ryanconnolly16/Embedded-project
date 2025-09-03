package battleship.players;

import battleship.playinggame.Battle;
import battleship.fleetplacements.*;
import battleship.domain.*;
import battleship.enums.*;
import battleship.interfaces.*;
import battleship.fleetplacements.*;
import java.util.Random;

class Ai implements AiShooter {
    //simple naming like your style
    private final Board playerboard;
    private final Board aiboard;  
    private final Fleet playerfleet;
    
    public Ai(Board playerboard, Board aiboard, Fleet playerfleet) {
        this.playerboard = playerboard;
        this.aiboard = aiboard;
        this.playerfleet = playerfleet;
    }
    
    //same method name and logic as your original
    public void aiShoot() {
        Random rand = new Random();
        int xpos = rand.nextInt(10);
        int ypos = rand.nextInt(10);
        
        //checking what state the chosen cell is - same as your code
        Cell trial_shot = playerboard.cellAt(ypos, xpos, GridType.SHIPS);
        
        //checking if they already shot in that space - same as your code
        if (trial_shot == Cell.HIT || trial_shot == Cell.MISS) {
            aiShoot();
        }
        else if (trial_shot == Cell.WATER || trial_shot == Cell.SHIP) {
            ypos++;
            char letterxpos = (char)('a' + xpos);
            String usershot = "" + letterxpos + ypos;
            System.out.println("\nThe ai shoots at " + usershot);
            Battle.usershot(usershot, aiboard, playerfleet, playerboard);
        }
    }
}