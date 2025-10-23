package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayAction implements RulesAction {

    GHOST ghost;
    enum STRATEGY {RANDOM, CORNER, JAIL};
    STRATEGY runAwayStrategy; 
	public RunAwayAction() {
		
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("runawaystrategy");
			if(value == null)
				return;
			String strategyValue = value.stringValue(null);
			runAwayStrategy = STRATEGY.valueOf(strategyValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public MOVE execute(Game game) {
 
        return MOVE.NEUTRAL;	
	}
	
	@Override
	public String getActionId() {
		return ghost+ "runsAway";
	}
}
