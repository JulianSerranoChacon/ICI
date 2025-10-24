package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseAction implements RulesAction {


	public ChaseAction( ) {

	}

	@Override
	public MOVE execute(Game game) {
     
 
        return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return "chases";
	}

	

}
