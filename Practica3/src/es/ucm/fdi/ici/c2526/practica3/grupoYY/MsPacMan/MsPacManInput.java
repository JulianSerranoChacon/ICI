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
	
	private int numEateableGhosts = 0;
	private int numDangerGhost = 0;
	private boolean llegoAntesAPPill = true;


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
		setGhostLastMove();
		setGhostEdible();
		setNextIntersections();
		setNearestPPill();
	}
	
	//Tenemos que rellenar con los datos que usan las reglas de PacMan para moverse
	@Override
	public Collection<String> getFacts(){
		parseInput();
		Vector<String> facts = new Vector<String>();
		
		//MISSPACMAN
		String pacmanData = "(MSPACMAN ";
		pacmanData += (String.format("(hayPillEnCaminoInmediato %s)",hayPillCaminoInmediato));

		pacmanData += (String.format("(minDistancePpill %d)", (int)minDistancePpill));

		pacmanData += (String.format("(variosCaminos %d)", isCandidateMove.size()));

		
		pacmanData += (String.format("(quedanPPils %d)", (int)game.getNumberOfActivePowerPills()));
		
		pacmanData += (String.format("(tiempoDesdePpil %d)", (int)tiempoDesdePpill)); 

		pacmanData += (String.format("(distanceToBLINKY %d)", (int)distanceToBlinky));
				

		pacmanData += (String.format("(distanceToINKY %d)", (int)distanceToINKY));


		pacmanData += (String.format("(llegoAntesAPPil %s)", this.llegoAntesAPPill));
		
		pacmanData += (String.format("(distanceToPINKY %d)", (int)distanceToPINKY));
		
		pacmanData += (String.format("(numDangerGhosts %d)", this.numDangerGhost));
		
		pacmanData += (String.format("(numEatableGhost %d)", this.numEateableGhosts));

		pacmanData += (String.format("(distanceToSUE %d)", (int)distanceToSUE));

		pacmanData += (String.format("(distanceToEatBLINKY %d)", (int)timeToEat(GHOST.BLINKY)));
		
		pacmanData += (String.format("(distanceToEatINKY %d)", (int)timeToEat(GHOST.INKY)));

		pacmanData += (String.format("(distanceToEatPINKY %d)", (int)timeToEat(GHOST.PINKY)));
		
		pacmanData += (String.format("(distanceToEatSUE %d)",  (int)timeToEat(GHOST.SUE)));
		
		//TODO: parametizar
		pacmanData += (String.format("(dangerDistanceGhost %d)", (int)dangerDistance));
		
		for(MOVE m: MOVE.values()) {
			if(moveToPpill.containsValue(m))
				pacmanData += (String.format("(goToPillMove %s)", m.toString()));
		}
		
		pacmanData += (String.format("(RIGHTCandidate %s)", isCandidateMove.containsKey(MOVE.RIGHT)));
		pacmanData += (String.format("(LEFTCandidate %s)", isCandidateMove.containsKey(MOVE.LEFT)));
		pacmanData += (String.format("(UPCandidate %s)", isCandidateMove.containsKey(MOVE.UP)));
		pacmanData += (String.format("(DOWNCandidate %s)", isCandidateMove.containsKey(MOVE.DOWN)));
		
		pacmanData += (String.format("(RIGHTMoveToPpill %s)", moveToPpill.containsKey(MOVE.RIGHT)));
		pacmanData += (String.format("(LEFTMoveToPpill %s)", moveToPpill.containsKey(MOVE.LEFT)));
		pacmanData += (String.format("(UPMoveToPpill %s)", moveToPpill.containsKey(MOVE.UP)));
		pacmanData += (String.format("(DOWNMoveToPpill %s)", moveToPpill.containsKey(MOVE.DOWN)));
		
		if( moveToPoints.containsKey(MOVE.RIGHT))
			pacmanData += (String.format("(RIGHTMoveToPoints %d)", moveToPoints.get(MOVE.RIGHT).intValue()));
		if( moveToPoints.containsKey(MOVE.LEFT))
			pacmanData += (String.format("(LEFTMoveToPoints %d)", moveToPoints.get(MOVE.LEFT).intValue()));
		if( moveToPoints.containsKey(MOVE.UP))
			pacmanData += (String.format("(UPMoveToPoints %d)", moveToPoints.get(MOVE.UP).intValue()));
		if( moveToPoints.containsKey(MOVE.DOWN))
			pacmanData += (String.format("(DOWNMoveToPoints %d)", moveToPoints.get(MOVE.DOWN).intValue()));
		
		if(moveToNode.containsKey(MOVE.RIGHT))
			pacmanData += (String.format("(RIGHTMoveToNode %d)", moveToNode.get(MOVE.RIGHT).intValue()));
		if(moveToNode.containsKey(MOVE.LEFT))
			pacmanData += (String.format("(LEFTMoveToNode %d)", moveToNode.get(MOVE.LEFT).intValue()));
		if(moveToNode.containsKey(MOVE.UP))
			pacmanData += (String.format("(UPMoveToNode %d)", moveToNode.get(MOVE.UP).intValue()));
		if(moveToNode.containsKey(MOVE.DOWN))
			pacmanData += (String.format("(DOWNMoveToNode %d)", moveToNode.get(MOVE.DOWN).intValue()));
	
		pacmanData += (String.format("(ClosestPpil %d)", (int) closestPPill));
		
		pacmanData += ")";
		facts.add(pacmanData);
		
		
		String BlinkyData = "(BLINKY ";
		//BLINKY
		BlinkyData += (String.format("(edible %s)", game.isGhostEdible(GHOST.BLINKY)));

		BlinkyData += (String.format("(minDistancePacMan %d)", (int)BLINKYdistancePacMan));
		
		BlinkyData += (String.format("(minDistancePpill %d)", (int)BLINKYMinDistanceToPpill));
		
		BlinkyData += ")";
		facts.add(BlinkyData);
		
		String InkyData = "(INKY ";
		//INKY
		InkyData += (String.format("(edible %s)", game.isGhostEdible(GHOST.INKY)));

		InkyData += (String.format("(minDistancePacMan %d)", (int)INKYdistancePacMan));
		
		InkyData += (String.format("(minDistancePpill %d)", (int)INKYMinDistanceToPpill));

		InkyData += ")";
		facts.add(InkyData);
		
		String PinkyData = "(PINKY ";
		//PINKY
		PinkyData += (String.format("(edible %s)", game.isGhostEdible(GHOST.PINKY)));

		PinkyData += (String.format("(minDistancePacMan %d)", (int)PINKYdistancePacMan));
	
		PinkyData += (String.format("(minDistancePpill %d)", (int)PINKYMinDistanceToPpill));
		PinkyData += ")";
		facts.add(PinkyData);

		//SUE
		String sueData = "(SUE ";
		sueData += (String.format("(edible %s)", game.isGhostEdible(GHOST.SUE)));

		sueData += (String.format("(minDistancePacMan %d)", (int)SUEdistancePacMan));
		
		sueData += (String.format("(minDistancePpill %d)", (int)SUEMinDistanceToPpill));
		sueData += ")";
		facts.add(sueData);
		

		return facts;
	}
	
	private void setGhostEdible() {
		for (GHOST g : GHOST.values()) {
			if (game.isGhostEdible(g)) {
				ghostEdible.put(g, true);
				tiempoDesdePpill = Constants.EDIBLE_TIME - game.getGhostEdibleTime(g);
				if(game.getDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(g), DM.MANHATTAN) <= this.timeToEat(g)) {
					this.numEateableGhosts++;
				}
			} else {
				ghostEdible.put(g, false);
				if(game.getDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), DM.MANHATTAN) <= this.dangerDistance) {
					this.numDangerGhost++;
				}
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
		for(MOVE c : candidateMoves) {
			this.isCandidateMove.put(c, true);
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
        minDistancePpill = 60;
	    if (!(game.getNumberOfActivePowerPills() == 0)) {
	        for (int pill : ppills) {
	            double aux = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
	            if (aux < minDistancePpill && game.isPowerPillStillAvailable(pill)) {
	                closestPPill = pill;
	                minDistancePpill = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
	            }
	        }
	        if(closestPPill != -1) {
		        setGhostDistanceToPPIL(); //Once the ppil is chosen we set the distance to the ghost to this PPIL
		        if(this.minDistancePpill > this.BLINKYMinDistanceToPpill || this.minDistancePpill > this.INKYMinDistanceToPpill  ||
		        		this.minDistancePpill > this.PINKYMinDistanceToPpill || this.minDistancePpill > this.SUEMinDistanceToPpill ) {
		        	this.llegoAntesAPPill = false;
		        }
	        }
	    }
	    else {
	    	this.llegoAntesAPPill = false;
	    }
	}

	private void setGhostDistanceToPPIL() {
		for(GHOST g: GHOST.values()) {
			switch(g) {
			case BLINKY:
				BLINKYMinDistanceToPpill = game.getDistance(game.getGhostCurrentNodeIndex(g), closestPPill, DM.MANHATTAN);
				break;
			case INKY:
				INKYMinDistanceToPpill = game.getDistance(game.getGhostCurrentNodeIndex(g), closestPPill, DM.MANHATTAN);
				break;
			case PINKY:
				PINKYMinDistanceToPpill = game.getDistance(game.getGhostCurrentNodeIndex(g), closestPPill, DM.MANHATTAN);
				break;
			case SUE:
				SUEMinDistanceToPpill = game.getDistance(game.getGhostCurrentNodeIndex(g), closestPPill, DM.MANHATTAN);
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
		double distance =game.getDistance( game.getGhostCurrentNodeIndex(g),game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(g), dm); 
		return distance == -1? 0: distance;
	}
	
	//method to not repeat the same line
	double getGhostDistance(GHOST g, DM dm){
		double distance = -1;
		if(game.getGhostLairTime(g) <= 0)
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),game.getGhostLastMoveMade(g), dm);
		return distance == -1? 0: distance;
	}
	
	private double timeToEat(GHOST ghost) {
		if(game.getGhostLairTime(ghost) <= 0)
			return game.getShortestPathDistance(game.getPacManInitialNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade()) - 2;
		return 0;
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
