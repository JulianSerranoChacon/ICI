package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import java.util.Objects;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class JailerAction implements RulesAction{
    GHOST ghost;
    int targetNode = 0;
	public JailerAction( GHOST ghost) {
		this.ghost = ghost;
		
	}

	@Override
	public MOVE execute(Game game) {    
		if(!game.doesGhostRequireAction(ghost))
			return MOVE.NEUTRAL;
		
    	MOVE moveToReturn = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),targetNode, DM.PATH);
    	
    	return moveToReturn;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value target = actionFact.getSlotValue("intersection");
			if(!Objects.isNull(target)) {
				targetNode =  target.intValue(null);
			}
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getActionId() {
		return ghost + "Jailer";
	}

}
