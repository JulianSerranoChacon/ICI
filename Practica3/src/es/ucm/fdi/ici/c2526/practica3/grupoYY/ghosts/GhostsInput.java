package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends RulesInput {
	
	public enum GHOSTTYPE{
		   HUNTER1,
		   HUNTER2,
		   JAILER,
		   RANDOM
	}
	
	private double minPacmanDistancePPill;
	private double pacmanBestNextNode;
	private double pacmanDistanceToBestNextNode;
	
	private double pacmanRequestAccion;
	
	private Map<GHOST,Double> distanceFromGhostToPacman;
	private Map<GHOST,Double> distanceFromPacmanToGhost;
	private Map<GHOST,Map<GHOST,Double>> distanceFromGhostToGhost;
	private Map<GHOST,Double> ghostToIntersection; 
	
	public boolean[] behaviourChanged;
	
	public GhostsInput(Game game) {
		super(game);
		reset();
	}
	
	private void reset() {
		distanceFromGhostToPacman = new HashMap<>();
		distanceFromPacmanToGhost = new HashMap<>();
		distanceFromGhostToGhost = new HashMap<>();
		ghostToIntersection = new HashMap<>();
		pacmanDistanceToBestNextNode = 100000;
		pacmanRequestAccion = 0;
	}
	
	@Override
	public void parseInput() {
		reset();
		
		//Gathers the distance of the closest ppill
		int pacman = game.getPacmanCurrentNodeIndex();
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for(int ppill: game.getActivePowerPillsIndices()) {
			double distance = game.getDistance(pacman, ppill, game.getPacmanLastMoveMade(), DM.PATH);
			if( this.minPacmanDistancePPill > distance) {
				this.minPacmanDistancePPill = distance;
			}
		}
		parsePacmanRequestAction();
		parseDistanceFromGhostToPacman();
		parseDistanceFromPacManToGhost();
		parseDistanceGhostToGhost();
		parseGhostToIntersection();
	}
	
	@Override
	public Collection<String> getFacts() {
		parseInput();
		Vector<String> facts = new Vector<String>();
		String pacmanData = "(MSPACMAN ";
		
		pacmanData += (String.format("(distanceToBlinky %f)"		, this.distanceFromPacmanToGhost.get(GHOST.BLINKY)));
		pacmanData += (String.format("(distanceToPinky %f)"   		, this.distanceFromPacmanToGhost.get(GHOST.PINKY)));
		pacmanData += (String.format("(distanceToInky %f)"			, this.distanceFromPacmanToGhost.get(GHOST.INKY)));
		pacmanData += (String.format("(distanceToSue %f)"			, this.distanceFromPacmanToGhost.get(GHOST.SUE)));
		pacmanData += (String.format("(closestIntersection %d)"		, (int) this.pacmanBestNextNode));
		pacmanData += (String.format("(distanceToClosestPPill %d)"	, (int) this.minPacmanDistancePPill));
		pacmanData += (String.format("(distanceToIntersection %d)" , (int) this.pacmanDistanceToBestNextNode));
		pacmanData += (String.format("(inCorridor %d)" , (int) this.pacmanRequestAccion));
		pacmanData += ")";
		facts.add(pacmanData);

		// BLINKY
		String blinkyData = "(BLINKY ";
		
		blinkyData += (String.format("(distanceToPacman %f)"		, this.distanceFromGhostToPacman.get(GHOST.BLINKY)));
		blinkyData += (String.format("(distanceToPinky %f)"   		, this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.PINKY)));
		blinkyData += (String.format("(distanceToInky %f)"			, this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.INKY)));
		blinkyData += (String.format("(distanceToSue %f)"			, this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.SUE)));
		blinkyData += (String.format("(distanceToIntersection %f)"	, this.ghostToIntersection.get(GHOST.BLINKY)));
		blinkyData += (String.format("(edibleTime %d)"				, this.game.getGhostEdibleTime(GHOST.BLINKY)));
		blinkyData += (String.format("(lairTime %d)"				, this.game.getGhostLairTime(GHOST.BLINKY)));
		
		blinkyData += ")";
		facts.add(blinkyData);
		
		// INKY
		String inkyData = "(INKY ";
		
		inkyData += (String.format("(distanceToPacman %f)"			, this.distanceFromGhostToPacman.get(GHOST.INKY)));
		inkyData += (String.format("(distanceToBlinky %f)"   		, this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.BLINKY)));
		inkyData += (String.format("(distanceToPinky %f)"			, this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.PINKY)));
		inkyData += (String.format("(distanceToSue %f)"				, this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.SUE)));
		inkyData += (String.format("(distanceToIntersection %f)"	, this.ghostToIntersection.get(GHOST.INKY)));
		inkyData += (String.format("(edibleTime %d)"				, this.game.getGhostEdibleTime(GHOST.INKY)));
		inkyData += (String.format("(lairTime %d)"					, this.game.getGhostLairTime(GHOST.INKY)));
		
		inkyData += ")";
		facts.add(inkyData);

		// PINKY
		String pinkyData = "(PINKY ";
		
		pinkyData += (String.format("(distanceToPacman %f)"			, this.distanceFromGhostToPacman.get(GHOST.PINKY)));
		pinkyData += (String.format("(distanceToBlinky %f)"   		, this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.BLINKY)));
		pinkyData += (String.format("(distanceToInky %f)"			, this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.INKY)));
		pinkyData += (String.format("(distanceToSue %f)"			, this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.SUE)));
		pinkyData += (String.format("(distanceToIntersection %f)"	, this.ghostToIntersection.get(GHOST.PINKY)));
		pinkyData += (String.format("(edibleTime %d)"				, this.game.getGhostEdibleTime(GHOST.PINKY)));
		pinkyData += (String.format("(lairTime %d)"					, this.game.getGhostLairTime(GHOST.PINKY)));
		
		pinkyData += ")";
		facts.add(pinkyData);

		// SUE
		String sueData = "(SUE ";
		
		sueData += (String.format("(distanceToPacman %f)"			, this.distanceFromGhostToPacman.get(GHOST.SUE)));
		sueData += (String.format("(distanceToBlinky %f)"   		, this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.BLINKY)));
		sueData += (String.format("(distanceToInky %f)"				, this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.INKY)));
		sueData += (String.format("(distanceToPinky %f)"			, this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.PINKY)));
		sueData += (String.format("(distanceToIntersection %f)"		, this.ghostToIntersection.get(GHOST.SUE)));
		sueData += (String.format("(edibleTime %d)"					, this.game.getGhostEdibleTime(GHOST.SUE)));
		sueData += (String.format("(lairTime %d)"					, this.game.getGhostLairTime(GHOST.SUE)));
		
		sueData += ")";
		facts.add(sueData);

		return facts;
	}
	private void parsePacmanRequestAction() {
		if(game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex())>2) pacmanRequestAccion = 1;
		else pacmanRequestAccion = 0;
	}
	private void parseDistanceFromGhostToPacman() {
		distanceFromGhostToPacman = new HashMap<>();
		for(GHOST g : GHOST.values()) {
			double auxDistance = 0;
			if(game.getGhostLairTime(g) > 0) {
				auxDistance = Integer.MAX_VALUE;
			}
			else {
				auxDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g) , game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
			}
			distanceFromGhostToPacman.put(g, auxDistance);
		}
		
	}
	
	private void parseDistanceFromPacManToGhost() {
		distanceFromPacmanToGhost = new HashMap<>();
		
		for(GHOST g : GHOST.values()) {
			double auxDistance = 0;
			if(game.getGhostLairTime(g) > 0) {
				auxDistance = Integer.MAX_VALUE;
			}
			else {
				auxDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),game.getPacmanLastMoveMade());
			}
			distanceFromPacmanToGhost.put(g, auxDistance);
		}
		
	}
	
	private void parseDistanceGhostToGhost() {
		distanceFromGhostToGhost= new HashMap<>();
		for(GHOST g : GHOST.values()) {
			Map<GHOST,Double> auxMap = new HashMap<>();
			for(GHOST otherghost : GHOST.values()) {
				double auxdistance = 0;
				if(game.getGhostLairTime(otherghost) > 0 || game.getGhostLairTime(g) > 0) { 	
					auxdistance = Integer.MAX_VALUE;
				}
				else {
					auxdistance = game.getShortestPathDistance( game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(otherghost),game.getGhostLastMoveMade(g));
				}
				auxMap.put(otherghost, auxdistance);
			}
			distanceFromGhostToGhost.put(g, auxMap);
		}
	}
	private void parseGhostToIntersection() {
		Set<Integer> auxSet = new HashSet<>();
		Map<Integer, Integer> nodeToPoints = new HashMap<>();
		int bestNode = game.getPacmanCurrentNodeIndex(); double distFromBest = Double.MAX_VALUE;
		int node = game.getPacmanCurrentNodeIndex();

		for (MOVE m : game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
			// Reset everything
			int count = 0;
			node = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);
			MOVE dirToMove = m;

			while (!game.isJunction(node)) {

				// Gather number of pills between our node and the node searching
				if (game.getPillIndex(node) != -1 && game.isPillStillAvailable(game.getPillIndex(node))) {
					count++;
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
				} 
				else {
					node = game.getNeighbour(node, dirToMove);
				}
				auxSet.add(node);
			}
			
			for(int ppillNode : game.getActivePowerPillsIndices()) {
				double distFromNode = game.getDistance(node, ppillNode, DM.PATH);
				if(distFromBest >  distFromNode  && game.isPowerPillStillAvailable(ppillNode)) {
					bestNode = node;
					distFromBest = distFromNode;
				}
			}
			
			if (count != 0) {
				nodeToPoints.put(node, count);
			}
		}
		
		if(game.getActivePowerPillsIndices().length == 0) {
			int maxPoints = 0;
			for(Map.Entry<Integer, Integer> points : nodeToPoints.entrySet()) {
				if(maxPoints < points.getValue()) {
					bestNode = points.getKey();
					maxPoints = points.getValue();
				}
			}
		}
		
		for(GHOST g : GHOST.values()) {
			double auxDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), bestNode, game.getGhostLastMoveMade(g));
			ghostToIntersection.put(g, auxDistance);
		}
		
		pacmanBestNextNode = bestNode;
		pacmanDistanceToBestNextNode = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), bestNode, game.getPacmanLastMoveMade());
		
	}
	
}
	

