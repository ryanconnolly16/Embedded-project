package battleship.interfaces;

import java.io.IOException;

import battleship.domain.Board;
import battleship.fleetplacements.*;

public interface PlayerShooter {
    String playershoot(Board shooterboard, Fleet receiverfleet, Board receiverboard) throws IOException;
}