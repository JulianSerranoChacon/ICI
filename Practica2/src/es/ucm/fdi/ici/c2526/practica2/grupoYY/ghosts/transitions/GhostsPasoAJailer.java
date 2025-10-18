package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsPasoAJailer implements Transition  {

	GHOST ghost;
	GhostInfo gi;
	public GhostsPasoAJailer(GHOST ghost,GhostInfo gi) {
		super();
		this.ghost = ghost;
		this.gi = gi;
	}



	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		return (gi.getMyGhostPriority(ghost) == 2);
		}



	@Override
	public String toString() {
		return this.ghost+" edible";
	}

	
	
}
