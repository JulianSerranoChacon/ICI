package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.List;
import java.util.Objects;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EatPpillAction implements RulesAction {

	MOVE moveToPpill;
	private List<MOVE> CandidateMoves;
	public EatPpillAction() {
	}
	
	@Override
	public MOVE execute(Game game) {
		
		/*for (Entry<MOVE, Boolean> m : pi.getMoveToPpill().entrySet()) {
			if (m.getValue()) {
				return m.getKey();
			}
		}*/
		
	
		//return pi.getCandidateMoves().get(0);
		if(moveToPpill == null)
			return moveToPpill;
		
		return CandidateMoves.get(0);
	}
	
	@Override
	public void parseFact(Fact actionFact){
		// Nothing to parse
		try {
		
		Value v = actionFact.getSlotValue("goPillMove");
		if(!Objects.isNull(v)) {
			moveToPpill = MOVE.valueOf(v.stringValue(null));
		}
		
		v = actionFact.getSlotValue("CandidateRight");
		if(!Objects.isNull(v)) {
			String value = v.stringValue(null);
			boolean moveRight = Boolean.getBoolean(value);
			if(moveRight) {
				CandidateMoves.addLast(MOVE.RIGHT);	
			}
		}
		
		
		v = actionFact.getSlotValue("CandidateLeft");
		if(!Objects.isNull(v)) {
			String value = v.stringValue(null);
			boolean moveLeft = Boolean.getBoolean(value);
			if(moveLeft) {
				CandidateMoves.addLast(MOVE.LEFT);	
			}
		}
		v = actionFact.getSlotValue("CandidateUp");
		if(!Objects.isNull(v)) {
			String value = v.stringValue(null);
			boolean moveUp = Boolean.getBoolean(value);
			if(moveUp) {
				CandidateMoves.addLast(MOVE.UP);	
			}
		}
		v = actionFact.getSlotValue("CandidateDown");
		if(!Objects.isNull(v)) {
			String value = v.stringValue(null);
			boolean moveDown = Boolean.getBoolean(value);
			if(moveDown) {
				CandidateMoves.addLast(MOVE.DOWN);	
			}
		}
		}
		catch (JessException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public String getActionId() {
		return "EatPPillAction";
	}

}
