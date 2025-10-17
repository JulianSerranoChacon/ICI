package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunSubOptimalAction implements Action {

	//SI ESTO DEVUELVE UN CAMINO EN EL QUE HAY UN FANTASMA Y SE PODÍA IR POR OTRO (REFACTORIZAR)
    GHOST ghost;
    MOVE[] possibleMoves;
    private GhostInfo info;
	public RunSubOptimalAction(GHOST ghost,GhostInfo g) {
		this.ghost = ghost;
		this.possibleMoves = MOVE.values();
		this.info = g;
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	
        	//We take the best Move to flee of PacMan, this move can not be done because there is a Ghost there too
              MOVE bestRunMove = game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),
                      				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
              
             
              MOVE moveToReturn = MOVE.NEUTRAL; //Which move should return
              double minDistance = 10000; //Save the farther distance of PacMan
              
              
              //For every possible Move
              for(MOVE mv : possibleMoves) {
            	  
            	  double distance = 0;
            	  
            	  //If it is not the bestMove (because there is a Ghost there, checks which move is the farthest of all to run away of PacMan
            	  if(mv != bestRunMove) {
            		  
            		  distance = info.getDistanceFromGhostToPacman(ghost);
 
            		  if(distance < minDistance) {
            			  minDistance = distance;
            			  moveToReturn = mv;
            		  }
            		  
            	  }
              }
              return moveToReturn;
        }
            
        return MOVE.NEUTRAL;	
	}

	@Override
	public String getActionId() {
		return ghost+ "RunSubOptimal";
	}
}
