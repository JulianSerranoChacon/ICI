package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import java.util.Map;
import java.util.Objects;

import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.GhostsInput.GHOSTTYPE;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Hunter2Action implements RulesAction  {
    GHOST ghost;
    private int DistanceGhosts = 90; //Limit distance between Ghosts in the same road
	private Map<GHOST, GHOSTTYPE> ghostPriority;
    
	public Hunter2Action( GHOST ghost) {
		this.ghost = ghost;
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
			if(ghostPriority.get(ghostType) == GHOSTTYPE.HUNTER1) {
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
			Value blinkyRole = actionFact.getSlotValue("BLINKYghostRole");
			if(!Objects.isNull(blinkyRole)) {
				String strategyValue = blinkyRole.stringValue(null);
				ghostPriority.put(GHOST.BLINKY, GHOSTTYPE.valueOf(strategyValue));
			}
			
			Value inkyRole = actionFact.getSlotValue("INKYghostRole");
			if(!Objects.isNull(inkyRole)) {
				String strategyValue = inkyRole.stringValue(null);
				ghostPriority.put(GHOST.BLINKY, GHOSTTYPE.valueOf(strategyValue));
			}
			
			Value pinkyRole = actionFact.getSlotValue("PINKYghostRole");
			if(!Objects.isNull(pinkyRole)) {
				String strategyValue = pinkyRole.stringValue(null);
				ghostPriority.put(GHOST.BLINKY, GHOSTTYPE.valueOf(strategyValue));
			}
			
			Value sueRole = actionFact.getSlotValue("SUEghostRole");
			if(!Objects.isNull(sueRole)) {
				String strategyValue = sueRole.stringValue(null);
				ghostPriority.put(GHOST.BLINKY, GHOSTTYPE.valueOf(strategyValue));
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