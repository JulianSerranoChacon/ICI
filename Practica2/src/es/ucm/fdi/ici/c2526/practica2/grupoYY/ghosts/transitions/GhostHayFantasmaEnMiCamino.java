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
	Hashtable<Integer, Integer> visitNode = new Hashtable();
	public GhostHayFantasmaEnMiCamino(GHOST ghost) {
		super();
		this.ghost = ghost;
	}



	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		if(in.getGame().getGhostLairTime(ghost)!=0) return false;
		int mghostNode = in.getGame().getGhostCurrentNodeIndex(ghost);
		int[] futureNodeMove = in.getGame().getNeighbouringNodes(mghostNode, in.getGame().getGhostLastMoveMade(ghost));
		LinkedList<Integer> colaNodos = new LinkedList<Integer>();
		colaNodos.add(futureNodeMove[0]);
		visitNode.put(futureNodeMove[0], 1);
		visitNode.put(mghostNode, 0);
		boolean inter = false;
		while(!inter&&!colaNodos.isEmpty()) {
			int aux = colaNodos.getFirst();
			colaNodos.remove(0);
			if(!in.getGame().isJunction(aux)) {
			futureNodeMove = in.getGame().getNeighbouringNodes(aux);
			for(int a : futureNodeMove) {
				//System.out.println(visitNode.contains(a));
				if(!visitNode.containsKey(a)) {
					colaNodos.add(a);
					
					visitNode.put(a, visitNode.get(aux)+1);
					}
				}
			}
			else inter = true;
		}
		boolean flag = false;
		for(GHOST otherGhost : GHOST.values()) {
			if(otherGhost != ghost) {
				
				if(in.getGame().getGhostLairTime(otherGhost)==0&&!flag&& in.getGame().isGhostEdible(otherGhost))
					flag = visitNode.containsKey(in.getGame().getGhostCurrentNodeIndex(otherGhost));
				
				
			}
		}
		//System.out.println(flag);
		return flag;
	}



	@Override
	public String toString() {
		return "Hay Fantasma en mi camino";
	}

	
	
}
