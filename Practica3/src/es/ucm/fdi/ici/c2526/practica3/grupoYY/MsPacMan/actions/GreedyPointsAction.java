package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.Map.Entry;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GreedyPointsAction implements Action {

	public GreedyPointsAction() {

	}
	
	@Override
	public MOVE execute(Game game) {
		/*
		int counter = 0;
		MOVE bestMove = pi.getCandidateMoves().get(0);
		
		if(pi.getMoveToPpill().containsKey(bestMove)) {
			bestMove = pi.getCandidateMoves().get(1);
		}
		
		for (Entry<MOVE, Integer> pills : pi.getMoveToPoints().entrySet()) {
			if (pills.getValue() > counter 
					&& !pi.getMoveToPpill().get(pills.getKey()) 
					&& pi.getCandidateMoves().contains(pills.getKey())) {
				bestMove = pills.getKey();
				counter = pills.getValue();
			}
		}

		return bestMove;*/
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Greedy Points Action";
	}

}
