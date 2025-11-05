package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import java.util.List;

public class GoToNearestPillAction implements RulesAction {

	private List<MOVE> CandidateMoves;
	public GoToNearestPillAction() {

	}
	
	@Override
	public MOVE execute(Game game) {
		
		Map<Double, Integer> distToPill = new HashMap<>();
		PriorityQueue <Double> queue = new PriorityQueue<Double>();
		
		//We create the priority queue where the distance in relation to Pacman is the priority
		for(int pill : game.getPillIndices()) {
			if (game.isPillStillAvailable(pill)) {
				queue.add(game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH));
				distToPill.put(game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH), pill);
			}
		}
		
		//We go through the pills trying to see what is the first one that is easier to reach and does not kill us
		while(!queue.isEmpty()) {
			double aux = queue.poll();
			for (MOVE m : CandidateMoves) {
				if (game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), distToPill.get(aux), game.getPacmanLastMoveMade(), DM.PATH) == m) {
					return m;
				}
			}
		}
		
		//If no pill seems appeling, we play safe
		return CandidateMoves.get(0);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		try {
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
		}
		catch (JessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getActionId() {
		return "Goesnearestpillaction";
	}

}
