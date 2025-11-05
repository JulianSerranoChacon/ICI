package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MoveAction implements RulesAction {


	private List<MOVE> CandidateMoves;
	public MoveAction() {

		CandidateMoves = new ArrayList<>();
	}
	
	@Override
	public MOVE execute(Game game) {
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
		} catch (JessException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public String getActionId() {
		return "Onlymovepossibleaction";
	}

}
