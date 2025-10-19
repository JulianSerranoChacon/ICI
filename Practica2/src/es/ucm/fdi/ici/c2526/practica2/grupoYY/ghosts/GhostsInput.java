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
		behaviourChanged = new boolean[4];
  	    behaviourChanged[0] = false;
  	    behaviourChanged[1] = false;
  	    behaviourChanged[2] = false;
  	    behaviourChanged[3] = false;
    	int index = 0; //Index for all the ghosts
    	
    	//Change the behavior of every ghost if is necessary
    	for(GHOST g : GHOST.values()) {	
    		
    		if(behaviourChanged[index] == false) {
    			//if the ghost is the HUNTER1
            	if(GhostClass.get(g) == GHOSTTYPE.HUNTER1) {	
            		
            		int i = 0; //Index to find the HunterGhost
            		int distance = 0;
            		int minDistance = 1000000000;
            		int closerGhostIndex = 0;
            		
                	for (GHOST ghostType : GHOST.values()) {
                		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0 && behaviourChanged[i] == false) {
                			if(i == index) {
                				distance = Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType));
                			}
                			else if(minDistance > Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType))) {
                				minDistance = Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType));
                				closerGhostIndex = i;
                			}
                		}
            			i++;
                	}
                	
                
                	//Si distancia del cazador fantasma a PacMan > distancia de otro fantasma a PacMan Añade el comportamiento de ese otro fantasma a aux
                	if(distance > minDistance) {
                		swapBehaviour(index,closerGhostIndex);
                		index--;
                		behaviourChanged[closerGhostIndex] = true;
          
                	}
        		}
        		else if(GhostClass.get(g) == GHOSTTYPE.HUNTER2) {
        			
            		int i = 0; //Index to find the HunterGhost2
            		int distance = 0;
            		int minDistance = 1000000000;
            		int closerGhostIndex = 0;
            		
                	for (GHOST ghostType : GHOST.values()) {
                		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0 && behaviourChanged[i] == false) {
                			if(i == index) {
                				distance = Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType));
                			}
                			else if(GhostClass.get(g) != GHOSTTYPE.HUNTER1 && minDistance > Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType))) {
                				minDistance = Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType));
                				closerGhostIndex = i;
                			}
                		}
            			i++;
                	}
                	
                	//Si distancia del cazador fantasma a PacMan > distancia de otro fantasma a PacMan Añade el comportamiento de ese otro fantasma a aux
                	if(distance > minDistance) {
                		swapBehaviour(index,closerGhostIndex);
                		index--;
                		behaviourChanged[closerGhostIndex] = true;
                	}
        		}
        		else if(GhostClass.get(g) == GHOSTTYPE.JAILER) {
        			
        			int i = 0; //Index to find the HunterGhost2
            		int minDistance = 1000000000;
            		int closerGhostIndex = 0;
            		GHOST actJailer = GHOST.PINKY;
            		
            		
        	    	GHOST FleePacManGhost = GHOST.BLINKY;
        	    	
        	    	//We search the closest Ghost and the Jailer Ghost
        	    	for (GHOST ghostType : GHOST.values()) {
        	    		if(GhostClass.get(g) == GHOSTTYPE.HUNTER1) {
        	    			FleePacManGhost = ghostType;
        	    		}
        	    		else if(GhostClass.get(g) == GHOSTTYPE.JAILER) {
        	    			actJailer = ghostType;
        	    		}
        	    		++i;
        	    	}
        	    	
        	    	
        	    	//If jailer is not in prision
        	    	if(!game.isGhostEdible(actJailer) && game.getGhostLairTime(actJailer) <= 0 && behaviourChanged[index] == false) {
        	    		
        	    		//We get the Move of Pac-Man
        	    		MOVE bestPacManMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(FleePacManGhost),DM.PATH);
        	    		
        	    		//We calculated the future intersection where PacMan
        	    		int[] futureNodeMove = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
        	    		
        	    		while(futureNodeMove.length <= 1) {
        	    			futureNodeMove = game.getNeighbouringNodes(futureNodeMove[0]);
        	    		}
        	    	
       
        	    		i = 0;
        	    		for(GHOST ghostType: GHOST.values()) {
        	    			if(minDistance > Math.abs(futureNodeMove[0]  - game.getGhostCurrentNodeIndex(ghostType))) {
        	    				minDistance = Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType));
        	    				closerGhostIndex = i;
        	    			}
        	    		 i++;
        	    		}
        	    	
        	    		//Si distancia del jailer al camino de huida de PacMan > distancia de otro fantasma a dicho camino Añade el comportamiento de ese otro fantasma a aux
        	    		if(futureNodeMove[0] > minDistance) {
        	    			swapBehaviour(index,closerGhostIndex);
        	    			index--;
        	    			behaviourChanged[closerGhostIndex] = true;
        	    		}
        	    	}
        	    }	 	
        	} 
        	index++;
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
