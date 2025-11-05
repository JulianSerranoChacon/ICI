package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class HideAction implements RulesAction {

	private List<MOVE> CandidateMoves;
	private Map<MOVE, Integer> MoveToNode;
	public HideAction() {

	}
	
	@Override
	public MOVE execute(Game game) {
		
		int nextIntersection = -1;
		double bestMean = Integer.MIN_VALUE;
		int bestIntersection = -1;
		double bestDistanceToIntersection = Double.MAX_VALUE;
		for(MOVE m : CandidateMoves) {
			nextIntersection = MoveToNode.get(m);
			double distanceToIntersection = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), nextIntersection, game.getPacmanLastMoveMade());
			List<Integer> depth2 = depht2Intersections(game, nextIntersection);
			for (Integer i : depth2) {
				double mean = meanDistance(game, i);
				if (bestIntersection != i && mean > bestMean || bestIntersection == i && distanceToIntersection < bestDistanceToIntersection) {
					bestMean = mean;
					bestIntersection = i;
					bestDistanceToIntersection = distanceToIntersection;
				}

			}
		}

		MOVE candidateMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), bestIntersection, game.getPacmanLastMoveMade(), DM.PATH);
		if (CandidateMoves.contains(candidateMove) || CandidateMoves.size() == 0) {
			return candidateMove;
		}
		return CandidateMoves.get(0);
	}

	private List<Integer> depht2Intersections(Game game, int intersection) {
		List<Integer> nextIntersections = new ArrayList<>(); 
		int prevStep = intersection;
		for (int i : game.getNeighbouringNodes(intersection)) {
			int aux[];
			while (!game.isJunction(i)) {
				aux = game.getNeighbouringNodes(i);
				int c = 0;
				while (aux[c] == prevStep) {
					c++;
				}
				prevStep = i;
				i = aux[c];
			}

			if (i != game.getPacmanCurrentNodeIndex()) {

				nextIntersections.add(i);
			}
		}
		return nextIntersections;
	} 

	private double meanDistance(Game game, int node) {
		double mean = 0;

		for (GHOST ghost : GHOST.values()) {
			mean += game.getDistance(game.getGhostCurrentNodeIndex(ghost), node, game.getGhostLastMoveMade(ghost), DM.PATH);
		}

		return mean / 4;
		//return MOVE.NEUTRAL;
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
			v = actionFact.getSlotValue("MoveToNodeRight");
			if(!Objects.isNull(v))
				MoveToNode.put(MOVE.RIGHT, v.intValue(null));
			
			v = actionFact.getSlotValue("MoveToNodeLeft");
			if(!Objects.isNull(v))
				MoveToNode.put(MOVE.LEFT, v.intValue(null));
			
			v = actionFact.getSlotValue("MoveToNodeUp");
			if(!Objects.isNull(v))
				MoveToNode.put(MOVE.UP, v.intValue(null));
			
			v = actionFact.getSlotValue("MoveToNodeDown");
			if(!Objects.isNull(v))
				MoveToNode.put(MOVE.DOWN, v.intValue(null));
		}
		catch (JessException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getActionId() {
		return "Basic Action";
	}

}
