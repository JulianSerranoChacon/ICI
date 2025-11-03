package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.List;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EatPpillAction implements RulesAction {

	private Map<GHOST, Integer> distances;
	private int pacManDistance;
	private boolean caminoInmediato;
	MOVE moveToPpill;
	private List<MOVE> CandidateMoves;
	public EatPpillAction() {
		distances = new HashMap<>();
	}
	
	@Override
	public MOVE execute(Game game) {
		
		/*for (Entry<MOVE, Boolean> m : pi.getMoveToPpill().entrySet()) {
			if (m.getValue()) {
				return m.getKey();
			}
		}*/
		
	
		//return pi.getCandidateMoves().get(0);
		if(moveToPpill == null)
			return moveToPpill;
		
		return CandidateMoves.get(0);
	}
	
	@Override
	public void parseFact(Fact actionFact){
		// Nothing to parse
		try {
		/*Value v = actionFact.getSlotValue("BLINKYminDistanceToPpill");
		if(!Objects.isNull(v))
			distances.put( GHOST.BLINKY,v.intValue(null));
		
		v = actionFact.getSlotValue("PINKYminDistanceToPpill");
			if(!Objects.isNull(v))
				distances.put( GHOST.PINKY,v.intValue(null));
		
		v = actionFact.getSlotValue("INKYminDistanceToPpill");
		if(!Objects.isNull(v))
			distances.put( GHOST.INKY,v.intValue(null));

		v = actionFact.getSlotValue("SUEminDistanceToPpill");
		if(!Objects.isNull(v))
			distances.put( GHOST.SUE,v.intValue(null));
		
		v = actionFact.getSlotValue("MSPACMANminDistancePPill");
		if(!Objects.isNull(v))
			pacManDistance = v.intValue(null);
		
		v = actionFact.getSlotValue("MSPACMANhayPillCaminoInmediato");
		if(!Objects.isNull(v))
			pacManDistance = v.intValue(null);*/
		
		Value v = actionFact.getSlotValue("goToPillMove");
		if(!Objects.isNull(v))
			moveToPpill = MOVE.valueOf(v.stringValue(null));
		
		v = actionFact.getSlotValue("RIGHTCandidate");
		if(!Objects.isNull(v) && v.symbolValue(null) == "true")
			CandidateMoves.addLast(MOVE.RIGHT);
		
		v = actionFact.getSlotValue("LEFTCandidate");
		if(!Objects.isNull(v) && v.symbolValue(null) == "true")
			CandidateMoves.addLast(MOVE.LEFT);
		
		v = actionFact.getSlotValue("UPCandidate");
		if(!Objects.isNull(v) && v.symbolValue(null) == "true")
			CandidateMoves.addLast(MOVE.UP);
		
		v = actionFact.getSlotValue("DOWNCandidate");
		if(!Objects.isNull(v) && v.symbolValue(null) == "true")
			CandidateMoves.addLast(MOVE.DOWN);
		
		}
		catch (JessException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public String getActionId() {
		return "Eat PPill Action";
	}

}
