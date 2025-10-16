package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.PacmanInfo;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class HideAction implements Action {
	PacmanInfo pi;

	public HideAction(PacmanInfo pi) {
		this.pi = pi;
	}
	
	//TODO: Implement
	@Override
	public MOVE execute(Game game) {
		return null;
	}

	@Override
	public String getActionId() {
		return "Basic Action";
	}

}
