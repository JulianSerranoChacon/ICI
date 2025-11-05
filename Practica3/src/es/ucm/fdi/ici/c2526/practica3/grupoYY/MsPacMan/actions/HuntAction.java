package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.List;
import java.util.Objects;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class HuntAction implements RulesAction {

	private List<MOVE> CandidateMoves;

	public HuntAction() {
	
	}
	
	@Override
	public MOVE execute(Game game) {
		
		GHOST objective = null;
		double distance = Double.MAX_VALUE;

		// By default take the closest edible ghost
		for (GHOST g : GHOST.values()) {
			if (!game.isGhostEdible(g) || game.getGhostLairTime(g) > 0) { continue; }

			double candidateDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), 
										game.getGhostCurrentNodeIndex(g), 
										game.getPacmanLastMoveMade(), DM.PATH);
										
			if (candidateDistance < distance) {
				distance = candidateDistance;
				objective = g;
			}
		}
		
		distance = Double.MAX_VALUE;
		double bestSpareTime = 0;
		for(GHOST g : GHOST.values()) {
			MOVE candidateMove = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), game.getPacmanLastMoveMade(), DM.PATH);
			if (!game.isGhostEdible(g) || game.getGhostLairTime(g) > 0 || !CandidateMoves.contains(candidateMove)) { continue; }
			double timeToEatFirst = timeToEat(game, g);
			double timeLeft = Constants.EDIBLE_TIME;
			timeLeft = timeLeft - timeToEatFirst;
			if(timeLeft > 0) {
				for (GHOST g2 : GHOST.values()) {
					if (g == g2 || !game.isGhostEdible(g2) || game.getGhostLairTime(g2) > 0) { continue; }
					
					double spareTime = timeLeft - timeToEat(game, game.getGhostCurrentNodeIndex(g), g2);
					if (spareTime >= bestSpareTime) {
						objective = g;
						bestSpareTime = spareTime;
					}
				}
			}
		}
		MOVE candidateMove = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(objective), game.getPacmanLastMoveMade(), DM.PATH);
		if (CandidateMoves.contains(candidateMove) || CandidateMoves.size() == 0) {
			return candidateMove;
		}

		return CandidateMoves.get(0);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		try {
			// Nothing to parse
			Value v;
			v = actionFact.getSlotValue("CandidateRight");
			if(!Objects.isNull(v)) {
				String value = v.stringValue(null);
				boolean moveRight = Boolean.parseBoolean(value);
				if(moveRight) {
					CandidateMoves.addLast(MOVE.RIGHT);	
				}
			}
			
			
			v = actionFact.getSlotValue("CandidateLeft");
			if(!Objects.isNull(v)) {
				String value = v.stringValue(null);
				boolean moveLeft = Boolean.parseBoolean(value);
				if(moveLeft) {
					CandidateMoves.addLast(MOVE.LEFT);	
				}
			}
			v = actionFact.getSlotValue("CandidateUp");
			if(!Objects.isNull(v)) {
				String value = v.stringValue(null);
				boolean moveUp = Boolean.parseBoolean(value);
				if(moveUp) {
					CandidateMoves.addLast(MOVE.UP);	
				}
			}
			v = actionFact.getSlotValue("CandidateDown");
			if(!Objects.isNull(v)) {
				String value = v.stringValue(null);
				boolean moveDown = Boolean.parseBoolean(value);
				if(moveDown) {
					CandidateMoves.addLast(MOVE.DOWN);	
				}
			}
		}
		catch (JessException e) {
			e.printStackTrace();
		}
	}

	/* this function assumes that the ghost is going to be running away from pacman so it is pessimistic, it may cut cases where it could have
	 * reached the ghost but it didn't see it
	 */
	private double timeToEat(Game game, int startPosition, GHOST ghost) {
		return 2 * game.getShortestPathDistance(startPosition, game.getGhostCurrentNodeIndex(ghost));
	}

	private double timeToEat(Game game, GHOST ghost) {
		return 2 * game.getShortestPathDistance(game.getPacManInitialNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade());
	}

	@Override
	public String getActionId() {
		return "HuntAction";
	}

}
