
package battleship.interfaces;
import java.io.IOException;
public interface UserInput {
    String getInput() throws IOException;
    String askPreset() throws IOException;
}