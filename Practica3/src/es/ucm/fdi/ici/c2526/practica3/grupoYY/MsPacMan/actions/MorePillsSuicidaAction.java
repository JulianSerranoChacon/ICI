package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MorePillsSuicidaAction implements RulesAction {

	private Map<MOVE, Integer> moveToPoints;

	public MorePillsSuicidaAction() {
	
	}
	
	@Override
	public MOVE execute(Game game) {
		
		int counter = 0;
		MOVE bestMove = MOVE.NEUTRAL;
		for (Entry<MOVE, Integer> pills :moveToPoints.entrySet()) {
			if (pills.getValue() > counter) {
				bestMove = pills.getKey();
				counter = pills.getValue();
			}
		}

		return bestMove;
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
		// Nothing to parse
		Value v = actionFact.getSlotValue("MoveToPointsRight");
		if(!Objects.isNull(v))
			moveToPoints.put(MOVE.RIGHT, v.intValue(null));
		
		v = actionFact.getSlotValue("MoveToPointsLeft");
		if(!Objects.isNull(v))
			moveToPoints.put(MOVE.LEFT, v.intValue(null));
		
		v = actionFact.getSlotValue("MoveToPointsUp");
		if(!Objects.isNull(v))
			moveToPoints.put(MOVE.UP, v.intValue(null));
		
		v = actionFact.getSlotValue("MoveToPointsDown");
		if(!Objects.isNull(v))
			moveToPoints.put(MOVE.DOWN, v.intValue(null));
		}catch (JessException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public String getActionId() {
		return "Gopillssuicidaaction";
	}

}
