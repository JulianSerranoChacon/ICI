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
		
		//Get possible moves of ghost
		MOVE[] possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
		//Move that hunter 1 will do to go to pacman
		MOVE moveHunter1 = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),DM.PATH);
		//Saves return moves
		MOVE moveToReturn = MOVE.NEUTRAL;
		
		//Distances
		double minDistance = Double.MAX_VALUE;
		double distanceBetweenHunters = game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(hunter1), DM.PATH);
		
		for(MOVE m : possibleMoves) { 
			if(minDistance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), m)) {
				//If both hunters are to close, the second hunter must not take the closest Move to PacMan
				if(distanceBetweenHunters < DISTANCE_LIMIT && m != moveHunter1) {
    				minDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), m);
    				moveToReturn = m;
	    		}
			}
		}
		if(moveToReturn == game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(ghost) ,DM.PATH))
		return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(ghost) ,DM.PATH);
		return moveToReturn;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value hunter1Role = actionFact.getSlotValue("extraGhost");
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