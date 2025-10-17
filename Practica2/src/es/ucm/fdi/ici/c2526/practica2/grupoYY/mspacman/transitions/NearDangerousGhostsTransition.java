package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class NearDangerousGhostsTransition implements Transition {
	private int privateID;
	private static int idcount = 0;

	public NearDangerousGhostsTransition() {
		privateID = idcount;
		idcount++;
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;

		Game game = input.getGame();

		for (GHOST ghost : GHOST.values()) {
			if (!game.isGhostEdible(ghost) && game.getDistance(game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(ghost), DM.PATH) <= input.getDangerDistance()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("Run from one ghost " + privateID);
	}
}
