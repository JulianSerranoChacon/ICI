package es.ucm.fdi.ici.c2425.practica0.SerranoChacon;

import pacman.controllers.PacmanController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacMan extends PacmanController{
	int limit = 20;
	int[] ghosts = new int[GHOST.values().length];
	int closestGhostIndex;		
	GHOST nearestGhost = null;
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		for( int i = 0; i < GHOST.values().length ;i++) {
			ghosts[i] = game.getGhostCurrentNodeIndex(GHOST.values()[i]);
		}
		
		closestGhostIndex = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),ghosts, DM.PATH);
		
		for(GHOST ghostType : GHOST.values()) {
			if(closestGhostIndex == game.getGhostCurrentNodeIndex(ghostType))
				nearestGhost = ghostType;
			/*if(nearestGhost != null)
			System.out.println(nearestGhost.name());*/
		}
		
		if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost)) < limit ) {
			if(!game.isGhostEdible(nearestGhost)) {
				//System.out.println("hide");
				return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),
						game.getGhostCurrentNodeIndex(nearestGhost),game.getPacmanLastMoveMade(),DM.PATH);
			}
			else {
				//System.out.println("seek");
				return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
						game.getGhostCurrentNodeIndex(nearestGhost),game.getPacmanLastMoveMade(),DM.PATH);
			}
		}

		//System.out.println("pill");
		int nearestPill;
		nearestPill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),game.getActivePillsIndices(),DM.PATH);
		
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
				nearestPill,game.getPacmanLastMoveMade(),DM.PATH);
	}

    public String getName() {
    	return "MsPacMan";
    }
}
