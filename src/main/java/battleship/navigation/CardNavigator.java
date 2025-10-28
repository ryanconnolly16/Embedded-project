// battleship/navigation/CardNavigator.java
package battleship.navigation;

import javax.swing.JPanel;
import java.awt.CardLayout;

public class CardNavigator implements Navigator {
    private final CardLayout layout;
    private final JPanel container;

    public CardNavigator(CardLayout layout, JPanel container) {
        this.layout = layout;
        this.container = container;
    }

    @Override
    public void show(String cardName) {
        layout.show(container, cardName);
    }
}