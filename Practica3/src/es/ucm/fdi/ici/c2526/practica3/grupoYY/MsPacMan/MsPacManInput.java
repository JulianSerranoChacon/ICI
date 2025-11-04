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
import pacman.game.internal.Ghost;
import pacman.game.Game;

public class MsPacManInput extends RulesInput {

	// Data gathered TODO: Maybe erase this and have everything on pi
	boolean finishLevel;
	private Map<MOVE, Integer> moveToNode;
	private Map<MOVE, Integer> moveToPoints;
	private Map<MOVE, Boolean> moveToPpill;
	private Map<MOVE, Boolean> moveToGhost;
	private Map<GHOST, Boolean> ghostEdible;
	private Map<GHOST, MOVE> ghostLastMove;
	private List<MOVE> candidateMoves;
	private Map<MOVE, Boolean> isCandidateMove;
	private int closestPPill; //Index of the closer PPIL of PacMan
	private double minDistancePpill; //PacMan distance to Closer PPil
	private final double dangerDistance = 30; //tentative, subject to change 
	private final double hideDistance = 30; //tentative, subject to change 
	private boolean hayPillCaminoInmediato;
	private double tiempoDesdePpill = -1; //Time since PacMan eat the PPIL
	
	//Distance from PacMan to the different Ghosts
	private double distanceToBlinky;
	private double distanceToINKY;
	private double distanceToPINKY;
	private double distanceToSUE;
	
	private double distanceToEatBlinky;
	private double distanceToEatINKY;
	private double distanceToEatPINKY;
	private double distanceToEatSUE;
	
	//Distance from all the ghosts to PacMan and the closer Ppil of PacMan
	private double BLINKYdistancePacMan;
	private double BLINKYMinDistanceToPpill;
	private double INKYdistancePacMan;
	private double INKYMinDistanceToPpill;
	private double PINKYdistancePacMan;
	private double PINKYMinDistanceToPpill;
	private double SUEdistancePacMan;
	private double SUEMinDistanceToPpill;


	public double getDangerDistance() {
		return dangerDistance;
	}

	public MsPacManInput(Game game) {
		super(game);
		initializeVars();
	}

	private void reset() {
		initializeVars();
	}

	private void initializeVars(){

		moveToPoints = new HashMap<>();
		moveToPpill = new HashMap<>();
		moveToGhost = new HashMap<>();
		moveToNode = new HashMap<>();
		ghostEdible = new HashMap<>();
		ghostLastMove = new HashMap<>();
		candidateMoves = new ArrayList<>();
		isCandidateMove = new HashMap<>();
		hayPillCaminoInmediato = false;
		tiempoDesdePpill = -1;
		distanceToBlinky = getGhostDistance(GHOST.BLINKY, DM.PATH);
		distanceToINKY = getGhostDistance(GHOST.INKY, DM.PATH);
		distanceToPINKY = getGhostDistance(GHOST.PINKY,DM.PATH);
		distanceToSUE = getGhostDistance(GHOST.SUE, DM.PATH);
		
		//distanceToEatBlinky = 
		//distanceToEatINKY = 
		//distanceToEatPINKY = 
		//distanceToEatSUE = 
		
		BLINKYdistancePacMan = getPacManDistance(GHOST.BLINKY,DM.PATH);
		INKYdistancePacMan = getPacManDistance(GHOST.INKY,DM.PATH);
		PINKYdistancePacMan = getPacManDistance(GHOST.PINKY,DM.PATH);
		SUEdistancePacMan = getPacManDistance(GHOST.SUE,DM.PATH);
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
		
		//MISSPACMAN
		
		facts.add(String.format("(MSPACMAN (hayPillCaminoInmediato %s))",hayPillCaminoInmediato));

		facts.add(String.format("(MSPACMAN (minDistancePPill %s))", minDistancePpill));

		facts.add(String.format("(MSPACMAN (variosCaminos %d))", candidateMoves.size()));

		
		facts.add(String.format("(MSPACMAN (numPpills %n))", game.getNumberOfActivePowerPills()));
		
		facts.add(String.format("(MSPACMAN (tiempoDesdePpill %n))", tiempoDesdePpill));

		facts.add(String.format("(MSPACMAN (distanceToBLINKY %n))", distanceToBlinky));
				

		facts.add(String.format("(MSPACMAN (distanceToINKY %n))", distanceToINKY));


		facts.add(String.format("(MSPACMAN (distanceToPINKY %n))", distanceToPINKY));
		

		facts.add(String.format("(MSPACMAN (distanceToSUE %n))", distanceToSUE));

		facts.add(String.format("(MSPACMAN (distanceToEatBLINKY %n))", timeToEat(GHOST.BLINKY)));
		
		facts.add(String.format("(MSPACMAN (distanceToEatINKY %n))", timeToEat(GHOST.INKY)));

		facts.add(String.format("(MSPACMAN (distanceToEatPINKY %n))", timeToEat(GHOST.PINKY)));
		
		facts.add(String.format("(MSPACMAN (distanceToEatSUE %n))",  timeToEat(GHOST.SUE)));
		
		//TODO: parametizar
		facts.add(String.format("(MSPACMAN (dangerDistance %n))", dangerDistance));
		
		
		//BLINKY
		facts.add(String.format("(BLINKY (edible %s))", game.isGhostEdible(GHOST.BLINKY)));

		facts.add(String.format("(BLINKY (minDistanceToPacman %n))", BLINKYdistancePacMan));
		
		facts.add(String.format("(BLINKY (minDistanceToPpill %n))", BLINKYMinDistanceToPpill));
		
		//INKY
		facts.add(String.format("(INKY (edible %s))", game.isGhostEdible(GHOST.INKY)));

		facts.add(String.format("(INKY (minDistanceToPacman %n))", INKYdistancePacMan));
		
		facts.add(String.format("(INKY (minDistanceToPpill %n))", INKYMinDistanceToPpill));

		//PINKY
		facts.add(String.format("(PINKY (edible %s))", game.isGhostEdible(GHOST.PINKY)));

		facts.add(String.format("(PINKY (minDistanceToPacman %n))", PINKYdistancePacMan));
		
		facts.add(String.format("(PINKY (minDistanceToPpill %n))", PINKYMinDistanceToPpill));

		//SUE
		facts.add(String.format("(SUE (edible %s))", game.isGhostEdible(GHOST.SUE)));

		facts.add(String.format("(SUE (minDistanceToPacman %n))", SUEdistancePacMan));
		
		facts.add(String.format("(SUE (minDistanceToPpill %n))", SUEMinDistanceToPpill));
		
		for(MOVE m: MOVE.values()) {
			if(moveToPpill.containsValue(m))
				facts.add(String.format("(MSPACMAN (goToPillMove %s))", m));
		}
		
		facts.add(String.format("(MSPACMAN (RIGHTCandidate %s))", isCandidateMove.get(MOVE.RIGHT)));
		facts.add(String.format("(MSPACMAN (LEFTCandidate %s))", isCandidateMove.get(MOVE.LEFT)));
		facts.add(String.format("(MSPACMAN (UPCandidate %s))", isCandidateMove.get(MOVE.UP)));
		facts.add(String.format("(MSPACMAN (DOWNCandidate %s))", isCandidateMove.get(MOVE.DOWN)));
		
		facts.add(String.format("(MSPACMAN (RIGHTMoveToPpill %s))", moveToPpill.get(MOVE.RIGHT)));
		facts.add(String.format("(MSPACMAN (LEFTMoveToPpill %s))", moveToPpill.get(MOVE.LEFT)));
		facts.add(String.format("(MSPACMAN (UPMoveToPpill %s))", moveToPpill.get(MOVE.UP)));
		facts.add(String.format("(MSPACMAN (DOWNMoveToPpill %s))", moveToPpill.get(MOVE.DOWN)));
		

		facts.add(String.format("(MSPACMAN (RIGHTMoveToPoints %d))", moveToPoints.get(MOVE.RIGHT)));
		facts.add(String.format("(MSPACMAN (LEFTMoveToPoints %d))", moveToPoints.get(MOVE.LEFT)));
		facts.add(String.format("(MSPACMAN (UPMoveToPoints %d))", moveToPoints.get(MOVE.UP)));
		facts.add(String.format("(MSPACMAN (DOWNMoveToPoints %d))", moveToPoints.get(MOVE.DOWN)));
		
		facts.add(String.format("(MSPACMAN (RIGHTMoveToNode %d))", moveToNode.get(MOVE.RIGHT)));
		facts.add(String.format("(MSPACMAN (LEFTMoveToNode %d))", moveToNode.get(MOVE.LEFT)));
		facts.add(String.format("(MSPACMAN (UPTMoveToNode %d))", moveToNode.get(MOVE.UP)));
		facts.add(String.format("(MSPACMAN (DOWNTMoveToNode %d))", moveToNode.get(MOVE.DOWN)));
	
		facts.add(String.format("(MSPACMAN (ClosestPpil %d))", closestPPill));
		return facts;
	}
	
	private void setGhostEdible() {
		for (GHOST g : GHOST.values()) {
			if (game.isGhostEdible(g)) {
				ghostEdible.put(g, true);
				tiempoDesdePpill = Constants.EDIBLE_TIME - game.getGhostLairTime(g);
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
		
		for(MOVE m : MOVE.values())
			isCandidateMove.put(m,false);

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
					hayPillCaminoInmediato = true;
					minDistancePpill = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getPowerPillIndex(node), DM.PATH);
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
		
		for(MOVE m: candidateMoves)
			isCandidateMove.put(m, true);
		
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
        minDistancePpill = Double.MAX_VALUE;
	    if (!(game.getNumberOfActivePowerPills() == 0)) {
	        for (int pill : ppills) {
	            double aux = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
	            if (aux < minDistancePpill) {
	                closestPPill = pill;
	                minDistancePpill = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
	            }
	        }
	        setGhostDistanceToPPIL(); //Once the ppil is chosen we set the distance to the ghost to this PPIL
	    }
	}

	private void setGhostDistanceToPPIL() {
		for(GHOST g: GHOST.values()) {
			switch(g) {
			case GHOST.BLINKY:
				BLINKYMinDistanceToPpill = game.getDistance(game.getGhostCurrentNodeIndex(g), closestPPill, DM.PATH);
				break;
			case GHOST.INKY:
				INKYMinDistanceToPpill = game.getDistance(game.getGhostCurrentNodeIndex(g), closestPPill, DM.PATH);
				break;
			case GHOST.PINKY:
				PINKYMinDistanceToPpill = game.getDistance(game.getGhostCurrentNodeIndex(g), closestPPill, DM.PATH);
				break;
			case GHOST.SUE:
				SUEMinDistanceToPpill = game.getDistance(game.getGhostCurrentNodeIndex(g), closestPPill, DM.PATH);
				break;
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
	
	

	double getPacManDistance(GHOST g, DM dm){
		return game.getDistance( game.getGhostCurrentNodeIndex(g),game.getPacmanCurrentNodeIndex(), dm);
	}
	
	//method to not repeat the same line
	double getGhostDistance(GHOST g, DM dm){
		return game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), dm);
	}
	
	private double timeToEat(GHOST ghost) {
		if(game.getGhostLairTime(ghost) <= 0)
			return 2 * game.getShortestPathDistance(game.getPacManInitialNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade());
		return -1;
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

	public double getMinDistancePpill() {
		return minDistancePpill;
	}

	public double getHideDistance() {
		return hideDistance;
	}

}
