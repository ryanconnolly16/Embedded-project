package battleship.interfaces;

import battleship.domain.Board;
import battleship.fleetplacements.Fleet;

public interface AiShooter {
    void aiShoot(Board aiboard, Fleet pfleet, Board pboard);
    
}