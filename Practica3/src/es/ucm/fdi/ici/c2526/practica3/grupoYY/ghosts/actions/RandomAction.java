package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import java.util.Random;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RandomAction implements RulesAction {
    GHOST ghost;
    int limitDistance = 40;
    private Random rnd = new Random();
    
	public RandomAction( GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {

		if(!game.doesGhostRequireAction(ghost))
			return MOVE.NEUTRAL;
		
		// If no ppills available or we are to far, we go to PacMan
    	if(game.getActivePowerPillsIndices().length==0 || limitDistance < game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex())) {
    		return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),DM.PATH);
    	}
    	else {
    		MOVE[] availableMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost));
        	return availableMoves[rnd.nextInt(availableMoves.length)];
    	}
	}

	@Override
	public void parseFact(Fact actionFact) {}

	@Override
	public String getActionId() {
		return ghost + "Random";
	}

}
