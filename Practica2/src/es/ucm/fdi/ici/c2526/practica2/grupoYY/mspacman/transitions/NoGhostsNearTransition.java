package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class NoGhostsNearTransition implements Transition {

	public NoGhostsNearTransition() {
		
	}

	@Override
	public boolean evaluate(Input in) {
        MsPacManInput input = (MsPacManInput) in;
        Game game = input.getGame();

        // if ghosts near return false;
		for (GHOST ghost : GHOST.values()) {
            if (game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH) <= input.getDangerDistance()){
                return false;
            }
        }

        return true;
	}

	@Override
	public String toString() {
		return String.format("there is no ghost close to pacman");
	}
}
