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
	public GhostsHePasadoAlEscudero(GHOST ghost,GhostInfo g) {
		super();
		this.ghost = ghost;
		gi = g;
	}

	@Override
	public boolean evaluate(Input in) {
		if(gi.getMyShieldGhost(ghost)==ghost || in.getGame().getGhostLairTime(ghost)==0) {
			return false;
		}
		else if (distanceFromShield > gi.getDistanceFromGhostToGhost(ghost, gi.getMyShieldGhost(ghost))){
			distanceFromShield =gi.getDistanceFromGhostToGhost(ghost, gi.getMyShieldGhost(ghost));
			return false;
		}
		else {
			return true;
		}
			
	}



	@Override
	public String toString() {
		return "Pase al escudero";
	}

	
	
}
