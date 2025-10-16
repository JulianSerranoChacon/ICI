package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.PacmanInfo;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseAction implements Action {
	PacmanInfo pi;

	public ChaseAction(PacmanInfo pi) {
		this.pi = pi;
	}
	
	@Override
	public MOVE execute(Game game) {
		GHOST objective = null;
		double distance = Double.MAX_VALUE;
		for (GHOST g : GHOST.values()) {
			if (distance > game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),
					DM.PATH) && game.isGhostEdible(g)) {
				distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),
						DM.PATH);
				objective = g;
			}
		}
		
		distance = Double.MAX_VALUE;
		for(GHOST g : GHOST.values()) {
			if(g == objective || game.isGhostEdible(g))
				continue;
			if(game.getDistance(game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(objective), DM.PATH) < 10) {
				for(GHOST g2 : GHOST.values()) {
					if(g2 == g || g2 == objective) {
						continue;
					}
					if(game.isGhostEdible(g2) && distance > game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g2),
							DM.PATH)) {
						objective = g2;
					}
				}
			}
		}
		
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Chase Action";
	}

}
