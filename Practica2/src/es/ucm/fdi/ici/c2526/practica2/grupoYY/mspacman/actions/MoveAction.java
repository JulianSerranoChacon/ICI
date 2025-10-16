package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.PacmanInfo;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MoveAction implements Action {

	PacmanInfo pi;

	public MoveAction(PacmanInfo pi) {
		this.pi = pi;
	}
	
	@Override
	public MOVE execute(Game game) {
		return pi.getCandidateMoves().getFirst();
	}

	@Override
	public String getActionId() {
		return "Only move possible action";
	}

}
