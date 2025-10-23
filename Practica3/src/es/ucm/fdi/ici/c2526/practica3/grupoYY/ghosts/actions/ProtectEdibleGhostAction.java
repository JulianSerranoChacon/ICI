package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.BasicAction2.STRATEGY;
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
    GHOST[] allGhosts;
    
	public ProtectEdibleGhostAction(GHOST ghost) {
		this.ghost = ghost;
		this.allGhosts = GHOST.values();
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	
           GHOST edible = GHOST.BLINKY;
           int minDistance = 10000;
           
           //Search the edible Ghost who is closest to PacMan
           for(GHOST actGhost : allGhosts) {
        	   int distance = 0; 
        	   if(game.isGhostEdible(actGhost)) {
        		   
        		   distance = info.getDistanceFromGhostToPacman(actGhost);
        		   
        		   if(distance < minDistance) {
        			   minDistance = distance;
        			   edible = actGhost;
        		   }
        	   }
           }
           //asigna escudero en caso de no existir este 
           if(info.getMyShieldGhost(ghost) == ghost) info.setaShieldGhost(ghost, edible);
           //Return the best move to get to the edible ghost 
           return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(this.ghost),
        		   game.getGhostCurrentNodeIndex(edible), game.getGhostLastMoveMade(this.ghost), DM.PATH);
        }
            
        return MOVE.NEUTRAL;	
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
	public String getActionId() {
		return ghost+ "runToTheEdible";
	}

}
