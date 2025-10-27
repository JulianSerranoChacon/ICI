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
    private Random rnd = new Random();
    
	public RandomAction( GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {

		if(!game.doesGhostRequireAction(ghost))
			return MOVE.NEUTRAL;
		
		int rand = rnd.nextInt(100);
    	if(rand < 33) {
    		return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),DM.PATH);
    	}
    	else {
    		MOVE[] availabeMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost));
        	return availabeMoves[rnd.nextInt(availabeMoves.length)];
    	}
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
