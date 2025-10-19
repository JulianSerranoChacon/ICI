package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class GhostsVoyASerEscuderoQueProteje implements Transition  {

	GHOST ghost;
	GhostInfo gi;
	int minDistance = 200;
	public GhostsVoyASerEscuderoQueProteje(GHOST ghost,GhostInfo gi) {
		super();
		this.ghost = ghost;
		this.gi = gi;
	}



	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		GHOST nearToPacMan = ghost;
		int nearDistance = 999999;
		for(GHOST otherGhost : GHOST.values()) {
			if(otherGhost != ghost&&in.getGame().isGhostEdible(otherGhost)) {
				if(gi.getDistanceFromPacmanToGhost(otherGhost)<nearDistance) {
					nearDistance = gi.getDistanceFromPacmanToGhost(otherGhost);
					nearToPacMan = otherGhost;
					}
				}
			}
		System.out.println("ASDASSADSAAS " + ghost + " "  + nearToPacMan);
		if(ghost== nearToPacMan) {
			return false;
		}
		GHOST nearToGhost = ghost;
		int nearDistanceToGhost = gi.getDistanceFromGhostToGhost(ghost,nearToPacMan);
		for(GHOST otherGhost : GHOST.values()) {
			if(otherGhost != ghost&&!in.getGame().isGhostEdible(otherGhost)&&otherGhost != nearToPacMan) {
				if(gi.getDistanceFromGhostToGhost(otherGhost,nearToPacMan)<nearDistanceToGhost) {
					nearDistanceToGhost = gi.getDistanceFromPacmanToGhost(otherGhost);
					nearToGhost = otherGhost;
					}
				}
			}
		return (ghost == nearToGhost);
		}


	@Override
	public String toString() {
		return "Soy el escudero que proteje";
	}

	
	
}
