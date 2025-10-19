package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsHayEscudero implements Transition  {

	GHOST ghost;
	int references; 
	public GhostsHayEscudero(GHOST ghost) {
		super();
		this.ghost = ghost;
		this.references = 0;
	}


	public void addReference() {
		this.references++;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		for(GHOST otherGhost : GHOST.values()) {
			if(otherGhost != ghost) {
				//revisa que el fantasma no sea comestible y que su direccion es puesta a la de este fantasma
				if(!in.getGame().isGhostEdible(otherGhost)&&
						(in.getGame().getGhostLastMoveMade(otherGhost)== in.getGame().getGhostLastMoveMade(ghost).opposite())) 
					return true;
			}
		}
		return false;
	}



	@Override
	public String toString() {
		return "Hay escudero " + this.references;
	}

	
	
}
