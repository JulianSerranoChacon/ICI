package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsPacManLejosParpadeo implements Transition  {

	GHOST ghost;
	int limitDistance = 40;
	int limitTime = 10;
	public GhostsPacManLejosParpadeo(GHOST ghost) {
		super();
		this.ghost = ghost;
	}



	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		if(in.getGame().getGhostLairTime(ghost)!=0) return false;
		int distance = in.getGame().getShortestPathDistance(in.getGame().getPacmanCurrentNodeIndex(), in.getGame().getGhostCurrentNodeIndex(ghost), in.getGame().getPacmanLastMoveMade());
		
		return ((in.getGame().getGhostEdibleTime(ghost)<limitTime)&& (distance> limitDistance));
				
		
	}



	@Override
	public String toString() {
		return "I'm flicking and PacMan is Far" ;
	}

	
	
}
