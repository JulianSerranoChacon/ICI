package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class HuntToGreedyTransition implements Transition {
	
	private int privateID;
	private static int idcount = 0;

	public HuntToGreedyTransition() {
		privateID = idcount;
		idcount++;
	}

	@Override
	public boolean evaluate(Input in) {
        MsPacManInput input = (MsPacManInput) in;
        Game game = input.getGame();
        
        for(GHOST g : GHOST.values()) {
        	if(game.isGhostEdible(g) && ghostReachable(game, g)) {
        		return false;
        	}
        }
        
        return true;
	}
	
	private boolean ghostReachable(Game game, GHOST ghost) {
		if(game.getGhostLairTime(ghost) > 0) {
			return false;
		}
		
		double distanceToGhostPosition = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade());
		
		if (game.getGhostLastMoveMade(ghost) != game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH)) {
			return game.getGhostEdibleTime(ghost) >= 2 * distanceToGhostPosition;
		}
		
		return game.getGhostEdibleTime(ghost) >= distanceToGhostPosition;
	}

	@Override
	public String toString() {
		return String.format("there is no ghost close to pacman" + privateID);
	}
}
