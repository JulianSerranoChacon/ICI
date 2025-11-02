package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.Map.Entry;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PPillSuicidaAction implements RulesAction {

	public PPillSuicidaAction() {
		
	}
	
	//Sabemos que tiene que haber una Ppill
	@Override
	public MOVE execute(Game game) {
		/*
		for(Entry<MOVE, Boolean> m : pi.getMoveToPpill().entrySet()) {
			if(m.getValue()) {
				return m.getKey();
			}
		}*/
		
		return MOVE.NEUTRAL;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}
	
	@Override
	public String getActionId() {
		return "PPill Suicida";
	}

}
