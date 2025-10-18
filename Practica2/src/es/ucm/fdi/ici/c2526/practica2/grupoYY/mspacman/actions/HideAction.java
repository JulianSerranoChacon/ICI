package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.PacmanInfo;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class HideAction implements Action {
	PacmanInfo pi;

	public HideAction(PacmanInfo pi) {
		this.pi = pi;
	}
	
	@Override
	public MOVE execute(Game game) {
		int nextIntersection = -1;
		double bestMean = Integer.MIN_VALUE;
		int bestIntersection = -1;
		for(MOVE m : pi.getCandidateMoves()) {
			nextIntersection = pi.getMoveToNode().get(m);
			List<Integer> depth2 = depht2Intersections(game, nextIntersection);
			for (Integer i : depth2) {
				double mean = meanDistance(game, i);
				if (mean > bestMean) {
					bestMean = mean;
					bestIntersection = i;
				}

			}
		}

		MOVE candidateMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), bestIntersection, game.getPacmanLastMoveMade(), DM.PATH);
		if (pi.getCandidateMoves().contains(candidateMove) || pi.getCandidateMoves().size() == 0) {
			return candidateMove;
		}
		return pi.getCandidateMoves().get(0);
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
	}

	@Override
	public String getActionId() {
		return "Basic Action";
	}

}
