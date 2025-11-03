package es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions;

import java.util.Random;

import es.ucm.fdi.ici.rules.*;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RandomAction implements RulesAction {

	public RandomAction() {
	}

    private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	
	@Override
	public MOVE execute(Game game) {
		return allMoves[rnd.nextInt(allMoves.length)];
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return "Random Action";
	}

}
