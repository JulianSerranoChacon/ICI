package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo.GHOSTTYPE;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
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
	private Map<GHOST,GHOSTTYPE> GhostClass;
	
	 public boolean[] behaviourChanged; 
	private GhostInfo gi;
	
	public GhostsInput(Game game,GhostInfo g) {
		super(game);
		gi = g;
		fillInfo();
	}
	private void fillInfo() {
		gi.setFromPacmanToGhost(distanceFromPacmanToGhost);
		gi.setDistanceFromGhostToGhost(distanceFromGhostToGhost);
		gi.setGhostClass(GhostClass);
		gi.setFromGhostToPacMan(distanceFromGhostToPacman);
	}
	private void parseDistanceFromGhostToPacman() {
		distanceFromGhostToPacman = new HashMap<>();
		for(GHOST g : GHOST.values()) {
			int auxDistance = game.getApproximateShortestPathDistance(game.getGhostCurrentNodeIndex(g) , game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
			distanceFromGhostToPacman.put(g, auxDistance);
		}
		
	}
	
	private void parseFromPacManToGhost() {
		distanceFromPacmanToGhost = new HashMap<>();
		
		for(GHOST g : GHOST.values()) {
		if(game.getGhostLairTime(g)==0) {	int auxDistance = game.getApproximateShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),game.getPacmanLastMoveMade());
			distanceFromPacmanToGhost.put(g, auxDistance);
			}
		}
		
	}
	
	private void parseDistanceGhostToGhost() {
		distanceFromGhostToGhost= new HashMap<>();
		for(GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g)==0) {
				Map<GHOST,Integer> auxMap = new HashMap<>();
				for(GHOST otherghost : GHOST.values()) {
					if(game.getGhostLairTime(otherghost)==0){ 	
						int auxdistance = game.getApproximateShortestPathDistance( game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(otherghost),game.getGhostLastMoveMade(g));
						auxMap.put(otherghost, auxdistance);
					}
				}
			distanceFromGhostToGhost.put(g, auxMap);
			
			}
		}
	}
	
	  public void swapBehaviour(int indexA, int indexB) {
	    	GHOSTTYPE aux = GhostClass.get(indexA);
	    	GhostClass.remove(indexA);
	    	GhostClass.put(GHOST.values()[indexA], GhostClass.get(indexB));
	    	GhostClass.remove(indexB);
	    	GhostClass.put(GHOST.values()[indexB], aux);
	    }
	private void parseGhostPriority() {
    	
		GhostClass = new HashMap<>();
    	GHOST closerGhostToPacMan = GHOST.BLINKY;
		GHOST jailerGhost = GHOST.BLINKY;;
		GHOST secondCloserGhost = GHOST.BLINKY;;

		int distance = 0;
		int minDistance = 100000;

		
		//Sacar Fantasma cercano
    	for (GHOST ghostType : GHOST.values()) {
    		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0) {
    			distance = Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType));
    			 if(distance < minDistance) {
    				minDistance = distance;
    				closerGhostToPacMan = ghostType;
    			}
    		}
    	}
    	
    	minDistance = 100000;
    	//Sacar jailer
    	//We calculated the future intersection where PacMan
		int[] futureNodeMove = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
		
		while(futureNodeMove.length <= 1) {
			futureNodeMove = game.getNeighbouringNodes(futureNodeMove[0]);
		}

       	for (GHOST ghostType : GHOST.values()) {
    		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0 && ghostType != closerGhostToPacMan) {
    			distance = Math.abs(futureNodeMove[0]  - game.getGhostCurrentNodeIndex(ghostType));
      			if( minDistance > distance) {
    				minDistance = distance;
    				jailerGhost = ghostType;
    			}
    			if(ghostType != closerGhostToPacMan && ghostType != jailerGhost && distance < minDistance) {
    				minDistance = distance;
    				secondCloserGhost = ghostType;
    			}
    		}
    	}
    	
    	minDistance = 100000;
    	//Sacar Segundo Fantasma Cercano
       	for (GHOST ghostType : GHOST.values()) {
    		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0 && ghostType != closerGhostToPacMan && ghostType != jailerGhost) {
    			distance = Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType));
    			if(distance < minDistance) {
    				minDistance = distance;
    				secondCloserGhost = ghostType;
    			}
    		}
    	}
    	
    	//Change the behavior of every ghost if is necessary
    	for(GHOST g : GHOST.values()) {	
    		if(g == closerGhostToPacMan) {
    			GhostClass.put(g, GHOSTTYPE.HUNTER1);
    		}
    		else if(g == jailerGhost) {
    			GhostClass.put(g, GHOSTTYPE.JAILER);
    		}
    		else if(g == secondCloserGhost) {
    			GhostClass.put(g, GHOSTTYPE.HUNTER2);
    		}
    		else {
    			GhostClass.put(g, GHOSTTYPE.RANDOM);
    		}
    	}
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
