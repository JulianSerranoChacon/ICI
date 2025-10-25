package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import java.util.Objects;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Hunter2Action implements RulesAction  {
	private static final int DISTANCE_LIMIT = 90; //Limit distance between Ghosts in the same road
	
    private GHOST ghost;
	private GHOST hunter1;
    
	public Hunter2Action(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if(!game.doesGhostRequireAction(ghost))
			return MOVE.NEUTRAL;
		
		MOVE[] possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
		MOVE bestMove = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),DM.PATH);
		MOVE moveToReturn = MOVE.NEUTRAL;  
		double minDistance = Double.MAX_VALUE;
		double distanceBetweenHunters = 0;
		
		for (GHOST g : GHOST.values()) {
			if(g == hunter1) {
				distanceBetweenHunters = game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(g), DM.PATH);
				break;
			}
		}
		
		for(int i = 0; i < possibleMoves.length; ++i) {
			//If both hunters are to close the second hunter must not take the closest Move to PacMan
			if(distanceBetweenHunters < DISTANCE_LIMIT && possibleMoves[i] != bestMove) {
    			if(minDistance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i])) {
    				minDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i]);
    				moveToReturn = possibleMoves[i];
    			}
    		}
    		// else follow Pac-Man
    		else {
    			if(minDistance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i])) {
    				minDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i]);
    				moveToReturn = possibleMoves[i];
    				}
    		}
		}
		
		return moveToReturn;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		
		try {
			Value hunter1Role = actionFact.getSlotValue("hunter1Id");
			if(!Objects.isNull(hunter1Role)) {
				String strategyValue = hunter1Role.stringValue(null);
				hunter1 = GHOST.valueOf(strategyValue);
			}
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getActionId() {
		return ghost + "Hunter2";
	}

}