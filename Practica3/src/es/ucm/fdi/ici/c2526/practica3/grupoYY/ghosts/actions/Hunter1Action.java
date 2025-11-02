package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Hunter1Action implements RulesAction {
    GHOST ghost;

	public Hunter1Action( GHOST g) {
		ghost = g;
	}
	
	@Override
	public MOVE execute(Game game) {
		
		if(!game.doesGhostRequireAction(ghost)) return MOVE.NEUTRAL;
		
        
        return game.getApproximateNextMoveTowardsTarget(
         	   game.getGhostCurrentNodeIndex(ghost),
                game.getPacmanCurrentNodeIndex(), 
                game.getGhostLastMoveMade(ghost), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {}
	
	@Override
	public String getActionId() {
		return ghost + "Hunter1";
	}


}
