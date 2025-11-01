package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends RulesInput {
	
	public enum GHOSTTYPE{
		   HUNTER1,
		   HUNTER2,
		   JAILER,
		   RANDOM
	}
	
	private double minPacmanDistancePPill;
	private double distancePacmanToIntersection;
	
	private Map<GHOST,Double> distanceFromGhostToPacman;
	private Map<GHOST,Double> distanceFromPacmanToGhost;
	private Map<GHOST,Map<GHOST,Double>> distanceFromGhostToGhost;
	private Map<GHOST,GHOST> shieldGhost;
	private Map<GHOST,Double> ghostToIntersection; 
	
	public boolean[] behaviourChanged;
	
	public GhostsInput(Game game) {
		super(game);
		reset();
	}
	
	private void reset() {
		distanceFromGhostToPacman = new HashMap<>();
		distanceFromPacmanToGhost = new HashMap<>();
		distanceFromGhostToGhost = new HashMap<>();
		shieldGhost = new HashMap<>();
		ghostToIntersection = new HashMap<>();
	}
	
	@Override
	public void parseInput() {
		reset();
	
		int pacman = game.getPacmanCurrentNodeIndex();
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for(int ppill: game.getPowerPillIndices()) {
			double distance = game.getDistance(pacman, ppill, DM.PATH);
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
		}
		
		parseDistanceFromGhostToPacman();
		parseDistanceFromPacManToGhost();
		parseDistanceGhostToGhost();
		parseGhostShield();
		parseGhostToIntersection();
	}
	
	@Override
	public Collection<String> getFacts() {
		parseInput();
		Vector<String> facts = new Vector<String>();
		String pacmanData = "(MSPACMAN ";
		
		pacmanData += (String.format("(distanceToBlinky %f)"		, this.distanceFromPacmanToGhost.get(GHOST.BLINKY)));
		pacmanData += (String.format("(distanceToPinky %f)"   		, this.distanceFromPacmanToGhost.get(GHOST.INKY)));
		pacmanData += (String.format("(distanceToInky %f)"			, this.distanceFromPacmanToGhost.get(GHOST.PINKY)));
		pacmanData += (String.format("(distanceToSue %f)"			, this.distanceFromPacmanToGhost.get(GHOST.SUE)));
		pacmanData += (String.format("(distanceToIntersection %f)"	, this.distancePacmanToIntersection));
		pacmanData += (String.format("(distanceToClosestPPill %f)"	, this.minPacmanDistancePPill));
		
		pacmanData += ")";
		facts.add(pacmanData);

		// BLINKY
		String blinkyData = "(BLINKY ";
		
		blinkyData += (String.format("(distanceToPacman %f)"		, this.distanceFromGhostToPacman.get(GHOST.BLINKY)));
		blinkyData += (String.format("(distanceToPinky %f)"   		, this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.INKY)));
		blinkyData += (String.format("(distanceToInky %f)"			, this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.PINKY)));
		blinkyData += (String.format("(distanceToSue %f)"			, this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.SUE)));
		blinkyData += (String.format("(distanceToIntersection %f)"	, this.ghostToIntersection.get(GHOST.BLINKY)));
		if(!Objects.isNull(this.shieldGhost.get(GHOST.BLINKY))) {
		blinkyData += (String.format("(myShield %s)"				, this.shieldGhost.get(GHOST.BLINKY)));
		}
		blinkyData += (String.format("(edibleTime %d)"				, this.game.getGhostEdibleTime(GHOST.BLINKY)));
		blinkyData += (String.format("(lairTime %d)"				, this.game.getGhostEdibleTime(GHOST.BLINKY)));
		
		blinkyData += ")";
		facts.add(blinkyData);
		
		// INKY
		String inkyData = "(INKY ";
		
		inkyData += (String.format("(distanceToPacman %f)"			, this.distanceFromGhostToPacman.get(GHOST.INKY)));
		inkyData += (String.format("(distanceToBlinky %f)"   		, this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.BLINKY)));
		inkyData += (String.format("(distanceToPinky %f)"			, this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.PINKY)));
		inkyData += (String.format("(distanceToSue %f)"				, this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.SUE)));
		inkyData += (String.format("(distanceToIntersection %f)"	, this.ghostToIntersection.get(GHOST.INKY)));
		if(!Objects.isNull(this.shieldGhost.get(GHOST.INKY))) {
		inkyData += (String.format("(myShield %s)"					, this.shieldGhost.get(GHOST.INKY)));
		}
		inkyData += (String.format("(edibleTime %d)"				, this.game.getGhostEdibleTime(GHOST.INKY)));
		inkyData += (String.format("(lairTime %d)"					, this.game.getGhostEdibleTime(GHOST.INKY)));
		
		inkyData += ")";
		facts.add(inkyData);

		// PINKY
		String pinkyData = "(PINKY ";
		
		pinkyData += (String.format("(distanceToPacman %f)"			, this.distanceFromGhostToPacman.get(GHOST.PINKY)));
		pinkyData += (String.format("(distanceToBlinky %f)"   		, this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.BLINKY)));
		pinkyData += (String.format("(distanceToInky %f)"			, this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.INKY)));
		pinkyData += (String.format("(distanceToSue %f)"			, this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.SUE)));
		pinkyData += (String.format("(distanceToIntersection %f)"	, this.ghostToIntersection.get(GHOST.PINKY)));
		if(!Objects.isNull(this.shieldGhost.get(GHOST.PINKY))) {
		pinkyData += (String.format("(myShield %s)"					, this.shieldGhost.get(GHOST.PINKY)));
		}
		pinkyData += (String.format("(edibleTime %d)"				, this.game.getGhostEdibleTime(GHOST.PINKY)));
		pinkyData += (String.format("(lairTime %d)"					, this.game.getGhostEdibleTime(GHOST.PINKY)));
		
		pinkyData += ")";
		facts.add(pinkyData);

		// SUE
		String sueData = "(SUE ";
		
		sueData += (String.format("(distanceToPacman %f)"			, this.distanceFromGhostToPacman.get(GHOST.SUE)));
		sueData += (String.format("(distanceToBlinky %f)"   		, this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.BLINKY)));
		sueData += (String.format("(distanceToInky %f)"				, this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.INKY)));
		sueData += (String.format("(distanceToPinky %f)"			, this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.PINKY)));
		sueData += (String.format("(distanceToIntersection %f)"		, this.ghostToIntersection.get(GHOST.SUE)));
		if(!Objects.isNull(this.shieldGhost.get(GHOST.SUE))) {
		sueData += (String.format("(myShield %s)"					, this.shieldGhost.get(GHOST.SUE)));
		}
		sueData += (String.format("(edibleTime %d)"					, this.game.getGhostEdibleTime(GHOST.SUE)));
		sueData += (String.format("(lairTime %d)"					, this.game.getGhostEdibleTime(GHOST.SUE)));
		
		sueData += ")";
		facts.add(sueData);

		return facts;
	}
	
	private void parseDistanceFromGhostToPacman() {
		distanceFromGhostToPacman = new HashMap<>();
		for(GHOST g : GHOST.values()) {
			double auxDistance = 0;
			if(game.getGhostLairTime(g) > 0) {
				auxDistance = Double.MAX_VALUE;
			}
			else {
				auxDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g) , game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
			}
			distanceFromGhostToPacman.put(g, auxDistance);
		}
		
	}
	
	private void parseDistanceFromPacManToGhost() {
		distanceFromPacmanToGhost = new HashMap<>();
		
		for(GHOST g : GHOST.values()) {
			double auxDistance = 0;
			if(game.getGhostLairTime(g) > 0) {
				auxDistance = Double.MAX_VALUE;
			}
			else {
				auxDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),game.getPacmanLastMoveMade());
			}
			distanceFromPacmanToGhost.put(g, auxDistance);
		}
		
	}
	
	private void parseDistanceGhostToGhost() {
		distanceFromGhostToGhost= new HashMap<>();
		for(GHOST g : GHOST.values()) {
			Map<GHOST,Double> auxMap = new HashMap<>();
			for(GHOST otherghost : GHOST.values()) {
				double auxdistance = 0;
				if(game.getGhostLairTime(otherghost) > 0 || game.getGhostLairTime(g) > 0) { 	
					auxdistance = Double.MAX_VALUE;
				}
				else {
					auxdistance = game.getShortestPathDistance( game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(otherghost),game.getGhostLastMoveMade(g));
				}
				auxMap.put(otherghost, auxdistance);
			}
			distanceFromGhostToGhost.put(g, auxMap);
		}
	}
	private void parseGhostToIntersection() {
		 ghostToIntersection =  new HashMap<>();
		 int nextIntersectNode = 99999;
		 int nearestDistance = 99999;
		 int[] juntions =  this.getGame().getJunctionIndices();
		 for(int i = 0; i < juntions.length;i++) {
			 int aux = this.getGame().getShortestPathDistance(game.getPacmanCurrentNodeIndex(), i,game.getPacmanLastMoveMade() );
			 if(aux<nearestDistance) {
				 nextIntersectNode = i;
				 nearestDistance = aux;
			 }
			 
		 }
		 nextIntersectNode =  this.getGame().getClosestNodeIndexFromNodeIndex(nextIntersectNode, juntions, DM.PATH);
		 
		 
		for(GHOST g : GHOST.values()) {
			double auxDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), nextIntersectNode, game.getGhostLastMoveMade(g));
			ghostToIntersection.put(g, auxDistance);
		}
		
		distancePacmanToIntersection = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), nextIntersectNode, game.getPacmanLastMoveMade());
	}
	private void parseGhostShield() {
		//Compruebo que estoy lo sufcientemente amenzado como para buscar a un escudero
		for(GHOST ghost : GHOST.values()) {
			double limit = (game.getGhostEdibleTime(ghost) / 2) + 30;
			double distance = distanceFromGhostToPacman.get(ghost);
			int ghostPosition = game.getGhostCurrentNodeIndex(ghost);
			//MOVE ghostLastmove = game.getGhostLastMoveMade(ghost);
			
			if(distance >= limit || game.getGhostLairTime(ghost) <= 0 || !game.isGhostEdible(ghost)) {
				continue;
			}
			
			//Recorro todos los fantasmas buscando al no comestible más cercano y que no tenga a PacMan en medio
			//
			for(GHOST shield : GHOST.values()) {
				if(shield == ghost || shieldGhost.containsValue(shield) || game.isGhostEdible(shield) || game.getGhostLairTime(shield) > 0) {
					continue;
				}

				int shielderPosition = game.getGhostCurrentNodeIndex(shield);

				// calculo la distancia hasta el escudero
				double distanceToShielder = 2/3 * game.getDistance(shielderPosition, ghostPosition, game.getGhostLastMoveMade(shield), DM.PATH);
												
				double distanceToPacman = distanceFromPacmanToGhost.get(ghost);
				// ajuste asumiendo que el fantasma se esta alejando de pacman, por lo que pacman tiene que recorrer extra
				// calculamos ese extra basandonos en el limite de la serie geometrica de (1/2)^n (n en [1, infinito))
				distanceToPacman += distanceToPacman;

				if (distanceToShielder < distanceToPacman) {
					shieldGhost.put(ghost, shield);
					break;
				}
			}
		}
	}
}
	

