package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MoveAction implements Action {


	public MoveAction() {

	}
	
	@Override
	public MOVE execute(Game game) {
		//return pi.getCandidateMoves().get(0);
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Only move possible action";
	}

}
