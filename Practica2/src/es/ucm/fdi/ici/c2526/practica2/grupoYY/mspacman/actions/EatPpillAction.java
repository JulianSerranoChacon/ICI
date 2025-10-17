package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import java.util.Map.Entry;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.PacmanInfo;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EatPpillAction implements Action {

	PacmanInfo pi;

	public EatPpillAction(PacmanInfo pi) {
		this.pi = pi;
	}
	
	@Override
	public MOVE execute(Game game) {
		for (Entry<MOVE, Boolean> m : pi.getMoveToPpill().entrySet()) {
			if (m.getValue()) {
				return m.getKey();
			}
		}
		
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Eat PPill Action";
	}

}
