package es.ucm.fdi.ici.c2526.practica2.grupoYY;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostInfo {

	private Map<GHOST,Integer> distanceFromGhostToPacman;
	private Map<GHOST,Integer> distanceFromPacmanToGhost;
	private Map<GHOST,Map<GHOST,Integer>> DistanceFromGhostToGhost;
	private Map<GHOST,GHOST> shieldGhost;
	public enum GHOSTTYPE{
		   HUNTER1,
		   HUNTER2,
		   JAILER,
		   RANDOM
	   }
	private Map<GHOST,GHOSTTYPE> GhostClass;
	
	public GhostInfo(){
	
	
		reset();
		 
	}
	
	public Map<GHOST, GHOSTTYPE> getGhostClass() {
		return GhostClass;
	}
	
	public void setGhostClass(Map<GHOST, GHOSTTYPE> ghostClass) {
		GhostClass = ghostClass;
	}

	public void reset() {
		distanceFromGhostToPacman = new HashMap<>();
		distanceFromPacmanToGhost = new HashMap<>();
		DistanceFromGhostToGhost = new HashMap<>();
		shieldGhost = new HashMap<>();
		for(GHOST g : GHOST.values())  shieldGhost.put(g, g);
		
	}

	public int getDistanceFromGhostToPacman(GHOST g) {
		return distanceFromGhostToPacman.get(g);
	}
	public int getDistanceFromPacmanToGhost(GHOST g) {
		return distanceFromPacmanToGhost.get(g);
	}
	public int getDistanceFromGhostToGhost(GHOST myGhost,GHOST otherGhost) {

		if(DistanceFromGhostToGhost.containsKey(myGhost)) {
			if (DistanceFromGhostToGhost.get(myGhost).containsKey(otherGhost)) return DistanceFromGhostToGhost.get(myGhost).get(otherGhost);
		}
		 return 999999;
	}
	public GHOST getMyShieldGhost(GHOST g) {
		if(shieldGhost.size()== 0) return g;
		return shieldGhost.get(g);
	}
	public void setMyShieldGhost(Map<GHOST,GHOST> s) {
		shieldGhost = s;
	}
	public void setaShieldGhost(GHOST g,GHOST toProtect) {
		shieldGhost.remove(g);
		shieldGhost.put(g, toProtect);
	}
	public void setFromGhostToPacMan(Map<GHOST,Integer> m) {
		distanceFromGhostToPacman = m;
	}
	public void setFromPacmanToGhost(Map<GHOST,Integer> m) {
		distanceFromPacmanToGhost = m;
	}
	
	public void setDistanceFromGhostToGhost(Map<GHOST, Map<GHOST, Integer>> distanceFromGhostToGhost) {
		DistanceFromGhostToGhost = distanceFromGhostToGhost;
	}
	
	public GHOSTTYPE getMyGhostPriority(GHOST g) {
		return GhostClass.get(g);
	}

	
}
