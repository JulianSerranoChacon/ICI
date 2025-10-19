package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class RunFromAllToMenacedTransition implements Transition {
	
	private Game game;
	private int closestPPill;
	private static final double DIST_MARGIN = 0;
	
	public RunFromAllToMenacedTransition() {
		
	}
	
	//Check if menaced, then if it is possible to eat ppill
	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		game = input.getGame();
		closestPPill = input.getClosestPPill();
		
		if(closestPPill == -1) {
			return false;
		}
		
        for(GHOST ghost : GHOST.values()) {
        	//Ghost can not be in the lair
            if(game.getGhostLairTime(ghost) > 0) continue; 
            
            //Estimation of the time left after eating first ghost.
            double time_left = Constants.EDIBLE_TIME - distanceToGhostPosition(game, ghost);
            
            //We try to find if we can have a double kill approximately.
            for(GHOST g : GHOST.values()) {
                if(g == ghost || game.getGhostLairTime(g) > 0)  continue;
                
                double dist2 = time_left - distanceTo2GhostPosition(game, g);
                
                if (dist2 > DIST_MARGIN) return true;
            }       
        }
		
		
		return false;
	}
	
	//Pessimistic estimation of the worst distance a ghost can be
	private double distanceToGhostPosition(Game game, GHOST ghost) {
		return game.getShortestPathDistance(closestPPill,
                game.getPacmanCurrentNodeIndex()) + 2 * game.getDistance(closestPPill, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
	}
	
	//Pessimistic estimation of the worst distance a second ghost can be
		private double distanceTo2GhostPosition(Game game, GHOST ghost) {
			return 2 * game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost),
                    game.getGhostCurrentNodeIndex(ghost));
		}

	@Override
	public String toString() {
		return String.format("Go to Ppill");
	}
}
