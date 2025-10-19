package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsYaNoHayComibles implements Transition  {

	GHOST ghost;
	GhostInfo gi;
	public GhostsYaNoHayComibles(GHOST ghost,GhostInfo g) {
		super();
		this.ghost = ghost;
		gi = g;
	}

///WIP

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;

		for(GHOST g : GHOST.values()) if (g!=ghost &&in.getGame().isGhostEdible(g)) return false;
		return true;
	}
	@Override
	public String toString() {
		return "Vuelvo a por PacMan ";
	}

	
	
}
