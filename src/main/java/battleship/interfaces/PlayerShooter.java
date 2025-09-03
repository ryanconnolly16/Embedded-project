package battleship.interfaces;

import java.io.IOException;
import battleship.domain.Board;
import battleship.Fleet;

public interface PlayerShooter {
    void playerShoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException;
}