package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsPacManVaAPillarPP implements Transition  {

	GHOST ghost;
	int distanceFromShield = 999999;
	public GhostsPacManVaAPillarPP(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

///WIP

	@Override
	public boolean evaluate(Input in) {
		
		GhostsInput input = (GhostsInput)in;
		if(in.getGame().getActivePowerPillsIndices().length == 0) return false;
		boolean flag = false;
		int nearestGhost = 99999;
		int nearestPP = 99999;
		int nearestPPindex = 0;
		for(GHOST otherGhost : GHOST.values()) {
			int distance = in.getGame().getApproximateShortestPathDistance( in.getGame().getGhostCurrentNodeIndex(ghost),in.getGame().getPacmanCurrentNodeIndex(), in.getGame().getGhostLastMoveMade(otherGhost));
			if(distance<nearestGhost) nearestGhost = distance;
			}
		
		for(int node : in.getGame().getActivePowerPillsIndices()) {
			int distance = in.getGame().getApproximateShortestPathDistance(in.getGame().getPacmanCurrentNodeIndex(), node, in.getGame().getPacmanLastMoveMade());
			if(distance<nearestPP) nearestPP = distance;
		}
		if(nearestGhost<nearestPP) return false;
		else {
			
			
    		//MOVE bestPacManMove = in.getGame().getNextMoveAwayFromTarget(in.getGame().getPacmanCurrentNodeIndex(),in.getGame().getGhostCurrentNodeIndex(FleePacManGhost),DM.PATH);
			int[] futureNodesPath  = in.getGame().getApproximateShortestPath(in.getGame().getPacmanCurrentNodeIndex(), nearestPPindex, in.getGame().getPacmanLastMoveMade());
    		for(int i : futureNodesPath) {
    			if(in.getGame().getNeighbouringNodes(i).length>2) return false;
    		}
			
		}
		return false;
	}



	@Override
	public String toString() {
		return "Va a pillar la PP";
	}

	
	
}
