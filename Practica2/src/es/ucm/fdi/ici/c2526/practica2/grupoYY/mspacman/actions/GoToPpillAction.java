package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.PacmanInfo;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToPpillAction implements Action {

	PacmanInfo pi;

	public GoToPpillAction(PacmanInfo pi) {
		this.pi = pi;
	}
	
	//Same code since it is very similar, we have to distinct them
	@Override
	public MOVE execute(Game game) {
		for (MOVE m : pi.getCandidateMoves()) {
			if (game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), pi.getClosestPPill(), DM.PATH) == m) {
				return m;
			}
		}
		
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Go to PPill Action";
	}

}
