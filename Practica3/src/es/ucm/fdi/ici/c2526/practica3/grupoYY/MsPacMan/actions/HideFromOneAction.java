package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class HideFromOneAction implements RulesAction {


	public HideFromOneAction() {

	}
	
	@Override
	public MOVE execute(Game game) {
		/*
		GHOST closestGhost = null;
		double closestDistance = Double.MAX_VALUE;
		double distance;
		for (GHOST ghost : GHOST.values()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), 
					game.getPacmanLastMoveMade(), DM.PATH);
			if (distance < closestDistance) {
				closestGhost = ghost; 
				closestDistance = distance;
			}
		}
		MOVE candidateMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost),
				game.getPacmanLastMoveMade(), DM.PATH);
		if (pi.getCandidateMoves().contains(candidateMove) || pi.getCandidateMoves().size() == 0) {
			return candidateMove;
		}
		return pi.getCandidateMoves().get(0);*/
		return MOVE.NEUTRAL;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return "Hide From One Action";
	}

}
