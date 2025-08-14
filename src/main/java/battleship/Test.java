package battleship;

import java.io.IOException;
import java.io.File;
// class for testing shit

public class Test {
        public static void main(String[] args) throws IOException {
        battleship.FileInput in = new battleship.FileInput();

        File save = new File("save.log");

        battleship.Board[] boards = in.loadMatch(save);
        battleship.Board player1 = boards[0];
        battleship.Board player2 = boards[1];

        System.out.println(battleship.BoardRenderer.renderBoth(player2));
        
//        Board player1 = new Board(10);
//        Board player2 = new Board(10);
//        
//        player2.placeShip(6, 0, 5, Board.Direction.UP);
//        
//        player1.markHit(6, 0);
//        player2.shipHit(6, 0);
//        System.out.println(BoardRenderer.renderBoth(player1));
//        FileOutput save = new FileOutput();
//        save.process(player1, player2);
    }
}
