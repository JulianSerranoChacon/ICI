package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class GhostsPacmanEstaLejos implements Transition  {

	GHOST ghost;
	int limit = 200;
	public GhostsPacmanEstaLejos(GHOST ghost) {
		super();
		this.ghost = ghost;
		
	}



	@Override
	public boolean evaluate(Input in) {
		this.limit = (in.getGame().getGhostEdibleTime(this.ghost)/2) + 1;
		GhostsInput input = (GhostsInput)in;
		if(in.getGame().getGhostLairTime(ghost) !=0 ) return true;
		int distance = in.getGame().getShortestPathDistance(in.getGame().getPacmanCurrentNodeIndex(), in.getGame().getGhostCurrentNodeIndex(ghost), in.getGame().getPacmanLastMoveMade());
		return (distance>limit);
		
	}



	@Override
	public String toString() {
		return "PacMan is far away";
	}

	
	
}
