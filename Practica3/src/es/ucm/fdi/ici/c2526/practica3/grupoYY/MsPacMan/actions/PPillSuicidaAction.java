package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PPillSuicidaAction implements RulesAction {

	private Map<MOVE, Boolean> moveToPpill;
	
	public PPillSuicidaAction() {
		
	}
	
	//Sabemos que tiene que haber una Ppill
	@Override
	public MOVE execute(Game game) {
		
		for(Entry<MOVE, Boolean> m : moveToPpill.entrySet()) {
			if(m.getValue()) {
				return m.getKey();
			}
		}
		
		return MOVE.NEUTRAL;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		try {
			Value v = actionFact.getSlotValue("RIGHTMoveToPpill");
			if(!Objects.isNull(v))
				moveToPpill.put(MOVE.RIGHT, v.symbolValue(null) == "true");
			
			v = actionFact.getSlotValue("LEFTMoveToPpill");
			if(!Objects.isNull(v))
				moveToPpill.put(MOVE.LEFT, v.symbolValue(null) == "true");
			
			v = actionFact.getSlotValue("UPMoveToPpill");
			if(!Objects.isNull(v))
				moveToPpill.put(MOVE.UP, v.symbolValue(null) == "true");
			
			v = actionFact.getSlotValue("DOWNMoveToPpill");
			if(!Objects.isNull(v))
				moveToPpill.put(MOVE.DOWN, v.symbolValue(null) == "true");
			
		}
		catch (JessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getActionId() {
		return "PPill Suicida";
	}

}
