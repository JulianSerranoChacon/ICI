package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PPillSuicidaAction implements Action {

	private int closestPPill;
	private double distanceToPPill;

	public PPillSuicidaAction() {
		
	}
	
	//Sabemos que tiene que haber una Ppill
	@Override
	public MOVE execute(Game game) {
		closestPPill = -1; // no Ppill active
        distanceToPPill = Double.MAX_VALUE;
	    if (!(game.getNumberOfActivePowerPills() == 0)) {
	        for (int pill : game.getActivePowerPillsIndices()) {
	            double aux = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
	            if (aux < distanceToPPill) {
	                closestPPill = pill;
	                distanceToPPill = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
	            }
	        }
	    }
	    
	    return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closestPPill, DM.PATH);
	}

	@Override
	public String getActionId() {
		return "PPill Suicida";
	}

}
