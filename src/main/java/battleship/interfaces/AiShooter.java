package battleship.interfaces;

import battleship.domain.Board;
import battleship.fleetplacements.Fleet;

public interface AiShooter {
    String aiShoot(Board aiboard, Fleet pfleet, Board pboard);
    
}