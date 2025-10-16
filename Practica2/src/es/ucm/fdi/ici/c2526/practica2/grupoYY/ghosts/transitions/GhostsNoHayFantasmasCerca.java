package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class GhostsNoHayFantasmasCerca implements Transition  {

	GHOST ghost;
	int limit = 50;
	public GhostsNoHayFantasmasCerca(GHOST ghost) {
		super();
		this.ghost = ghost;
	}



	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		
		for(GHOST otherGhost : GHOST.values()) {
			if(otherGhost != ghost) {
				if(in.getGame().getDistance(in.getGame().getGhostCurrentNodeIndex(otherGhost),
						in.getGame().getGhostCurrentNodeIndex(ghost),DM.EUCLID) <limit) return false;
			}
		}
		return true;
	}



	@Override
	public String toString() {
		return "Ghost is edible";
	}

	
	
}
