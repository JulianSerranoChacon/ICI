package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsNoFantasmasCerca implements Transition  {

	GHOST ghost;
	int limit = 100;
	public GhostsNoFantasmasCerca(GHOST ghost) {
		super();
		this.ghost = ghost;
	}



	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		for(GHOST otherGhost : GHOST.values()) {
			if(otherGhost != ghost) {
				//revisa que el fantasma no sea comestible y que su direccion es puesta a la de este fantasma
				if(limit>in.getGame().getApproximateShortestPathDistance(in.getGame().getGhostCurrentNodeIndex(otherGhost), 
						in.getGame().getGhostCurrentNodeIndex(ghost), in.getGame().getGhostLastMoveMade(otherGhost) )) 
					return false;
			}
		}
		return true;
	}



	@Override
	public String toString() {
		return "Ghost is edible";
	}

	
	
}
