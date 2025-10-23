package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class JailerAction implements RulesAction{
    GHOST ghost;

	public JailerAction( GHOST ghost) {
		this.ghost = ghost;
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
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getActionId() {
		return ghost + "Jailer";
	}

}
