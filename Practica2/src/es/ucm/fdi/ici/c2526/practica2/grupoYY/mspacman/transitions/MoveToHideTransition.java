package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MoveToHideTransition implements Transition {

	public MoveToHideTransition() {
		
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		Game game = input.getGame();
		
		if(input.getCandidateMoves().size() <= 1) {
			return false;
		}
		
		for(MOVE m : input.getCandidateMoves()) {
			for(GHOST g : GHOST.values()) {
				if(game.getGhostLairTime(g) <= 0 && !game.isGhostEdible(g)
						&& game.getDistance(game.getGhostCurrentNodeIndex(g), input.getMoveToNode().get(m), DM.EUCLID) <= input.getHideDistance()) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public String toString() {
		return String.format("We go to run away mode");
	}
}
