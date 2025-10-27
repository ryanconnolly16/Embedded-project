
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    
    private JPanel currentPanel;  // Track which panel is showing
    
    public MainFrame() {
        //initComponents();
        setTitle("My Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}