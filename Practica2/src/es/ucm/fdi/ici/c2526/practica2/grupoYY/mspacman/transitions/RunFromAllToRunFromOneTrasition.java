package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class RunFromAllToRunFromOneTrasition implements Transition {

	public RunFromAllToRunFromOneTrasition() {
		
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		
		boolean aux = false;
		Game game = input.getGame();
		
		//If we find two or more ghosts under the danger distance, then we are running away from all.		
		for (GHOST ghost : GHOST.values()) {
			if (!game.isGhostEdible(ghost) && game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH) <= input.getDangerDistance()) {
				aux = true;
			}
			if (!game.isGhostEdible(ghost) && game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH) <= input.getDangerDistance() && aux) {
				return false;
			}
		}
		return aux;
	}

	@Override
	public String toString() {
		return String.format("Run from one ghost only");
	}
}
