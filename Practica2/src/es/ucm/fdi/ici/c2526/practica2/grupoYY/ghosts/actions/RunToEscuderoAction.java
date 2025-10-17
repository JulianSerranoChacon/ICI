package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunToEscuderoAction implements Action {

    GHOST ghost;
    GHOST[] allGhosts;
	public RunToEscuderoAction(GHOST ghost) {
		this.ghost = ghost;
		this.allGhosts = GHOST.values();
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	
           GHOST shield = GHOST.BLINKY;
           double minDistance = 10000;
           for(GHOST actGhost : allGhosts) {
        	   double distance = 0; 
        	   if(!game.isGhostEdible(actGhost)) {
        		   
        		   distance = game.getDistance(game.getGhostCurrentNodeIndex(this.ghost), game.getGhostCurrentNodeIndex(actGhost), DM.PATH);
        		   
        		   if(distance < minDistance) {
        			   minDistance = distance;
        			   shield = actGhost;
        		   }
        	   }
           }
           
           return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(this.ghost), game.getGhostCurrentNodeIndex(shield), game.getGhostLastMoveMade(this.ghost), DM.PATH);
        }
            
        return MOVE.NEUTRAL;	
	}

	@Override
	public String getActionId() {
		return ghost+ "runToEscudero";
	}
}
