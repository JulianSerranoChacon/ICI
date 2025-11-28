package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import antlr.collections.impl.Vector;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);
		
	}
	
	Integer nearestGhost;
	Boolean edible;
	Integer score;
	Integer time;
	
	//New variables
	Integer numPPills;
	Integer nearestPPill;
	Integer nearestPill;
	
	Double[] ghostToPacman = new Double[4];
	/*
	Double  ghostToPacman1;
	Double  ghostToPacman2;
	Double  ghostToPacman3;
	Double  ghostToPacman4;
	*/
	Double[] pacmanToGhost = new Double[4];
	/*
	Double  pacmanToGhost1;
	Double  pacmanToGhost2;
	Double  pacmanToGhost3;
	Double  pacmanToGhost4;
	*/
	Integer[] ghostEdibleTime = new Integer[4];
	/*
	Integer  ghostEdibleTime1;
	Integer  ghostEdibleTime2;
	Integer  ghostEdibleTime3;
	Integer  ghostEdibleTime4;
	*/
	String pacmanMove;
	String  ghostToPacman1Movement;
	String  ghostToPacman2Movement;
	String  ghostToPacman3Movement;
	String  ghostToPacman4Movement;
	
	
	@Override
	public void parseInput() {
		computeNearestGhost(game);
		computeGhostsToPacmanDist(game);
		computeNearestPPill(game);
		computeNearestPill(game);
		computeGhostsToPacmanDist(game);
		computePacmanToGhostsDist(game);
		time = game.getTotalTime();
		score = game.getScore();
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		description.setScore(score);
		description.setTime(time);
		
		//Pill related info
		description.setNumPPills(numPPills);
		description.setNearestPPill(nearestPPill);
		description.setNearestPill(nearestPill);
		
		//Ghost menacing pacman info
		
		description.setGhostToPacman1(ghostToPacman1);
		description.setGhostToPacman2(ghostToPacman2);
		description.setGhostToPacman3(ghostToPacman3);
		description.setGhostToPacman4(ghostToPacman4);
		
		description.setPacmanMove(pacmanMove);
		description.setGhostToPacman1Movement(ghostToPacman1Movement);
		description.setGhostToPacman2Movement(ghostToPacman2Movement);
		description.setGhostToPacman3Movement(ghostToPacman3Movement);
		description.setGhostToPacman4Movement(ghostToPacman4Movement);
		
		//Ghosts edible by pacman
		
		description.setPacmanToGhost1(pacmanToGhost1);
		description.setPacmanToGhost2(pacmanToGhost2);
		description.setPacmanToGhost3(pacmanToGhost3);
		description.setPacmanToGhost4(pacmanToGhost4);
		
		description.setGhostEdibleTime1(ghostEdibleTime1);
		description.setGhostEdibleTime2(ghostEdibleTime2);
		description.setGhostEdibleTime3(ghostEdibleTime3);
		description.setGhostEdibleTime4(ghostEdibleTime4);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private void computeNearestGhost(Game game) {
		nearestGhost = Integer.MAX_VALUE;
		edible = false;
		GHOST nearest = null;
		for(GHOST g: GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			int distance; 
			if(pos != -1) 
				distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			else
				distance = Integer.MAX_VALUE;
			if(distance < nearestGhost)
			{
				nearestGhost = distance;
				nearest = g;
			}
		}
		if(nearest!=null)
			edible = game.isGhostEdible(nearest);
	}
	
	private void computeNearestPPill(Game game) {
		nearestPPill = Integer.MAX_VALUE;
		numPPills = game.getNumberOfActivePowerPills();
		
		for(int pos: game.getPowerPillIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < nearestGhost && game.isPowerPillStillAvailable(pos))
				this.nearestPPill = distance;
		}
	}
	
	// NEW ADDITIONS //
	
	private void computeNearestPill(Game game) {
		nearestPill = Integer.MAX_VALUE;
		
		//TODO: maybe is too expensive for just a pill but bfs does not look great either
		for(int pos: game.getPillIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < nearestGhost && game.isPillStillAvailable(pos))
				this.nearestPill = distance;
		}
	}
	
	private void computeGhostsToPacmanDist(Game game) {
		class GhostDistance {
		    String move;
		    double distance;

		    GhostDistance(String move, double distance) {
		        this.move = move;
		        this.distance = distance;
		    }
		}
		
		List<GhostDistance> distances = new ArrayList<>();

		for (GHOST g : GHOST.values()) {
			double dist = game.getGhostLairTime(g) <= 0
		            ? game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex())
		            : game.getShortestPathDistance(game.getGhostInitialNodeIndex(), game.getPacmanCurrentNodeIndex());
		    
		    distances.add(new GhostDistance(game.getGhostLastMoveMade(g).toString(), dist));
		}

		// Ordenar la lista de distancias
		distances.sort(Comparator.comparingDouble(gd -> gd.distance));

		// Obtener los valores ordenados de los fantasmas
		ghostToPacman1 = distances.get(0).distance;
		ghostToPacman2 = distances.get(1).distance;
		ghostToPacman3 = distances.get(2).distance;
		ghostToPacman4 = distances.get(3).distance;
		pacmanMove = game.getPacmanLastMoveMade().toString();
		ghostToPacman1Movement = distances.get(0).move;
		ghostToPacman2Movement = distances.get(1).move;
		ghostToPacman3Movement = distances.get(2).move;
		ghostToPacman4Movement = distances.get(3).move;

	}
	
	private void computePacmanToGhostsDist(Game game) {
		class GhostDistance {
		    int ghost_time;
		    double distance;

		    GhostDistance(int ghost_time, double distance) {
		        this.ghost_time = ghost_time;
		        this.distance = distance;
		    }
		}

		List<GhostDistance> ghostDistances = new ArrayList<>();
		for (GHOST g : GHOST.values()) {
			double dist = game.getGhostLairTime(g) <= 0
		            ? game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g))
		            : game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostInitialNodeIndex());

		    ghostDistances.add(new GhostDistance(game.getGhostEdibleTime(g), dist));
		}

		// Ordenar por distancia creciente
		ghostDistances.sort(Comparator.comparingDouble(gd -> gd.distance));

		// Obtener los 4 más cercanos
		ghostEdibleTime1 = ghostDistances.get(0).ghost_time;
		ghostEdibleTime2 = ghostDistances.get(1).ghost_time;
		ghostEdibleTime3 = ghostDistances.get(2).ghost_time;
		ghostEdibleTime4 = ghostDistances.get(3).ghost_time;

		pacmanToGhost1 = ghostDistances.get(0).distance;
		pacmanToGhost2 = ghostDistances.get(1).distance;
		pacmanToGhost3 = ghostDistances.get(2).distance;
		pacmanToGhost4 = ghostDistances.get(3).distance;
	}
	
}
