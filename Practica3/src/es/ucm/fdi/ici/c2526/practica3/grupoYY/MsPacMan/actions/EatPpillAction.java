package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.Map.Entry;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EatPpillAction implements Action {



	public EatPpillAction() {
	
	}
	
	@Override
	public MOVE execute(Game game) {
		/*
		for (Entry<MOVE, Boolean> m : pi.getMoveToPpill().entrySet()) {
			if (m.getValue()) {
				return m.getKey();
			}
		}
	
		return pi.getCandidateMoves().get(0);*/
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Eat PPill Action";
	}

}
