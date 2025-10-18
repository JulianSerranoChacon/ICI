package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class StartRunning implements Action {

	GHOST ghost;
	MOVE[] possibleMoves;
	public StartRunning(GHOST ghost) {
		this.ghost = ghost;
		this.possibleMoves = MOVE.values();
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	//We take the best Move to "Follow" PacMan, this move can not be done because we want to escape from PacMan when he eats the PPill
            MOVE bestTowardsMove = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
                    				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
            
           
            MOVE moveToReturn = MOVE.NEUTRAL; //Which move should return
            double minDistance = 10000; //Save the closest distance through PacMan
            
            
            //For every possible Move
            for(MOVE mv : possibleMoves) {
          	  
          	  double distance = 0;
          	  
          	  //If it is not the bestMove (because Pacman is there, checks which move is the closest of all to "Follow" PacMan
          	  if(mv != bestTowardsMove) {
          		  
          		  distance = game.getApproximateShortestPathDistance(game.getGhostCurrentNodeIndex(this.ghost), game.getPacmanCurrentNodeIndex(), mv);

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
		return ghost+ "statrRunning";
	}
}
