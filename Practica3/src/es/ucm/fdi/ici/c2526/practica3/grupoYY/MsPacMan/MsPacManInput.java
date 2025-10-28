package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends RulesInput {

	// Data gathered TODO: Maybe erase this and have everything on pi
	boolean goGreedy; //We always start in greedy, then the rules will define where we will go
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
	private final double dangerDistance = 30; //tentative, subject to change 
	private final double hideDistance = 30; //tentative, subject to change 
	private int numFantasmasNoComestiblesCerca;
	private int numFantasmasComestiblesCerca;

	public double getDangerDistance() {
		return dangerDistance;
	}

	public MsPacManInput(Game game) {
		super(game);
		goGreedy = true;
		moveToPoints = new HashMap<>();
		moveToPpill = new HashMap<>();
		moveToGhost = new HashMap<>();
		moveToNode = new HashMap<>();
		ghostEdible = new HashMap<>();
		ghostLastMove = new HashMap<>();
		candidateMoves = new ArrayList<>();
	}

	private void reset() {
		goGreedy = true;
		moveToPoints = new HashMap<>();
		moveToPpill = new HashMap<>();
		moveToGhost = new HashMap<>();
		moveToNode = new HashMap<>();
		ghostEdible = new HashMap<>();
		ghostLastMove = new HashMap<>();
		candidateMoves = new ArrayList<>();
	}
	
	@Override
	public void parseInput() {
		reset();
		finishLevel = game.getNumberOfActivePowerPills() == 0 ? true : false;
		setGhostEdible();
		setGhostLastMove();
		setNextIntersections();
		setNearestPPill();
	}
	
	//Tenemos que rellenar con los datos que usan las reglas de PacMan para moverse
	@Override
	public Collection<String> getFacts(){
		Vector<String> facts = new Vector<String>();
		
		facts.add(String.format("(MSPACMAN (mindistancePPill %d))", 
				distanceToPPill));
		
		facts.add(String.format("(MSPACMAN (soloUnaInterseccionPosible %s))", 
				candidateMoves.size() > 1));
		
		facts.add(String.format("(MSPACMAN (variosCaminos %d))", 
				candidateMoves.size()));
		
		facts.add(String.format("(MSPACMAN (quedanPPils %s))", 
						game.getNumberOfActivePowerPills() > 0));
		//TODO: cambiar esto por un booleano
		facts.add(String.format("(MSPACMAN (estoyCercaDePpil %s))", 
				false));
		//TODO: cambiar esto por un booleano
				facts.add(String.format("(MSPACMAN (hayFantasmasCercaDePpil %s))", 
						false));		
		//TODO: cambiar esto por un numero real
		facts.add(String.format("(MSPACMAN (tiempoDesdePpil %d))", 
						0));
		
		
		//DEFINIDAS YA EN LAS RULES
		
		facts.add(String.format("(MSPACMAN (voyGreedy %d))",        
						this.goGreedy));
		
		facts.add(String.format("(MSPACMAN (HayPillEnCaminoInmediato %d))",       
				this.moveToPoints.size()));
		
		facts.add(String.format("(MSPACMAN (hayFantasmasNoComestiblesCerca %s))", 
				this.numFantasmasNoComestiblesCerca));
		
		facts.add(String.format("(MSPACMAN (hayVariosFantasmasNoComestiblesCerca %d))", 
				this.numFantasmasNoComestiblesCerca));
		
		facts.add(String.format("(MSPACMAN (hayVariosFantasmasComestiblesCerca %d))", 
				this.numFantasmasComestiblesCerca));
		
		return facts;
	}
	
	private void setGhostEdible() {
		for (GHOST g : GHOST.values()) {
			if (game.isGhostEdible(g)) {
				ghostEdible.put(g, true);
			} else {
				ghostEdible.put(g, false);
			}
		}
	}
	
	private void setGhostLastMove() {
		for (GHOST g : GHOST.values()) {
			ghostLastMove.put(g, game.getGhostLastMoveMade(g));
		}
	}
	
	private void setNextIntersections() {
		List<MOVE> cM = new ArrayList<>();
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
				if (game.getPillIndex(node) != -1 && game.isPillStillAvailable(game.getPillIndex(node))) {
					count++;
				}

				// Check if there is a power pill between the intersections
				if (game.getPowerPillIndex(node) != -1 && game.isPowerPillStillAvailable(game.getPowerPillIndex(node))) {
					pPill = true;
				}

				// Checks if a ghost
				// NOT EDIBLE
				// NOT GOING OUR DIRECTION is in the way to the node
				// OR IS EDIBLE BUT NOT REACHABLE
				for (GHOST g : GHOST.values()) {
					if ((game.getGhostCurrentNodeIndex(g) == node && dirToMove != ghostLastMove.get(g))
							&& (!ghostEdible.get(g) || (ghostEdible.get(g) && !ghostReachable(g)))) {
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
			cM.add(m);
			moveToGhost.put(m, ghostInPath);
			moveToNode.put(m, node);
			moveToPpill.put(m, pPill);
			if (count != 0) moveToPoints.put(m, count);
		}
		
		candidateMoves = cM;
		
		if(candidateMoves.size() > 1) {
			candidateMoves = filterData();
		}
	}
	
	private List<MOVE> filterData() {
		// Remove all nodes we are not reaching first
		List<MOVE> aux = new ArrayList<>();
		for (MOVE m : candidateMoves) {
			if (!ghostReachUs(moveToNode.get(m))
				&& !moveToGhost.get(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), moveToNode.get(m), game.getPacmanLastMoveMade(), DM.PATH))) {
				aux.add(m);
			}
		}

		return aux;
	}
	
	private void setNearestPPill() {
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

	// We discard candidates when the ghost that is NOT EDIBLE, is CLOSER TO NODE
	// and is MOVING IN THAT DIRECTION.
	private Boolean ghostReachUs(int interNode) {
		double pacmanToNode = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), interNode);
		
		for (GHOST g : GHOST.values()) {
			double ghostToNode = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), interNode);
			if ( ghostToNode != -1 && pacmanToNode >= ghostToNode - Constants.EAT_DISTANCE && !ghostEdible.get(g)) {
				return true;
			}
		}

		return false;
	}
	
	// Speed of ghosts is halved
	private boolean ghostReachable(GHOST ghost) {
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
	
	

	// GETTERS // 
	public boolean isFinishLevel() {
		return finishLevel;
	}

	public Map<MOVE, Integer> getMoveToNode() {
		return moveToNode;
	}

	public Map<MOVE, Integer> getMoveToPoints() {
		return moveToPoints;
	}

	public Map<MOVE, Boolean> getMoveToPpill() {
		return moveToPpill;
	}

	public Map<MOVE, Boolean> getMoveToGhost() {
		return moveToGhost;
	}

	public Map<GHOST, Boolean> getGhostEdible() {
		return ghostEdible;
	}

	public Map<GHOST, MOVE> getGhostLastMove() {
		return ghostLastMove;
	}

	public List<MOVE> getCandidateMoves() {
		return candidateMoves;
	}

	public int getClosestPPill() {
		return closestPPill;
	}

	public double getDistanceToPPill() {
		return distanceToPPill;
	}

	public double getHideDistance() {
		return hideDistance;
	}

}
