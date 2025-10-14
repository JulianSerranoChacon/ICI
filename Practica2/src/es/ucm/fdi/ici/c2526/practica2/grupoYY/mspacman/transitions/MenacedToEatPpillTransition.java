package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class MenacedToEatPpillTransition implements Transition {
	
	private Game game;
	private int closestPPill;
	
	public MenacedToEatPpillTransition() {
		
	}
	
	//Check if menaced, then if it is possible
	//TODO:Refactor pleaseee
	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		
		game = input.getGame();
		closestPPill = input.getClosestPPill();
		
		if(input.getMoveToPpill().size() != 0) {
			//Estimation of the worst distance a ghost can be
	        try {
	            int time_left = Constants.EDIBLE_TIME;
	
	            for(GHOST ghost : GHOST.values()) {
	                if(game.getGhostLairTime(ghost) > 0) 
	                    continue;
	                double distanceToGhostPosition = game.getShortestPathDistance(closestPPill,
	                       game.getPacmanCurrentNodeIndex()) + 2 * game.getDistance(closestPPill, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
	                if (time_left > distanceToGhostPosition) {
	                    time_left -= distanceToGhostPosition;
	                    for(GHOST g : GHOST.values()) {
	                        if(g == ghost || game.getGhostLairTime(ghost) > 0) 
	                            continue;
	                        double distanceToSecondGhostPosition = 2 * game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g),
	                                game.getGhostCurrentNodeIndex(ghost));
	                        if (time_left > distanceToSecondGhostPosition) {
	                            return true;
	                        }
	                    }
	                }
	            }
	        }
	        catch(Exception e) {
	
	        }
		}
		
		return false;
	}

	@Override
	public String toString() {
		return String.format("Eat Ppill");
	}
}
