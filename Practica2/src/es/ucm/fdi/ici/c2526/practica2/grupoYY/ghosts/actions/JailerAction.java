package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo.GHOSTTYPE;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class JailerAction implements Action{
    GHOST ghost;
    GhostInfo gI;
	public JailerAction( GHOST ghost, GhostInfo gi) {
		this.ghost = ghost;
		gI = gi;
	}

	@Override
	public MOVE execute(Game game) {    

		if(!game.doesGhostRequireAction(ghost))
			return MOVE.NEUTRAL;

    	MOVE moveToReturn = MOVE.NEUTRAL;

   		int[] futureNodeMove = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
		
		while(futureNodeMove.length <= 1) {
			futureNodeMove = game.getNeighbouringNodes(futureNodeMove[0]);
		}
	
    	moveToReturn = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), futureNodeMove[0], DM.PATH);
    	return moveToReturn;
	}

	@Override
	public String getActionId() {
		return ghost + "Jailer";
	}
}
