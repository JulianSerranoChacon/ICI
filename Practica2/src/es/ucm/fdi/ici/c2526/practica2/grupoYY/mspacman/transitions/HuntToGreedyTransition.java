package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
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
        	if(game.isGhostEdible(g) 
        			&& game.getGhostEdibleTime(g) < 2 * game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), game.getPacmanLastMoveMade())) {
        		return false;
        	}
        }
        
        return true;
	}

	@Override
	public String toString() {
		return String.format("there is no ghost close to pacman" + privateID);
	}
}
