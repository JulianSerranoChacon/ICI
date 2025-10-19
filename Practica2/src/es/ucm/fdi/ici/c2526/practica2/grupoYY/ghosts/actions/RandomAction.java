package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo.GHOSTTYPE;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import java.util.Random;

public class RandomAction implements Action{
    GHOST ghost;
    GhostInfo gI;
    private Random rnd = new Random();
    
	public RandomAction( GHOST ghost, GhostInfo gi) {
		this.ghost = ghost;
		gI = gi;
	}

	@Override
	public MOVE execute(Game game) {

		if(!game.doesGhostRequireAction(ghost))
			return MOVE.NEUTRAL;
		
		int rand = rnd.nextInt(0,100);
    	if(rand < 33) {
    		return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),DM.PATH);
    	}
    	else {
    		MOVE[] availabeMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost));
        	return availabeMoves[rnd.nextInt(0,availabeMoves.length)];
    	}
	}

	@Override
	public String getActionId() {
		return ghost + "Random";
	}
}
