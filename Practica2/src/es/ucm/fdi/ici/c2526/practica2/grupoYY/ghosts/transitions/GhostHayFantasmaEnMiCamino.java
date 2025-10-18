package es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions;

import java.util.Dictionary;
import java.util.Hashtable;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import java.util.LinkedList;

public class GhostHayFantasmaEnMiCamino implements Transition  {

	GHOST ghost;
	int limit = 50; 
	Hashtable visitNode = new Hashtable();
	public GhostHayFantasmaEnMiCamino(GHOST ghost) {
		super();
		this.ghost = ghost;
	}



	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		
		int mghostNode = in.getGame().getGhostCurrentNodeIndex(ghost);
		int[] futureNodeMove = in.getGame().getNeighbouringNodes(mghostNode);
		MOVE mghostMove = in.getGame().getGhostLastMoveMade(ghost);
		LinkedList<Integer> colaNodos = new LinkedList<Integer>();
		colaNodos.add(futureNodeMove[0]);
		visitNode.put(futureNodeMove[0], 1);
		visitNode.put(mghostNode, 0);
		while(!colaNodos.isEmpty()) {
			int aux = colaNodos.getFirst();
			colaNodos.remove(0);
			if((Integer)visitNode.get(aux) <limit) {
			futureNodeMove = in.getGame().getNeighbouringNodes(aux);
			for(int a : futureNodeMove) {
				if(!visitNode.contains(a)) {
					colaNodos.add(a);
					visitNode.put(a, (Integer)visitNode.get(aux)+1);
				}
				}
			}
		}
		boolean flag = false;
		for(GHOST otherGhost : GHOST.values()) {
			if(otherGhost != ghost) {
				flag = visitNode .containsKey(in.getGame().getGhostCurrentNodeIndex(otherGhost));
				
			}
		}
		return flag;
	}



	@Override
	public String toString() {
		return "Hay Fantasma en mi camino";
	}

	
	
}
