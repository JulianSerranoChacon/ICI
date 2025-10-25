package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.Map.Entry;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MorePillsSuicidaAction implements Action {

	public MorePillsSuicidaAction() {
	
	}
	
	@Override
	public MOVE execute(Game game) {
		/*
		int counter = 0;
		MOVE bestMove = MOVE.NEUTRAL;
		for (Entry<MOVE, Integer> pills : pi.getMoveToPoints().entrySet()) {
			if (pills.getValue() > counter) {
				bestMove = pills.getKey();
				counter = pills.getValue();
			}
		}

		return bestMove;*/
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Go to pills suicida action";
	}

}
