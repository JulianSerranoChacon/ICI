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
	private List<GHOST> ghostPriority;
	public void reset() {
		distanceFromGhostToPacman = new HashMap<>();
		distanceFromPacmanToGhost = new HashMap<>();
		DistanceFromGhostToGhost = new HashMap<>();
		ghostPriority = new LinkedList<>();
	}
	
	GhostInfo(){
		reset();
		shieldGhost = new HashMap<>();
	}
	
	public int getMyGhostPriority(GHOST g) {
		return ghostPriority.indexOf(g);
	}
	public int getDistanceFromGhostToPacman(GHOST g) {
		return distanceFromGhostToPacman.get(g);
	}
	public int getDistanceFromPacmanToGhost(GHOST g) {
		return distanceFromPacmanToGhost.get(g);
	}
	public int getDistanceFromGhostToGhost(GHOST myGhost,GHOST otherGhost) {
		return DistanceFromGhostToGhost.get(myGhost).get(otherGhost);
	}
	public GHOST getMyShieldGhost(GHOST g) {
		return shieldGhost.get(g);
	}
	public void setMyShieldGhost(List<GHOST> l) {
		ghostPriority = l;
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
	
	public void setGhostPriority(List<GHOST> ghostPriority) {
		this.ghostPriority = ghostPriority;
	}

	
}
