package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class NoCandidateMovesTransition implements Transition {

	public NoCandidateMovesTransition() {
		
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return input.getCandidateMoves().size() == 0;
	}

	@Override
	public String toString() {
		return String.format("We enter suicidal mode");
	}
}
