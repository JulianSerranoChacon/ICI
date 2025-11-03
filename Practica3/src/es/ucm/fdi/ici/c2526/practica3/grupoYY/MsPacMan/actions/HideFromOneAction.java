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
import pacman.game.Game;

public class HideFromOneAction implements RulesAction {
	private List<MOVE> CandidateMoves;


	public HideFromOneAction() {

	}
	
	@Override
	public MOVE execute(Game game) {
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
		if (CandidateMoves.contains(candidateMove) || CandidateMoves.size() == 0) {
			return candidateMove;
		}
		return CandidateMoves.get(0);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		try {
			// Nothing to parse
			Value v = actionFact.getSlotValue("RIGHTCandidate");
			if(!Objects.isNull(v) && v.symbolValue(null) == "true")
				CandidateMoves.addLast(MOVE.RIGHT);
			
			v = actionFact.getSlotValue("LEFTCandidate");
			if(!Objects.isNull(v) && v.symbolValue(null) == "true")
				CandidateMoves.addLast(MOVE.LEFT);
			
			v = actionFact.getSlotValue("UPCandidate");
			if(!Objects.isNull(v) && v.symbolValue(null) == "true")
				CandidateMoves.addLast(MOVE.UP);
			
			v = actionFact.getSlotValue("DOWNCandidate");
			if(!Objects.isNull(v) && v.symbolValue(null) == "true")
				CandidateMoves.addLast(MOVE.DOWN);
		}
		catch (JessException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getActionId() {
		return "Hide From One Action";
	}

}
