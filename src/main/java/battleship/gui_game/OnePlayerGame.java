package battleship.gui_game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OnePlayerGame extends JPanel {
    
    private final JButton[][] shipsGrid  = new JButton[10][10];
    private final JButton[][] shotsGrid = new JButton[10][10];
    
    private ActionListener onQuitSave;
    private ActionListener onQuitDiscard;
    
    public OnePlayerGame(OnePlayerActions actions) {
        setLayout(new BorderLayout());

        add(new JLabel("One Player", JLabel.CENTER), BorderLayout.CENTER);
        
        //Creating button arrays
        JPanel boards = new JPanel(new GridLayout(1, 2, 10, 10));
        
        //left
        JPanel shipsBoard = new JPanel(new BorderLayout());
        shipsBoard.add(new JLabel("Your ships", JLabel.CENTER), BorderLayout.NORTH);
        JPanel shipsGridPanel = new JPanel(new GridLayout(10, 10, 2, 2));
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                JButton b = new JButton();
                shipsGrid[r][c] = b;
                shipsGridPanel.add(b);
            }
        }
        shipsBoard.add(shipsGridPanel, BorderLayout.CENTER);
        
        //right
        JPanel shotsBoard = new JPanel(new BorderLayout());
        shotsBoard.add (new JLabel("Your shots", JLabel.CENTER), BorderLayout.NORTH);
        JPanel shotsGridPanel = new JPanel(new GridLayout(10, 10, 2, 2));
        for (int r = 0; r< 10; r++) {
            for (int c = 0; c < 10; c++) {
                JButton b = new JButton();
                shotsGrid[r][c] = b;
                shotsGridPanel.add(b);
            }
        }
        shotsBoard.add (shotsGridPanel, BorderLayout.CENTER);
        
        boards.add(shipsBoard);
        boards.add(shotsBoard);
        add(boards, BorderLayout.CENTER);
        
        
        JButton quitSave    = new JButton("Quit & Save");
        JButton quitDiscard = new JButton("Quit & Discard");
        quitSave.addActionListener(e -> actions.quitSave());
        quitDiscard.addActionListener(e -> actions.quitDiscard());
        
        JPanel south = new JPanel();
        south.add(quitSave);
        south.add(quitDiscard);
        add(south, BorderLayout.SOUTH);

    }
}
