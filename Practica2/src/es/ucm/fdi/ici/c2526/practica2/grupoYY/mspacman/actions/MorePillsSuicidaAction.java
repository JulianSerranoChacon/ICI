package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MorePillsSuicidaAction implements Action {

	private Game game;
	private Map<MOVE,Integer> moveToPoints;

	public MorePillsSuicidaAction() {
		// TODO Auto-generated constructor stub
	}
	
	private void setNextIntersections() {
		Set<Integer> auxSet = new HashSet<>();
		
		for (MOVE m : game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
			// Reset everything
			int count = 0;
			int node = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);
			MOVE dirToMove = m;

			while (!game.isJunction(node)) {

				// Gather number of pills between our node and the node searching
				if (game.getPillIndex(node) != -1) {
					count++;
				}

				// Checks if a ghost
				// NOT EDIBLE
				// NOT GOING OUR DIRECTION is in the way to the node
				// OR IS EDIBLE BUT NOT REACHABLE
				for (GHOST g : GHOST.values()) {
					//We suppose that the ghost is actually coming our way, do not do any other
					//checks
					if (game.getGhostCurrentNodeIndex(g) == node) {
						break;
					}
				}

				// If the node is one corner
				if (game.getNeighbour(node, dirToMove) == -1) {
					int[] possibleNodes = game.getNeighbouringNodes(node);
					for (int i : possibleNodes) {
						if (!auxSet.contains(i) && i != -1) {
							dirToMove = game.getMoveToMakeToReachDirectNeighbour(node, i);
							node = i;
						}
					}
				} else {
					node = game.getNeighbour(node, dirToMove);
				}
				auxSet.add(node);
			}
			moveToPoints.put(m, count);
		}
	}
	
	@Override
	public MOVE execute(Game game) {
		this.game = game;
		return 
	}

	@Override
	public String getActionId() {
		return "Random Action";
	}

}
