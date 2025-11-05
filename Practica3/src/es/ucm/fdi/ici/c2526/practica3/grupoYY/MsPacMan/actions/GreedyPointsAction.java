package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GreedyPointsAction implements RulesAction {
	private List<MOVE> CandidateMoves;
	private Map<MOVE, Boolean> moveToPpill;
	private Map<MOVE, Integer> moveToPoints;

	public GreedyPointsAction() {

	}
	
	@Override
	public MOVE execute(Game game) {
		int counter = 0;
		MOVE bestMove = CandidateMoves.get(0);
		
		if(moveToPpill.containsKey(bestMove)) {
			bestMove = CandidateMoves.get(1);
		}
		
		for (Entry<MOVE, Integer> pills : moveToPoints.entrySet()) {
			if (pills.getValue() > counter 
					&& moveToPpill.get(pills.getKey()) 
					&& CandidateMoves.contains(pills.getKey())) {
				bestMove = pills.getKey();
				counter = pills.getValue();
			}
		}

		return bestMove;
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
			
			v = actionFact.getSlotValue("RIGHTMoveToPpill");
			if(!Objects.isNull(v))
				moveToPpill.put(MOVE.RIGHT, Boolean.getBoolean(v.stringValue(null)));
			
			v = actionFact.getSlotValue("LEFTMoveToPpill");
			if(!Objects.isNull(v))
				moveToPpill.put(MOVE.LEFT,  Boolean.getBoolean(v.stringValue(null)));
			
			v = actionFact.getSlotValue("UPMoveToPpill");
			if(!Objects.isNull(v))
				moveToPpill.put(MOVE.UP,  Boolean.getBoolean(v.stringValue(null)));
			
			v = actionFact.getSlotValue("DOWNMoveToPpill");
			if(!Objects.isNull(v))
				moveToPpill.put(MOVE.DOWN, Boolean.getBoolean(v.stringValue(null)));
			
			v = actionFact.getSlotValue("RIGHTMoveToPoints");
			if(!Objects.isNull(v))
				moveToPoints.put(MOVE.RIGHT, v.intValue(null));
			
			v = actionFact.getSlotValue("LEFTMoveToPoints");
			if(!Objects.isNull(v))
				moveToPoints.put(MOVE.LEFT, v.intValue(null));
			
			v = actionFact.getSlotValue("UPMoveToPoints");
			if(!Objects.isNull(v))
				moveToPoints.put(MOVE.UP, v.intValue(null));
			
			v = actionFact.getSlotValue("DOWNMoveToPoints");
			if(!Objects.isNull(v))
				moveToPoints.put(MOVE.DOWN, v.intValue(null));
			
		}
		catch (JessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getActionId() {
		return "Greedy Points Action";
	}

}
