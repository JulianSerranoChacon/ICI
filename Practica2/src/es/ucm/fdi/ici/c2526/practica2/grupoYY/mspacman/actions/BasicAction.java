package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.PacmanInfo;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class BasicAction implements Action {

	PacmanInfo pi;

	public BasicAction(PacmanInfo pi) {
		this.pi = pi;
	}
	
	@Override
	public MOVE execute(Game game) {
		return null;
	}

	@Override
	public String getActionId() {
		return "Basic Action";
	}

}
