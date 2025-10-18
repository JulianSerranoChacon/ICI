package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ProtectEdibleGhostAction implements Action {

    GHOST ghost;
    GHOST[] allGhosts;
    private GhostInfo info;
	public ProtectEdibleGhostAction(GHOST ghost,GhostInfo g) {
		this.ghost = ghost;
		this.allGhosts = GHOST.values();
		this.info = g;
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	
           GHOST edible = GHOST.BLINKY;
           int minDistance = 10000;
           
           //Search the edible Ghost who is closest to PacMan
           for(GHOST actGhost : allGhosts) {
        	   int distance = 0; 
        	   if(game.isGhostEdible(actGhost)) {
        		   
        		   distance = info.getDistanceFromGhostToPacman(actGhost);
        		   
        		   if(distance < minDistance) {
        			   minDistance = distance;
        			   edible = actGhost;
        		   }
        	   }
           }
           
           //Return the best move to get to the edible ghost 
           return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(this.ghost),
        		   game.getGhostCurrentNodeIndex(edible), game.getGhostLastMoveMade(this.ghost), DM.PATH);
        }
            
        return MOVE.NEUTRAL;	
	}

	@Override
	public String getActionId() {
		return ghost+ "runToEscudero";
	}
}
