package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

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

	}
	
	@Override
	public MOVE execute(Game game) {
		return CandidateMoves.get(0);
	}

	@Override
	public void parseFact(Fact actionFact) {
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
		} catch (JessException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public String getActionId() {
		return "Only move possible action";
	}

}
