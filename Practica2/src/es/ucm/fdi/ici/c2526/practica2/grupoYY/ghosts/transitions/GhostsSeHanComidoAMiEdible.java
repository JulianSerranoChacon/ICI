package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsSeHanComidoAMiEdible implements Transition  {

	GHOST ghost;
	GhostInfo gi;
	public GhostsSeHanComidoAMiEdible(GHOST ghost,GhostInfo g) {
		super();
		this.ghost = ghost;
		gi = g;
	}

///WIP

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;

		if(this.ghost == gi.getMyShieldGhost(this.ghost)) return true;
		boolean flag = input.getGame().isGhostEdible(gi.getMyShieldGhost(this.ghost));
		gi.setaShieldGhost(this.ghost, this.ghost);
		return flag;
	}
	@Override
	public String toString() {
		return "Vuelvo a por PacMan ";
	}

	
	
}
