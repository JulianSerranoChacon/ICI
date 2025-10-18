package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.PacmanInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class HuntAction implements Action {
	PacmanInfo pi;

	public HuntAction(PacmanInfo pi) {
		this.pi = pi;
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
			if (!game.isGhostEdible(g) || game.getGhostLairTime(g) > 0 || !pi.getCandidateMoves().contains(candidateMove)) { continue; }
			double timeToEatFirst = timeToPPill(game, pi.getClosestPPill()) + timeToEat(game, g);
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
		return candidateMove;
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

	private double timeToPPill(Game game, int pPill) {
		return 2 * game.getShortestPathDistance(game.getPacManInitialNodeIndex(), pPill, game.getPacmanLastMoveMade());
	}

	@Override
	public String getActionId() {
		return "Chase Action";
	}

}
