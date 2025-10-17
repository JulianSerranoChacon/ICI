package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.MsPacMan;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class OnlyOneMovePossibleTransition implements Transition {
	private static int idcount= 0;
	private int privateID;
	
	public OnlyOneMovePossibleTransition() {
		privateID = idcount;
		idcount++;
	}

	@Override
	public boolean evaluate(Input in) {
        MsPacManInput input = (MsPacManInput) in;
        return input.getCandidateMoves().size() == 1;
	}

	@Override
	public String toString() {
		return String.format("there is only one possible move"+ privateID);
	}
}
