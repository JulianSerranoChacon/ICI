package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MoveAction implements RulesAction {


	public MoveAction() {

	}
	
	@Override
	public MOVE execute(Game game) {
		//return pi.getCandidateMoves().get(0);
		return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}
	
	@Override
	public String getActionId() {
		return "Only move possible action";
	}

}
