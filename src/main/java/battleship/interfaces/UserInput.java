
package battleship.interfaces;
import java.io.IOException;
public interface UserInput {
    String getinput() throws IOException;
    String askpreset() throws IOException;
}