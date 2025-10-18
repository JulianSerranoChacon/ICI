package es.ucm.fdi.ici.c2526.practica1.GrupoH;

import java.util.EnumMap;
import java.util.Random;

import pacman.game.Constants;
import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class Ghosts extends GhostController {
	
   private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
   private MOVE[] allMoves = MOVE.values();
   private Random rnd = new Random();
   private int limit = 70; //Limit distance between PacMan and the powerPill 
   private int DistanceGhosts = 150; //Limit distance between Ghosts in the same road
		   
   private enum GHOSTTYPE{
	   HUNTER1,
	   HUNTER2,
	   JAILER,
	   RANDOM
   }
   
   public GHOSTTYPE[] behaviours = new GHOSTTYPE[4];
   public boolean[] behaviourChanged = new boolean[4];
   
   public Ghosts() {
	   
	   behaviours[0] = GHOSTTYPE.JAILER;
	   behaviours[1] = GHOSTTYPE.HUNTER1;
	   behaviours[2] = GHOSTTYPE.RANDOM;
	   behaviours[3] = GHOSTTYPE.HUNTER2;
	   
	   behaviourChanged[0] = false;
	   behaviourChanged[1] = false;
	   behaviourChanged[2] = false;
	   behaviourChanged[3] = false;
   }
    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
    	
        moves.clear();
        int index = 0;
        
        for (GHOST ghostType : GHOST.values()) {
        	
            if (game.doesGhostRequireAction(ghostType) && game.getGhostLairTime(ghostType) <= 0) {
            	
            	boolean escape = false;
            	
            	//Compare each ActivePowerPill, if PacMan is close to one the ghosts must run away
            	int i = 0;
            	boolean found = false;
            	while(!escape && i < game.getNumberOfActivePowerPills()) {
            		if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),game.getActivePowerPillsIndices()[i], game.getPacmanLastMoveMade()) < limit) {
            			if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),game.getActivePowerPillsIndices()[i], game.getPacmanLastMoveMade()) > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType),game.getActivePowerPillsIndices()[i], game.getGhostLastMoveMade(ghostType))) {
            				found = true;
            				moves.put(ghostType,game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), game.getActivePowerPillsIndices()[i], DM.PATH));
            				break;
            			}
            			else {
                			escape = true;
                			//End of while	
            			}
            		}
            		++i;
            	}
            	
            	if(found) break;
            	//Assignation of the ghosts move depending on escape boolean
            	
               	if (game.isGhostEdible(ghostType)) {
               		moves.put(ghostType,EscapeBehaviour(game,ghostType));
               	}
               	else if(escape) {
               		moves.put(ghostType,GhostsRun(game,ghostType));
               	}
               	
            	else {
            		
            		if(behaviours[index] == GHOSTTYPE.HUNTER1) {
            			moves.put(ghostType,Hunter1Behaviour(game,ghostType));
            		}
            		else if(behaviours[index] == GHOSTTYPE.HUNTER2) {
            			moves.put(ghostType,Hunter2Behaviour(game,ghostType));
            		}
            		else if(behaviours[index] == GHOSTTYPE.JAILER) {
            			moves.put(ghostType,JailerBehaviour(game,ghostType));
            		}
            		else {
            			moves.put(ghostType,RandomBehaviour(game,ghostType));
            		}
            		
            	}
 
            }
            index++;
        }
        modifiedBehaviours(game);
        return moves;
    }
    
    public String getName() {
    	return "Ghosts";
    }
    public MOVE GhostsAggresive(Game game,GHOST ghost) {
    	
    	return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),DM.PATH);
    }
    public MOVE GhostsRun(Game game,GHOST ghost) {
    	
    	return game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),DM.PATH);
    }
    
    public void swapBehaviour(int indexA, int indexB) {
    	GHOSTTYPE aux = behaviours[indexA];
    	behaviours[indexA] = behaviours[indexB];
    	behaviours[indexB] = aux;
    }
    
    public MOVE Hunter1Behaviour(Game game, GHOST ghost) {
    	return GhostsAggresive(game,ghost);
    }
    
    
	public MOVE Hunter2Behaviour(Game game, GHOST ghost) {
    	MOVE bestMove = GhostsAggresive(game,ghost);
    	MOVE moveToReturn = MOVE.NEUTRAL;  
    	double minDistance = 10000000;
    	MOVE[] possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
    	
    	double distanceBetweenHunters = 0;
    	int index = 0;
    	
       	for (GHOST ghostType : GHOST.values()) {
       		if(behaviours[index] == GHOSTTYPE.HUNTER1) {
       			distanceBetweenHunters = game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
       			break;
       		}
       		++index;
       	}
    	
    	for(int i = 0; i < possibleMoves.length; ++i) {
    		//If both hunters are to close the second hunter must not take the closest Move to PacMan
    		if(distanceBetweenHunters < DistanceGhosts && possibleMoves[i] != bestMove) {
    			if(minDistance > game.getApproximateShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i])) {
    				minDistance = game.getApproximateShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i]);
    				moveToReturn = possibleMoves[i];
    			}
    		}
    		// else follow Pac-Man
    		else {
    			if(minDistance > game.getApproximateShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i])) {
    				minDistance = game.getApproximateShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), possibleMoves[i]);
    				moveToReturn = possibleMoves[i];
    			}
    		}
    	}
    	return moveToReturn;	
    }
    
    
    public MOVE JailerBehaviour(Game game, GHOST ghost) {
    	
    	int index = 0;
    	MOVE bestPacManMove = MOVE.NEUTRAL;
    	MOVE moveToReturn = MOVE.NEUTRAL;
    	GHOST FleePacManGhost = GHOST.BLINKY;
    	for (GHOST ghostType : GHOST.values()) {
    		if(behaviours[index] == GHOSTTYPE.HUNTER1) {
    			FleePacManGhost = ghostType;
    		}
    		++index;
    	}
    	bestPacManMove = game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(FleePacManGhost),game.getPacmanLastMoveMade(),DM.PATH);
   		int[] futureNodeMove = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
		
		while(futureNodeMove.length <= 1) {
			futureNodeMove = game.getNeighbouringNodes(futureNodeMove[0]);
		}
	
    	moveToReturn = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), futureNodeMove[0], DM.PATH);
    	return moveToReturn;
    	
    }
    
    public MOVE RandomBehaviour(Game game,GHOST ghost) {
    	int rand = rnd.nextInt(0,100);
    	if(rand < 33) {
    		return GhostsAggresive(game,ghost);
    	}
    	else {
        	return moves.put(ghost, allMoves[rnd.nextInt(0,4)]);
    	}
    }
    
    public MOVE EscapeBehaviour(Game game,GHOST ghost) {
    	
    	MOVE escapeMove = GhostsRun(game,ghost); 
    	for(GHOST ghostType : GHOST.values()) {
    		if(ghost != ghostType && game.isGhostEdible(ghostType) && game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(ghostType),escapeMove ,DM.PATH) < DistanceGhosts / 2) {
    			escapeMove = game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(ghostType),DM.PATH);
    			break;
    		}
    	}
    	return escapeMove;
    }
    public void modifiedBehaviours(Game game) {
    	
  	    behaviourChanged[0] = false;
  	    behaviourChanged[1] = false;
  	    behaviourChanged[2] = false;
  	    behaviourChanged[3] = false;
    	int index = 0; //Index for all the ghosts
    	
    	//Change the behavior of every ghost if is necessary
    	while (index < 4) {	
    		
    		if(behaviourChanged[index] == false) {
    			//if the ghost is the HUNTER1
            	if(behaviours[index] == GHOSTTYPE.HUNTER1) {	
            		
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
        		else if(behaviours[index] == GHOSTTYPE.HUNTER2) {
        			
            		int i = 0; //Index to find the HunterGhost2
            		int distance = 0;
            		int minDistance = 1000000000;
            		int closerGhostIndex = 0;
            		
                	for (GHOST ghostType : GHOST.values()) {
                		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0 && behaviourChanged[i] == false) {
                			if(i == index) {
                				distance = Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType));
                			}
                			else if(behaviours[index] != GHOSTTYPE.HUNTER1 && minDistance > Math.abs(game.getPacmanCurrentNodeIndex() - game.getGhostCurrentNodeIndex(ghostType))) {
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
        		else if(behaviours[index] == GHOSTTYPE.JAILER) {
        			
        			int i = 0; //Index to find the HunterGhost2
            		int minDistance = 1000000000;
            		int closerGhostIndex = 0;
            		GHOST actJailer = GHOST.PINKY;
            		
            		
        	    	GHOST FleePacManGhost = GHOST.BLINKY;
        	    	
        	    	//We search the closest Ghost and the Jailer Ghost
        	    	for (GHOST ghostType : GHOST.values()) {
        	    		if(behaviours[i] == GHOSTTYPE.HUNTER1) {
        	    			FleePacManGhost = ghostType;
        	    		}
        	    		else if(behaviours[i] == GHOSTTYPE.JAILER) {
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
}
