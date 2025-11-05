package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.Objects;
import java.util.List;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToPpillAction implements RulesAction {
	private List<MOVE>CandidateMoves;
	private int closestPPill;

	public GoToPpillAction() {

	}
	
	//Same code since it is very similar, we have to distinct them
	@Override
	public MOVE execute(Game game) {
		
		for (MOVE m :CandidateMoves) {
			if (game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closestPPill, DM.PATH) == m) {
				return m;
			}
		}
		
		return CandidateMoves.get(0);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		try {
			Value v = actionFact.getSlotValue("RIGHTCandidate");
			v = actionFact.getSlotValue("RIGHTCandidate");
			if(!Objects.isNull(v)) {
				String value = v.stringValue(null);
				boolean moveRight = Boolean.getBoolean(value);
				if(moveRight) {
					CandidateMoves.addLast(MOVE.RIGHT);	
				}
			}
			
			
			v = actionFact.getSlotValue("LEFTCandidate");
			if(!Objects.isNull(v)) {
				String value = v.stringValue(null);
				boolean moveLeft = Boolean.getBoolean(value);
				if(moveLeft) {
					CandidateMoves.addLast(MOVE.LEFT);	
				}
			}
			v = actionFact.getSlotValue("UPCandidate");
			if(!Objects.isNull(v)) {
				String value = v.stringValue(null);
				boolean moveUp = Boolean.getBoolean(value);
				if(moveUp) {
					CandidateMoves.addLast(MOVE.UP);	
				}
			}
			v = actionFact.getSlotValue("DOWNCandidate");
			if(!Objects.isNull(v)) {
				String value = v.stringValue(null);
				boolean moveDown = Boolean.getBoolean(value);
				if(moveDown) {
					CandidateMoves.addLast(MOVE.DOWN);	
				}
			}
			
			v = actionFact.getSlotValue("ClosestPpil");
			if(!Objects.isNull(v))
				closestPPill = v.intValue(null);
			
			}
			catch (JessException e) {
				e.printStackTrace();
			}
	}

	@Override
	public String getActionId() {
		return "Go PPill Action";
	}

}
