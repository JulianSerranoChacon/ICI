package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends RulesInput {
	
	public enum GHOSTTYPE{
		   HUNTER1,
		   HUNTER2,
		   JAILER,
		   RANDOM
	}
	
	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;
	private double minPacmanDistancePPill;
	
	private Map<GHOST,Double> distanceFromGhostToPacman;
	private Map<GHOST,Double> distanceFromPacmanToGhost;
	private Map<GHOST,Map<GHOST,Double>> distanceFromGhostToGhost;
	private Map<GHOST,GHOST> shieldGhost;
	private Map<GHOST,GHOSTTYPE> GhostClass;
	
	public boolean[] behaviourChanged;
	
	public GhostsInput(Game game) {
		super(game);
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
		parseDistanceFromPacManToGhost();
		parseDistanceGhostToGhost();
		parseGhostPriority();
	}
	
	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		//ADD EDIBLE FACTS //
		facts.add(String.format("(BLINKY (edible %s))", this.BLINKYedible));
		facts.add(String.format("(INKY (edible %s))", this.INKYedible));
		facts.add(String.format("(PINKY (edible %s))", this.PINKYedible));
		facts.add(String.format("(SUE (edible %s))", this.SUEedible));
		facts.add(String.format("(MSPACMAN (mindistancePPill %d))", 
								(int)this.minPacmanDistancePPill));
		
		//ADD DISTANCE FROM GHOST TO PACMAN //
		//TODO: Does it have to be int??
		facts.add(String.format("(BLINKY (distanceToPacman %d))", 
				this.distanceFromGhostToPacman.get(GHOST.BLINKY)));
		facts.add(String.format("(INKY (distanceToPacman %d))", 
				this.distanceFromGhostToPacman.get(GHOST.INKY)));
		facts.add(String.format("(PINKY (distanceToPacman %d))", 
				this.distanceFromGhostToPacman.get(GHOST.PINKY)));
		facts.add(String.format("(SUE (distanceToPacman %d))", 
				this.distanceFromGhostToPacman.get(GHOST.SUE)));
		
		//ADD DISTANCE FROM PACMAN TO GHOST //
		facts.add(String.format("(MSPACMAN (distanceToBLINKY %d))", 
				this.distanceFromPacmanToGhost.get(GHOST.BLINKY)));
		facts.add(String.format("(MSPACMAN (distanceToINKY %d))", 
				this.distanceFromPacmanToGhost.get(GHOST.INKY)));
		facts.add(String.format("(MSPACMAN (distanceToPINKY %d))", 
				this.distanceFromPacmanToGhost.get(GHOST.PINKY)));
		facts.add(String.format("(MSPACMAN (distanceToSUE %d))", 
				this.distanceFromPacmanToGhost.get(GHOST.SUE)));
		
		//ADD DISTANCE FROM GHOST TO GHOST //
		//BLINKY
		facts.add(String.format("(BLINKY (distanceToINKY %d))", 
				this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.INKY)));
		facts.add(String.format("(BLINKY (distanceToPINKY %d))", 
				this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.PINKY)));
		facts.add(String.format("(BLINKY (distanceToSUE %d))", 
				this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.SUE)));
		//INKY
		facts.add(String.format("(INKY (distanceToBLINKY %d))", 
				this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.BLINKY)));
		facts.add(String.format("(INKY (distanceToPINKY %d))", 
				this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.PINKY)));
		facts.add(String.format("(INKY (distanceToSUE %d))", 
				this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.SUE)));
		//PINKY
		facts.add(String.format("(PINKY (distanceToBLINKY %d))", 
				this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.BLINKY)));
		facts.add(String.format("(PINKY (distanceToINKY %d))", 
				this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.INKY)));
		facts.add(String.format("(PINKY (distanceToSUE %d))", 
				this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.SUE)));
		//SUE
		facts.add(String.format("(SUE (distanceToBLINKY %d))", 
				this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.BLINKY)));
		facts.add(String.format("(SUE (distanceToINKY %d))", 
				this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.INKY)));
		facts.add(String.format("(SUE (distanceToPINKY %d))", 
				this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.PINKY)));
		
		//  GHOST ROLES   //
		if(Objects.isNull(this.GhostClass.get(GHOST.BLINKY))) {
			facts.add(String.format("(BLINKY (ghostRole %d))", 
					this.GhostClass.get(GHOST.BLINKY)));			
		}

		if(Objects.isNull(this.GhostClass.get(GHOST.BLINKY))) {
			facts.add(String.format("(INKY (ghostRole %d))", 
					this.GhostClass.get(GHOST.INKY)));
		}

		if(Objects.isNull(this.GhostClass.get(GHOST.BLINKY))) {
			facts.add(String.format("(PINKY (ghostRole %d))", 
					this.GhostClass.get(GHOST.PINKY)));
		}

		if(Objects.isNull(this.GhostClass.get(GHOST.BLINKY))) {
			facts.add(String.format("(SUE (ghostRole %d))", 
					this.GhostClass.get(GHOST.SUE)));
		}					
		
		return facts;
	}
	
	private void parseDistanceFromGhostToPacman() {
		distanceFromGhostToPacman = new HashMap<>();
		for(GHOST g : GHOST.values()) {
			double auxDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g) , game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
			distanceFromGhostToPacman.put(g, auxDistance);
		}
		
	}
	
	private void parseDistanceFromPacManToGhost() {
		distanceFromPacmanToGhost = new HashMap<>();
		
		for(GHOST g : GHOST.values()) {
		if(game.getGhostLairTime(g)==0) {	
			double auxDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),game.getPacmanLastMoveMade());
			distanceFromPacmanToGhost.put(g, auxDistance);
			}
		}
		
	}
	
	private void parseDistanceGhostToGhost() {
		distanceFromGhostToGhost= new HashMap<>();
		for(GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g)==0) {
				Map<GHOST,Double> auxMap = new HashMap<>();
				for(GHOST otherghost : GHOST.values()) {
					if(game.getGhostLairTime(otherghost)==0){ 	
						double auxdistance = game.getShortestPathDistance( game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(otherghost),game.getGhostLastMoveMade(g));
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
		//Roles to be assigned
    	GHOST closerGhostToPacMan = GHOST.BLINKY;
		GHOST jailerGhost = GHOST.BLINKY;
		GHOST secondCloserGhost = GHOST.BLINKY;

		boolean[] asignados = new boolean[4];
		double distance = 0;
		double minDistance = Double.MAX_VALUE;

		
		// ROL CLOSER GHOST TO PACMAN //
    	for (GHOST ghostType : GHOST.values()) {
    		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0) {
    			distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType)); //Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType));
    			 if(distance < minDistance) {
    				minDistance = distance;
    				closerGhostToPacMan = ghostType;
    				asignados[0] = true;
    			}
    		}
    	}
    	
    	// ROL SECOND CLOSEST GHOST //
    	minDistance = Double.MAX_VALUE;
    	
    	//Sacar Segundo Fantasma Cercano
       	for (GHOST ghostType : GHOST.values()) {
    		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0 && ghostType != closerGhostToPacMan) {
    			distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType));
    			if(distance < minDistance) {
    				minDistance = distance;
    				secondCloserGhost = ghostType;
    				asignados[1] = true;
    			}
    		}
    	}	
    	
    	// ROL JAILER //
    	minDistance = Double.MAX_VALUE;
    	distance = 0;
    	
    	//We calculated the future intersection where PacMan is going
		int[] futureNodeMove = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
		
		//In what direction?
		while(futureNodeMove.length <= 1) {
			futureNodeMove = game.getNeighbouringNodes(futureNodeMove[0]);
		}
		
		//Assign the rol to the closest ghost
       	for (GHOST ghostType : GHOST.values()) {
    		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0 && ghostType != closerGhostToPacMan && ghostType != secondCloserGhost) {
    			
    			//Dont select a well placed ghost
    			if(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), game.getGhostCurrentNodeIndex(closerGhostToPacMan), game.getGhostLastMoveMade(ghostType))  <= 5) {
    				break;
    			}
    			
    			distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), futureNodeMove[0], game.getGhostLastMoveMade(ghostType));
    			
      			if( minDistance > distance) {
    				minDistance = distance;
    				jailerGhost = ghostType;
    				asignados[2] = true;
    			}
    		}
    	}
    	
    	//Change the behavior of every ghost if is necessary
    	for(GHOST g : GHOST.values()) {	
    		if(asignados[0] && g == closerGhostToPacMan) {
    			GhostClass.put(g, GHOSTTYPE.HUNTER1);
    		}
    		else if(asignados[2] && g == jailerGhost) {
    			GhostClass.put(g, GHOSTTYPE.JAILER);
    		}
    		else if(asignados[1] && g == secondCloserGhost) {
    			GhostClass.put(g, GHOSTTYPE.HUNTER2);
    		}
    		else {
    			if(asignados[1] == false) {
    				GhostClass.put(g, GHOSTTYPE.HUNTER2);
    				asignados[1] = true; 
    			}
    			else if(asignados[2] == false) {
    				GhostClass.put(g, GHOSTTYPE.JAILER);
    				asignados[2] = true; 
    			}
    			else if(asignados[3] == false){
    				asignados[3] = true;
        			GhostClass.put(g, GHOSTTYPE.RANDOM);
    			}
    		}
    	}
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
