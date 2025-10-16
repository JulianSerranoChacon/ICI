package es.ucm.fdi.ici.c2526.practica2.grupoYY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class PacmanInfo {
	boolean finishLevel;
	private Map<MOVE, Integer> moveToNode;
	private Map<MOVE, Integer> moveToPoints;
	private Map<MOVE, Boolean> moveToPpill;
	private Map<MOVE, Boolean> moveToGhost;
	private Map<GHOST, Boolean> ghostEdible;
	private Map<GHOST, MOVE> ghostLastMove;
	private List<MOVE> candidateMoves;
	private int closestPPill;
	private double distanceToPPill;
	private final double dangerDistance = 20; //tentative, subject to change 
	
	public void reset() {
		moveToPoints = new HashMap<>();
		moveToPpill = new HashMap<>();
		moveToGhost = new HashMap<>();
		moveToNode = new HashMap<>();
		ghostEdible = new HashMap<>();
		ghostLastMove = new HashMap<>();
		candidateMoves = new ArrayList<>();
	}
	
	public PacmanInfo() {
		reset();
	}
	
	
	public boolean isFinishLevel() {
		return finishLevel;
	}

	public void setFinishLevel(boolean finishLevel) {
		this.finishLevel = finishLevel;
	}

	public Map<MOVE, Integer> getMoveToNode() {
		return moveToNode;
	}

	public void setMoveToNode(Map<MOVE, Integer> moveToNode) {
		this.moveToNode = moveToNode;
	}

	public Map<MOVE, Integer> getMoveToPoints() {
		return moveToPoints;
	}

	public void setMoveToPoints(Map<MOVE, Integer> moveToPoints) {
		this.moveToPoints = moveToPoints;
	}

	public Map<MOVE, Boolean> getMoveToPpill() {
		return moveToPpill;
	}

	public void setMoveToPpill(Map<MOVE, Boolean> moveToPpill) {
		this.moveToPpill = moveToPpill;
	}

	public Map<MOVE, Boolean> getMoveToGhost() {
		return moveToGhost;
	}

	public void setMoveToGhost(Map<MOVE, Boolean> moveToGhost) {
		this.moveToGhost = moveToGhost;
	}

	public Map<GHOST, Boolean> getGhostEdible() {
		return ghostEdible;
	}

	public void setGhostEdible(Map<GHOST, Boolean> ghostEdible) {
		this.ghostEdible = ghostEdible;
	}

	public Map<GHOST, MOVE> getGhostLastMove() {
		return ghostLastMove;
	}

	public void setGhostLastMove(Map<GHOST, MOVE> ghostLastMove) {
		this.ghostLastMove = ghostLastMove;
	}

	public List<MOVE> getCandidateMoves() {
		return candidateMoves;
	}

	public void setCandidateMoves(List<MOVE> candidateMoves) {
		this.candidateMoves = candidateMoves;
	}

	public int getClosestPPill() {
		return closestPPill;
	}

	public void setClosestPPill(int closestPPill) {
		this.closestPPill = closestPPill;
	}

	public double getDistanceToPPill() {
		return distanceToPPill;
	}

	public void setDistanceToPPill(double distanceToPPill) {
		this.distanceToPPill = distanceToPPill;
	}

	public double getDangerDistance() {
		return dangerDistance;
	}
}
