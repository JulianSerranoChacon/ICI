package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class OrbitateAction implements Action {

    GHOST ghost;
    double orbitDistance; 
    MOVE[] possibleMoves;
    double[] distancesMoves;
    
	public OrbitateAction(GHOST ghost) {
		this.ghost = ghost;
		this.orbitDistance = 80; 
		this.possibleMoves = MOVE.values();
		this.distancesMoves = new double[4];
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
           //Checks the new value of the limit distance
           this.orbitDistance = (game.getGhostEdibleTime(this.ghost)/2) + 1; //Initial distance is 101 because 100 is the limit when pacMan can eat us before die so in the end when edible time is 0 the limit will be 1
           System.out.println("CUAL ES MI DISTANCIA DE ORBITA? " + this.orbitDistance);
           
           //We take the closer intersection distance from our actual spot and checks if we move there if we are in danger or not
           for(int i = 0; i < possibleMoves.length; ++i) {
        	   
        	    //We take the neighbour nodes of the current Ghost node position
          		int[] futureNodeMove = game.getNeighbouringNodes(game.getGhostCurrentNodeIndex(this.ghost),possibleMoves[i]);
          		
          		//We search for the closest node intersection
        		while(futureNodeMove.length <= 1) {
        			futureNodeMove = game.getNeighbouringNodes(futureNodeMove[0]);
        		//if(futureNodeMove == null) break; Si se peta
        			System.out.println("SOY ORBITA Y BUSCO MIS VECINOS"); //Por saber si entra ya que futureNodeMove esta en una interseccion al principio entonces a lo mejor no entra en este while lo que sería liada
        		}
        		
        		//Calculate the future distance between that intersection and PacMan when the ghost arrives there
        	   double distanceBetweenIntersectionsGhosts =  game.getDistance(game.getGhostCurrentNodeIndex(ghost), futureNodeMove[0], possibleMoves[i], DM.PATH); //Distance from Ghost to there (is PacMan move / 2)
        	   double distanceBetweenIntersectionsPacMan =  game.getDistance(game.getPacmanCurrentNodeIndex(), futureNodeMove[0], DM.PATH); //Distance from PacMan to the intersection 
        	   double diff = Math.abs(distanceBetweenIntersectionsPacMan - (distanceBetweenIntersectionsGhosts * 2)); //Take if 
        	   
        	   if(diff >= this.orbitDistance) {
        		   distancesMoves[i] = diff;
        	   }
        	   else {
        		   distancesMoves[i] = -1;
        	   }
        	   
           }
           
           double minor = 100000; //Minor Value
           int minorIndex = 0; //Index of the minor movement
           boolean canMove = false; //Checks if there is a possible move
           
           //For the valid Movements (ones that make the ghost keep in the safe spot) checks which one is the closer one to that limit
           for(int i = 0; i < distancesMoves.length;++i ) {
        	   if(distancesMoves[i] != -1) {
        		   canMove = true;
        		   if(distancesMoves[i] < minor) {
        			   minor = distancesMoves[i];
        			   minorIndex = i;
        		   }
        	   }
           }
           if(canMove) return possibleMoves[minorIndex]; //Returns the best Move only if exists
           //else return Neutral
        }
            
        return MOVE.NEUTRAL;	
	}

	@Override
	public String getActionId() {
		return ghost+ "Orbit";
	}
}
