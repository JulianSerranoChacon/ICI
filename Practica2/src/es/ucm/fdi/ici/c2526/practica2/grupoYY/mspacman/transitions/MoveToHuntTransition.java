package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class MoveToHuntTransition implements Transition {
	
	private int privateID;
	private static int idcount = 0;
	
	public MoveToHuntTransition() {
		privateID = idcount;
		idcount++;
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		
		Game game = input.getGame();

		for (GHOST ghost : GHOST.values()) {
			if (game.isGhostEdible(ghost) && ghostReachable(game, ghost)) {
				return true;
			} 
		}
		return false;
	}
	
	private boolean ghostReachable(Game game, GHOST ghost) {
		//return 2 * game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH) 
		//		<= game.getGhostEdibleTime(ghost);
		
		if(game.getGhostLairTime(ghost) > 0) {
			return false;
		}
		
		double distanceToGhostPosition = game.getDistance(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH);
		
		if (game.getGhostLastMoveMade(ghost) != game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH)) {
			return game.getGhostEdibleTime(ghost) >= 2 * distanceToGhostPosition;
		}
		
		return game.getGhostEdibleTime(ghost) >= distanceToGhostPosition;
	}
	
	@Override
	public String toString() {
		return String.format("Move to hunt" + privateID);
	}
}
