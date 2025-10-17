package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import java.util.Map.Entry;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.PacmanInfo;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PPillSuicidaAction implements Action {

	private PacmanInfo pi;

	public PPillSuicidaAction(PacmanInfo pi) {
		this.pi = pi;
	}
	
	//Sabemos que tiene que haber una Ppill
	@Override
	public MOVE execute(Game game) {
		for(Entry<MOVE, Boolean> m : pi.getMoveToPpill().entrySet()) {
			if(m.getValue()) {
				return m.getKey();
			}
		}
		
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "PPill Suicida";
	}

}
