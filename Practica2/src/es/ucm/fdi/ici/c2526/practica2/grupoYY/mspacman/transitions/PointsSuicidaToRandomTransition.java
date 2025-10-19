package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class PointsSuicidaToRandomTransition implements Transition {

	public PointsSuicidaToRandomTransition() {
		
	}
	
	//If there is no pills near we return true.
	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		for(int points : input.getMoveToPoints().values()) {
			if(points != 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("Do random because we do not care");
	}
}
