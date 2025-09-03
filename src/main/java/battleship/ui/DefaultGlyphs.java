package battleship.ui;

import battleship.enums.Cell;
import battleship.interfaces.Glyphs;

public class DefaultGlyphs implements Glyphs {
    @Override
    public char glyph(Cell cell) {
        return cell.symbol; // Use the enum's symbol field
    }
}
