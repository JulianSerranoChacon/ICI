package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsPacManHaComidoPP implements Transition  {

	GHOST ghost;
	int distanceFromShield = 999999;
	public GhostsPacManHaComidoPP(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

///WIP

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		return in.getGame().wasPowerPillEaten();
	}



	@Override
	public String toString() {
		return "Ghost is edible";
	}

	
	
}
