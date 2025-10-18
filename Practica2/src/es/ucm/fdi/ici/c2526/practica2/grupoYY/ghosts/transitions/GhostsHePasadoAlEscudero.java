package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsHePasadoAlEscudero implements Transition  {

	GHOST ghost;
	GhostInfo gi;
	int distanceFromShield = 999999;
	public GhostsHePasadoAlEscudero(GHOST ghost,GhostInfo gi) {
		super();
		this.ghost = ghost;
		this.gi = gi;
	}

///WIP

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		if(gi.getMyShieldGhost(ghost)==ghost) return false;
		else if (distanceFromShield >gi.getDistanceFromGhostToGhost(ghost, gi.getMyShieldGhost(ghost))){
			distanceFromShield =gi.getDistanceFromGhostToGhost(ghost, gi.getMyShieldGhost(ghost));
			return false;
		}else return true;
	}



	@Override
	public String toString() {
		return "Pase al escudero";
	}

	
	
}
