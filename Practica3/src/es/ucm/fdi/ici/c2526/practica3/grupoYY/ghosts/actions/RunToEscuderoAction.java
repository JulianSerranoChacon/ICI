package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.GhostsInput.GHOSTTYPE;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.BasicAction2.STRATEGY;
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
    GHOST[] allGhosts;
    private Map<GHOST,Map<GHOST,Double>> distanceFromGhostToGhost;
  
	public RunToEscuderoAction(GHOST ghost) {
		this.ghost = ghost;
		this.allGhosts = GHOST.values();
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	
           GHOST shield = GHOST.BLINKY;
           double minDistance = Double.MAX_VALUE;
           for(GHOST actGhost : allGhosts) {
        	   double distance = 0; 
        	   if(!game.isGhostEdible(actGhost)) {
        		   
        		   distance = distanceFromGhostToGhost.get(this.ghost).get(actGhost);
        		   
        		   if(distance < minDistance) {
        			   minDistance = distance;
        			   shield = actGhost;
        		   }
        	   }
           }
           
           return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(this.ghost),
        		   game.getGhostCurrentNodeIndex(shield), game.getGhostLastMoveMade(this.ghost), DM.PATH);
        }
            
        return MOVE.NEUTRAL;	
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Map<GHOST, Map<GHOST, Double>> distanceFromGhostToGhost = new HashMap<>();

			Map<GHOST, Double> aux = new HashMap<GHOST, Double>();

			// ===== BLINKY =====
			Value blinkyToInky = actionFact.getSlotValue("BLINKYdistanceToINKY");
			if (!Objects.isNull(blinkyToInky)) {
			    String distValue = blinkyToInky.stringValue(null);
			    aux.put(GHOST.INKY, Double.valueOf(distValue));
			}

			Value blinkyToPinky = actionFact.getSlotValue("BLINKYdistanceToPINKY");
			if (!Objects.isNull(blinkyToPinky)) {
			    String distValue = blinkyToPinky.stringValue(null);
			    aux.put(GHOST.PINKY, Double.valueOf(distValue));
			}

			Value blinkyToSue = actionFact.getSlotValue("BLINKYdistanceToSUE");
			if (!Objects.isNull(blinkyToSue)) {
			    String distValue = blinkyToSue.stringValue(null);
			    aux.put(GHOST.SUE, Double.valueOf(distValue));
			}

			distanceFromGhostToGhost.put(GHOST.BLINKY, new HashMap<>(aux));
			aux.clear();

			// ===== INKY =====
			Value inkyToBlinky = actionFact.getSlotValue("INKYdistanceToBLINKY");
			if (!Objects.isNull(inkyToBlinky)) {
			    String distValue = inkyToBlinky.stringValue(null);
			    aux.put(GHOST.BLINKY, Double.valueOf(distValue));
			}

			Value inkyToPinky = actionFact.getSlotValue("INKYdistanceToPINKY");
			if (!Objects.isNull(inkyToPinky)) {
			    String distValue = inkyToPinky.stringValue(null);
			    aux.put(GHOST.PINKY, Double.valueOf(distValue));
			}

			Value inkyToSue = actionFact.getSlotValue("INKYdistanceToSUE");
			if (!Objects.isNull(inkyToSue)) {
			    String distValue = inkyToSue.stringValue(null);
			    aux.put(GHOST.SUE, Double.valueOf(distValue));
			}

			distanceFromGhostToGhost.put(GHOST.INKY, new HashMap<>(aux));
			aux.clear();

			// ===== PINKY =====
			Value pinkyToBlinky = actionFact.getSlotValue("PINKYdistanceToBLINKY");
			if (!Objects.isNull(pinkyToBlinky)) {
			    String distValue = pinkyToBlinky.stringValue(null);
			    aux.put(GHOST.BLINKY, Double.valueOf(distValue));
			}

			Value pinkyToInky = actionFact.getSlotValue("PINKYdistanceToINKY");
			if (!Objects.isNull(pinkyToInky)) {
			    String distValue = pinkyToInky.stringValue(null);
			    aux.put(GHOST.INKY, Double.valueOf(distValue));
			}

			Value pinkyToSue = actionFact.getSlotValue("PINKYdistanceToSUE");
			if (!Objects.isNull(pinkyToSue)) {
			    String distValue = pinkyToSue.stringValue(null);
			    aux.put(GHOST.SUE, Double.valueOf(distValue));
			}

			distanceFromGhostToGhost.put(GHOST.PINKY, new HashMap<>(aux));
			aux.clear();

			// ===== SUE =====
			Value sueToBlinky = actionFact.getSlotValue("SUEdistanceToBLINKY");
			if (!Objects.isNull(sueToBlinky)) {
			    String distValue = sueToBlinky.stringValue(null);
			    aux.put(GHOST.BLINKY, Double.valueOf(distValue));
			}

			Value sueToInky = actionFact.getSlotValue("SUEdistanceToINKY");
			if (!Objects.isNull(sueToInky)) {
			    String distValue = sueToInky.stringValue(null);
			    aux.put(GHOST.INKY, Double.valueOf(distValue));
			}

			Value sueToPinky = actionFact.getSlotValue("SUEdistanceToPINKY");
			if (!Objects.isNull(sueToPinky)) {
			    String distValue = sueToPinky.stringValue(null);
			    aux.put(GHOST.PINKY, Double.valueOf(distValue));
			}

			distanceFromGhostToGhost.put(GHOST.SUE, new HashMap<>(aux));
			aux.clear();

			
		} catch (JessException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getActionId() {
		return ghost+ "runToEscudero";
	}

}
