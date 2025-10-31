package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunToEscuderoAction implements RulesAction {

    GHOST ghost;
    GHOST escudero;
  
	public RunToEscuderoAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
        if (!game.doesGhostRequireAction(ghost)){
        	return MOVE.NEUTRAL;
        }
        
        return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(escudero), game.getGhostLastMoveMade(ghost), DM.PATH);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("extraGhost");
			if(value == null)
				return;
			String ghostValue = value.stringValue(null);
			this.escudero = GHOST.valueOf(ghostValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getActionId() {
		return ghost+ "runToEscudero";
	}

}
