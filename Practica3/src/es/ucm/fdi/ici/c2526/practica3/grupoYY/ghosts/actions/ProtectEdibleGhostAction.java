package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ProtectEdibleGhostAction implements RulesAction {

    GHOST ghost;
    GHOST ghostToCover;
    
	public ProtectEdibleGhostAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
        if (!game.doesGhostRequireAction(ghost)){
        	return MOVE.NEUTRAL;
        }
        
        return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(ghostToCover), game.getGhostLastMoveMade(ghost), DM.PATH);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("extraGhost");
			if(value == null)
				return;
			String ghostValue = value.stringValue(null);
			this.ghostToCover = GHOST.valueOf(ghostValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getActionId() {
		return ghost+ "runToTheEdible";
	}

}
