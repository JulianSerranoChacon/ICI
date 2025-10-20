package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import java.util.Arrays;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.GhostInfo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsHayEscudero implements Transition  {

	GHOST ghost;
	GhostInfo g;
	int limit;
	public GhostsHayEscudero(GHOST ghost,GhostInfo g) {
		super();
		this.ghost = ghost;
		this.g = g;
		this.limit = 100;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		//Compruebo que estoy lo sufcientemente amenzado como para buscar a un escudero
		this.limit = (input.getGame().getGhostEdibleTime(this.ghost) / 2) + 30;
		int distance = g.getDistanceFromGhostToPacman(this.ghost);
		if(distance >= this.limit) return false;
		
		//Recorro todos los fantasmas buscando al no comestible más cercano y que no tenga a PacMan en medio
		
		for(GHOST otherGhost : GHOST.values()) {
			boolean flag = true;
			if(in.getGame().getGhostLairTime(this.ghost) <= 0 && otherGhost != ghost && !in.getGame().isGhostEdible(otherGhost) && in.getGame().getGhostLairTime(otherGhost) <= 0) {
				
				//Encuentro la distancia de mi yo comestible a mi posible escudero
				int[] pathGhostDistance = input.getGame().getShortestPath(input.getGame().getGhostCurrentNodeIndex(this.ghost), 
						input.getGame().getGhostCurrentNodeIndex(otherGhost),input.getGame().getGhostLastMoveMade(this.ghost));
				
				//Encuentro la distancia de mi yo comestible a PacMan
				int[] pathPacManDistance = input.getGame().getShortestPath(input.getGame().getPacmanCurrentNodeIndex(), 
						input.getGame().getGhostCurrentNodeIndex(this.ghost));
			
				//Si algun coincide PELIGRO ME PUEDEN COMER
				for(int node : pathGhostDistance) {
					if(Arrays.asList(pathPacManDistance).contains(node)) {
						flag = false;
						break;
					}
				}
				if(flag == true) return true; //Si el camino seguro, devuelvo true
	
			}
		}
		return false;
	}



	@Override
	public String toString() {
		return "Hay escudero ";
	}

	
	
}
