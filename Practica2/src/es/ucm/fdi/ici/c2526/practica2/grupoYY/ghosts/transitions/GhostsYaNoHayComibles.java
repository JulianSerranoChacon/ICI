package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsYaNoHayComibles implements Transition  {

	GHOST ghost;
	GhostInfo gi;
	int references;
	public GhostsYaNoHayComibles(GHOST ghost,GhostInfo g,int ref) {
		super();
		this.ghost = ghost;
		gi = g;
		this.references = ref;
	}

///WIP

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		if(in.getGame().getGhostLairTime(ghost)!=0) return false;
		
		return in.getGame().isGhostEdible(gi.getMyShieldGhost(ghost));
	}
	@Override
	public String toString() {
		return "Vuelvo a por PacMan " + this.references;
	}

	
	
}
