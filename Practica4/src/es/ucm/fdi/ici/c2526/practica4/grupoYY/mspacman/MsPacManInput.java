package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	
	Integer score;
	Integer numPPills;
	Integer nearestPPill;
	Integer nearestPill;
	vectorCBRDouble ghostToPacman;
	vectorCBRDouble pacmanToGhost;
	vectorCBRDouble ghostEdibleTime;
	String pacmanLastMove;
	vectorCBR ghostLastMoves;
	
	public MsPacManInput(Game game) {
		super(game);
	}
		
	@Override
	public void parseInput() {
		computeGhostsToPacmanDist(game);
		computePacmanToGhostsDist(game);
		computeNearestPPill(game);
		computeNearestPill(game);
		numPPills = game.getNumberOfActivePowerPills();
		score = game.getScore();
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		description.setScore(score);
		//Pill related info
		description.setNumPPills(numPPills);
		description.setNearestPPill(nearestPPill);
		description.setNearestPill(nearestPill);
		
		//Ghost menacing pacman info
		
		description.setGhostToPacman(ghostToPacman);
		
		description.setGhostLastMoves(ghostLastMoves);
			
		description.setPacmanLastMove(pacmanLastMove);
		
		description.setPacmanToGhost(pacmanToGhost);
		
		description.setGhostEdibleTime(ghostEdibleTime);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	
	
	private void computeNearestPPill(Game game) {
		nearestPPill = Integer.MAX_VALUE;
		numPPills = game.getNumberOfActivePowerPills();
		
		for(int pos: game.getPowerPillIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < pacmanToGhost.getElement(0) && game.isPowerPillStillAvailable(pos))
				this.nearestPPill = distance;
		}
	}
	
	// NEW ADDITIONS //
	
	private void computeNearestPill(Game game) {
		nearestPill = Integer.MAX_VALUE;
		
		//TODO: maybe is too expensive for just a pill but bfs does not look great either
		for(int pos: game.getPillIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < pacmanToGhost.getElement(0) && game.isPillStillAvailable(pos))
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
		ghostToPacman = new vectorCBRDouble(4);
		ghostLastMoves = new vectorCBR(4);
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
		ghostToPacman.setElement(0,distances.get(0).distance);
		ghostToPacman.setElement(1,distances.get(1).distance);
		ghostToPacman.setElement(2,distances.get(2).distance);
		ghostToPacman.setElement(3,distances.get(3).distance);
		pacmanLastMove = game.getPacmanLastMoveMade().toString();
		ghostLastMoves.setElement(0, distances.get(0).move);
		ghostLastMoves.setElement(1, distances.get(1).move);
		ghostLastMoves.setElement(2, distances.get(2).move);
		ghostLastMoves.setElement(3, distances.get(3).move);

	}
	
	private void computePacmanToGhostsDist(Game game) {
		class GhostDistance {
		    double ghost_time;
		    double distance;

		    GhostDistance(int ghost_time, double distance) {
		        this.ghost_time = ghost_time;
		        this.distance = distance;
		    }
		}

		ghostEdibleTime = new vectorCBRDouble(4);
		pacmanToGhost = new vectorCBRDouble(4);
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
		ghostEdibleTime.setElement(0,ghostDistances.get(0).ghost_time);
		ghostEdibleTime.setElement(1,ghostDistances.get(1).ghost_time);
		ghostEdibleTime.setElement(2,ghostDistances.get(2).ghost_time);
		ghostEdibleTime.setElement(3,ghostDistances.get(3).ghost_time);

		pacmanToGhost.setElement(0,ghostDistances.get(0).distance);
		pacmanToGhost.setElement(1,ghostDistances.get(1).distance);
		pacmanToGhost.setElement(2,ghostDistances.get(2).distance);
		pacmanToGhost.setElement(3,ghostDistances.get(3).distance);
	}
	
}
