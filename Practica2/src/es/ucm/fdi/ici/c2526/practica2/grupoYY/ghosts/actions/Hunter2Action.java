package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo.GHOSTTYPE;
import pacman.game.GameView;
import java.awt.Color;

public class Hunter2Action implements Action  {
    GHOST ghost;
    GhostInfo gI;
    private int DistanceGhosts = 150; //Limit distance between Ghosts in the same road
    
	public Hunter2Action( GHOST ghost, GhostInfo gi) {
		this.ghost = ghost;
		gI = gi;
	}

	@Override
	public MOVE execute(Game game) {
		if(!game.doesGhostRequireAction(ghost))
			return MOVE.NEUTRAL;
		
		MOVE bestMove = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),DM.PATH);
		MOVE moveToReturn = MOVE.NEUTRAL;  
		double minDistance = 10000000;
		MOVE[] possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
		GHOST hunter1 = null;
	    boolean isHunterInMyWay = false;
		
		double distanceBetweenHunters = 0;
		int index = 0;

		for (GHOST ghostType : GHOST.values()) {
			if(gI.getMyGhostPriority(ghostType) == GHOSTTYPE.HUNTER1) {
				distanceBetweenHunters = game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
				hunter1 = ghostType;
				break;
			}
		}
			
		/*int[] path = game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex());
    	for(int i: path) {
    		if(i == game.getGhostCurrentNodeIndex(hunter1)) {
    	    	GameView.addPoints(game, Color.red, path);
    			isHunterInMyWay = true;
    			break;
    		}
    	}
    	if(!isHunterInMyWay)
    		return bestMove;*/

		for(int i = 0; i < possibleMoves.length; ++i) {
			//If both hunters are to close the second hunter must not take the closest Move to PacMan
			if(distanceBetweenHunters < DistanceGhosts && possibleMoves[i] != bestMove) {
    			if(minDistance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i].opposite())) {
    				minDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i]);
    				moveToReturn = possibleMoves[i];
    			}
    		}
    		// else follow Pac-Man
    		else {
    			if(minDistance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i].opposite())) {
    				minDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i]);
    				moveToReturn = possibleMoves[i];
    				}
    			}
		}
		return moveToReturn;
	}

	@Override
	public String getActionId() {
		return ghost + "Hunter2";
	}
}