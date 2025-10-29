package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import java.util.Random;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SearchShieldAction implements RulesAction {
    GHOST ghost;
    private Random rnd = new Random();
    
	public SearchShieldAction( GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {

		  if (game.doesGhostRequireAction(ghost))        //if it requires an action
	        {
	        	
	           GHOST shield = GHOST.BLINKY;
	           int minDistance = 10000;
	           for(GHOST actGhost : allGhosts) {
	        	   int distance = 0; 
	        	   if(!game.isGhostEdible(actGhost)) {
	        		   
	        		   distance = info.getDistanceFromGhostToGhost(this.ghost,actGhost);
	        		   
	        		   if(distance < minDistance) {
	        			   minDistance = distance;
	        			   shield = actGhost;
	        		   }
	        	   }
	           }
	           
	           return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(this.ghost),
	        		   game.getGhostCurrentNodeIndex(shield), game.getGhostLastMoveMade(this.ghost), DM.PATH);
	        }
	            
	        return MOVE.NEUTRAL;	
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getActionId() {
		return ghost + "Random";
	}

}
