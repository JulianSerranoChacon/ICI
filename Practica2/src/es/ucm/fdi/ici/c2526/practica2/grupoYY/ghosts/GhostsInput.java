package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends Input {

	

	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;
	private double minPacmanDistancePPill;
	
	private Map<GHOST,Integer> distanceFromGhostToPacman;
	private Map<GHOST,Integer> distanceFromPacmanToGhost;
	private Map<GHOST,Map<GHOST,Integer>> distanceFromGhostToGhost;
	private Map<GHOST,GHOST> shieldGhost;
	private List<GHOST> ghostPriority;
	
	private GhostInfo gi;
	
	public GhostsInput(Game game,GhostInfo gi) {
		super(game);
		this.gi = gi;
	}
	private void parseDistanceFromGhostToPacman() {
		distanceFromGhostToPacman = new HashMap<>();
		for(GHOST g : GHOST.values()) {
			int auxDistance = game.getApproximateShortestPathDistance(game.getGhostCurrentNodeIndex(g) , game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
			distanceFromGhostToPacman.put(g, auxDistance);
		}
		this.gi.setFromGhostToPacMan(distanceFromGhostToPacman);
	}
	
	private void parseFromPacManToGhost() {
		distanceFromPacmanToGhost = new HashMap<>();
		
		for(GHOST g : GHOST.values()) {
			int auxDistance = game.getApproximateShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),game.getPacmanLastMoveMade());
			distanceFromPacmanToGhost.put(g, auxDistance);
		}
		this.gi.setFromPacmanToGhost(distanceFromPacmanToGhost);
	}
	
	private void parseDistanceGhostToGhost() {
		distanceFromGhostToGhost= new HashMap<>();
		for(GHOST g : GHOST.values()) {
			Map<GHOST,Integer> auxMap = new HashMap<>();
			for(GHOST otherghost : GHOST.values()) {
				int auxdistance = game.getApproximateShortestPathDistance( game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(otherghost),game.getGhostLastMoveMade(g));
				auxMap.put(otherghost, auxdistance);
			}
			distanceFromGhostToGhost.put(g, auxMap);
			this.gi.setDistanceFromGhostToGhost(distanceFromGhostToGhost);
		}
	}
	
	private void parseGhostPriority() {
		TreeMap<Integer,GHOST>auxMap = new TreeMap<>();
		distanceFromGhostToPacman.forEach((key,element) -> auxMap.put(element, key));
		auxMap.forEach((key,element)-> ghostPriority.add(element));
		
		this.gi.setGhostPriority(ghostPriority);
	}
	@Override
	public void parseInput() {
		this.BLINKYedible = game.isGhostEdible(GHOST.BLINKY);
		this.INKYedible = game.isGhostEdible(GHOST.INKY);
		this.PINKYedible = game.isGhostEdible(GHOST.PINKY);
		this.SUEedible = game.isGhostEdible(GHOST.SUE);
	
		int pacman = game.getPacmanCurrentNodeIndex();
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for(int ppill: game.getPowerPillIndices()) {
			double distance = game.getDistance(pacman, ppill, DM.PATH);
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
		}
		parseDistanceFromGhostToPacman();
		parseFromPacManToGhost();
		parseDistanceGhostToGhost();
		 parseGhostPriority();
		
	}

	public boolean isBLINKYedible() {
		return BLINKYedible;
	}

	public boolean isINKYedible() {
		return INKYedible;
	}

	public boolean isPINKYedible() {
		return PINKYedible;
	}

	public boolean isSUEedible() {
		return SUEedible;
	}

	public double getMinPacmanDistancePPill() {
		return minPacmanDistancePPill;
	}



	
	
}
