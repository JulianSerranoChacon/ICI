package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class EdibleGhostsNear implements Transition {

	public EdibleGhostsNear() {
		
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		
		Game game = input.getGame();

		for (GHOST ghost : GHOST.values()) {
			if (game.isGhostEdible(ghost) && 2 * game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH) <= game.getGhostEdibleTime(ghost)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("Run from one ghost only");
	}
}
