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
		if(in.getGame().getActivePowerPillsIndices().length == 0) {
			return false;		
		}
		boolean flag = false;
		int nearestGhost = 99999;
		int nearestPP = 99999;
		int nearestPPindex = 0;
		
		for(int node : in.getGame().getActivePowerPillsIndices()) {
			int distance = in.getGame().getShortestPathDistance(in.getGame().getPacmanCurrentNodeIndex(), node, in.getGame().getPacmanLastMoveMade());
			if(distance<nearestPP) {
				nearestPP = distance;
				nearestPPindex = node;
			}
		}
		
		for(GHOST otherGhost : GHOST.values()) {
			int distance = in.getGame().getShortestPathDistance( in.getGame().getGhostCurrentNodeIndex(ghost),nearestPPindex, in.getGame().getGhostLastMoveMade(otherGhost));
			if(distance<nearestGhost) nearestGhost = distance;
			}
		
	
		
		if(in.getGame().isJunction(in.getGame().getPacmanCurrentNodeIndex())) { return false;}
		if(nearestGhost<nearestPP)  {return false;}
		else {
			
			
    		//MOVE bestPacManMove = in.getGame().getNextMoveAwayFromTarget(in.getGame().getPacmanCurrentNodeIndex(),in.getGame().getGhostCurrentNodeIndex(FleePacManGhost),DM.PATH);
			int[] futureNodesPath  = in.getGame().getShortestPath(in.getGame().getPacmanCurrentNodeIndex(), nearestPPindex, in.getGame().getPacmanLastMoveMade());
    		for(int i : futureNodesPath) {
    			//System.out.println(in.getGame().getNeighbouringNodes(i).length);
    			if(in.getGame().isJunction(i)) { 
    				
    				return false;
    				}
    		}
			
		}
		
		
		return true;
	}



	@Override
	public String toString() {
		return "Va a pillar la PP";
	}

	
	
}
