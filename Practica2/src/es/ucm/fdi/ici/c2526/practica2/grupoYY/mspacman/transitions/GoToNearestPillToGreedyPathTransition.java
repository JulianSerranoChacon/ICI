package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class GoToNearestPillToGreedyPathTransition implements Transition {

	public GoToNearestPillToGreedyPathTransition() {
		
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return input.getMoveToPoints().size() != 0;
	}

	@Override
	public String toString() {
		return String.format("Go to path with most pills");
	}
}
