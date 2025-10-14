package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends Input {

		// Data gathered
	boolean finishLevel;
	private Map<MOVE, Integer> moveToNode;
	private Map<MOVE, Integer> moveToPoints;
	private Map<MOVE, Boolean> moveToPpill;
	private Map<MOVE, Boolean> moveToGhost;
	private Map<GHOST, Boolean> ghostEdible;
	private Map<GHOST, MOVE> ghostLastMove;
	private List<MOVE> candidateMoves;
	private int closestPPill;
	private double distanceToPPill;

	public MsPacManInput(Game game) {
		super(game);
		
	}


	private void reset() {
		moveToPoints = new HashMap<>();
		moveToPpill = new HashMap<>();
		moveToGhost = new HashMap<>();
		moveToNode = new HashMap<>();
		ghostEdible = new HashMap<>();
		ghostLastMove = new HashMap<>();
		candidateMoves = new ArrayList<>();
	}

	// Gather all useful information
	private void gatherData(Game game) {
		reset();
		finishLevel = game.getNumberOfActivePowerPills() == 0 ? true : false;
		ghostEdible = getGhostEdible(game);
		ghostLastMove = getGhostLastMove(game);
		candidateMoves = getNextIntersections(game);
		getNearestPPill(game);
	}

	private void getNearestPPill(Game game) {
		int[] ppills = game.getActivePowerPillsIndices();
        closestPPill = -1; // no Ppill active
        distanceToPPill = Double.MAX_VALUE;
	    if (!(game.getNumberOfActivePowerPills() == 0)) {
	        for (int pill : ppills) {
	            double aux = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
	            if (aux < distanceToPPill) {
	                closestPPill = pill;
	                distanceToPPill = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
	            }
	        }
	    }
	}

	private List<MOVE> getNextIntersections(Game game) {
		List<MOVE> candidateNodes = new ArrayList<>();
		Set<Integer> auxSet = new HashSet<>();
		int node = game.getPacmanCurrentNodeIndex();

		for (MOVE m : game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
			// Reset everything
			int count = 0;
			boolean pPill = false;
			boolean ghostInPath = false;
			node = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);
			MOVE dirToMove = m;

			while (!game.isJunction(node)) {

				// Gather number of pills between our node and the node searching
				if (game.getPillIndex(node) != -1) {
					count++;
				}

				// Check if there is a power pill between the intersections
				if (game.getPowerPillIndex(node) != -1) {
					pPill = true;
				}

				// Checks if a ghost
				// NOT EDIBLE
				// NOT GOING OUR DIRECTION is in the way to the node
				// OR IS EDIBLE BUT NOT REACHABLE
				for (GHOST g : GHOST.values()) {
					if ((game.getGhostCurrentNodeIndex(g) == node && dirToMove != ghostLastMove.get(g))
							&& (!ghostEdible.get(g) || ghostEdible.get(g) && !ghostReachable(game, g))) {
						ghostInPath = true;
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
			candidateNodes.add(m);
			moveToGhost.put(m, ghostInPath);
			moveToPoints.put(m, count);
			moveToPpill.put(m, pPill);
			moveToNode.put(m, node);
		}

		return candidateNodes;
	}

	private Map<GHOST, Boolean> getGhostEdible(Game game) {
		Map<GHOST, Boolean> aux = new HashMap<GHOST, Boolean>();
		for (GHOST g : GHOST.values()) {
			if (game.isGhostEdible(g)) {
				aux.put(g, true);
			} else {
				aux.put(g, false);
			}
		}

		return aux;
	}

	private Map<GHOST, MOVE> getGhostLastMove(Game game) {
		Map<GHOST, MOVE> aux = new HashMap<GHOST, MOVE>();
		for (GHOST g : GHOST.values()) {
			aux.put(g, game.getGhostLastMoveMade(g));
		}

		return aux;
	}

	private List<MOVE> filterData(Game game) {
		// Remove all nodes we are not reaching first
		List<MOVE> aux = new ArrayList<>();
		for (MOVE m : candidateMoves) {
			if (!ghostReachUs(game, moveToNode.get(m))
				&& !moveToGhost.get(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), moveToNode.get(m), game.getPacmanLastMoveMade(), DM.PATH))) {
				aux.add(m);
			}
		}

		return aux;
	}

	// We discard candidates when the ghost that is NOT EDIBLE, is CLOSER TO NODE
	// and is MOVING IN THAT DIRECTION.
	private Boolean ghostReachUs(Game game, int interNode) {
		double pacmanToNode = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), interNode);
		
		for (GHOST g : GHOST.values()) {
			double ghostToNode = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), interNode);
			if ( ghostToNode != -1 && pacmanToNode >= ghostToNode && !ghostEdible.get(g)) {
				return true;
			}
		}

		return false;
	}

	// Condition:
	// If there are GHOSTS near us
	// And that number is >= GHOSTS_IN_DISTANCE
	private boolean amIMenaced(Game game) {
	   //Estimation of the worst distance a ghost can be
        try {
            int time_left = Constants.EDIBLE_TIME;

            for(GHOST ghost : GHOST.values()) {
                if(game.getGhostLairTime(ghost) > 0) 
                    continue;
                double distanceToGhostPosition = game.getShortestPathDistance(closestPPill,
                       game.getPacmanCurrentNodeIndex()) + 2 * game.getDistance(closestPPill, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
                if (time_left > distanceToGhostPosition) {
                    time_left -= distanceToGhostPosition;
                    for(GHOST g : GHOST.values()) {
                        if(g == ghost || game.getGhostLairTime(ghost) > 0) 
                            continue;
                        double distanceToSecondGhostPosition = 2 * game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g),
                                game.getGhostCurrentNodeIndex(ghost));
                        if (time_left > distanceToSecondGhostPosition) {
                            return true;
                        }
                    }
                }
            }
        }
        catch(Exception e) {

        }
        return false;
    }
	
	// Conditions:
	// At least one ghost has to be edible
	// I am able to reach ghost(s)
	// There is not another ghost in the way that can kill me
	public boolean amIHunting(Game game) {
		for (GHOST g : GHOST.values()) {
			if (ghostEdible.get(g) && ghostReachable(game, g)) {
				return true;
			}
		}

		return false;
	}

	// Speed of ghosts is halved
	private boolean ghostReachable(Game game, GHOST ghost) {
		if(game.getGhostLairTime(ghost) > 0) {
			return false;
		}
		
		double distanceToGhostPosition = game.getDistance(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH);
		if (game.getGhostLastMoveMade(ghost) != game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH)) {
			return game.getGhostEdibleTime(ghost) >= 2 * distanceToGhostPosition;
		}
		return game.getGhostEdibleTime(ghost) >= distanceToGhostPosition;
	}
	
	@Override
	public void parseInput() {
		// does nothing.

	}

}
