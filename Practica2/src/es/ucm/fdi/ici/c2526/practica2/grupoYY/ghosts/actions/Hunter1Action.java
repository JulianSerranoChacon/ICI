package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Hunter1Action implements Action {
    GHOST ghost;
    int references;
	public Hunter1Action( GHOST ghost) {
		this.ghost = ghost;
		this.references = 0;
	}


	public void addReference() {
		this.references++;
	}
	
	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
            return game.getApproximateNextMoveTowardsTarget(
            	   game.getGhostCurrentNodeIndex(ghost),
                   game.getPacmanCurrentNodeIndex(), 
                   game.getGhostLastMoveMade(ghost), DM.PATH);
        }
        return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return ghost + "Hunter1 " + this.references;
	}
}
