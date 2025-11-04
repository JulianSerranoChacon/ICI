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
		for(int ppill: game.getActivePowerPillsIndices()) {
			double distance = game.getDistance(pacman, ppill, game.getPacmanLastMoveMade(), DM.PATH);
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
		
		pacmanData += (String.format("(distanceToBlinky %d)"		, this.distanceFromPacmanToGhost.get(GHOST.BLINKY).intValue()));
		pacmanData += (String.format("(distanceToPinky %d)"   		, this.distanceFromPacmanToGhost.get(GHOST.PINKY).intValue()));
		pacmanData += (String.format("(distanceToInky %d)"			, this.distanceFromPacmanToGhost.get(GHOST.INKY).intValue()));
		pacmanData += (String.format("(distanceToSue %d)"			, this.distanceFromPacmanToGhost.get(GHOST.SUE).intValue()));
		pacmanData += (String.format("(closestIntersection %d)"		, (int) this.distancePacmanToIntersection));
		pacmanData += (String.format("(distanceToClosestPPill %d)"	, (int) this.minPacmanDistancePPill));
		
		pacmanData += ")";
		facts.add(pacmanData);

		// BLINKY
		String blinkyData = "(BLINKY ";
		
		blinkyData += (String.format("(distanceToPacman %d)"		, this.distanceFromGhostToPacman.get(GHOST.BLINKY).intValue()));
		blinkyData += (String.format("(distanceToPinky %d)"   		, this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.PINKY).intValue()));
		blinkyData += (String.format("(distanceToInky %d)"			, this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.INKY).intValue()));
		blinkyData += (String.format("(distanceToSue %d)"			, this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.SUE).intValue()));
		blinkyData += (String.format("(distanceToIntersection %d)"	, this.ghostToIntersection.get(GHOST.BLINKY).intValue()));
		if(!Objects.isNull(this.shieldGhost.get(GHOST.BLINKY))) {
		blinkyData += (String.format("(myShield %s)"				, this.shieldGhost.get(GHOST.BLINKY)));
		}
		blinkyData += (String.format("(edibleTime %d)"				, this.game.getGhostEdibleTime(GHOST.BLINKY)));
		blinkyData += (String.format("(lairTime %d)"				, this.game.getGhostLairTime(GHOST.BLINKY)));
		
		blinkyData += ")";
		facts.add(blinkyData);
		
		// INKY
		String inkyData = "(INKY ";
		
		inkyData += (String.format("(distanceToPacman %d)"			, this.distanceFromGhostToPacman.get(GHOST.INKY).intValue()));
		inkyData += (String.format("(distanceToBlinky %d)"   		, this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.BLINKY).intValue()));
		inkyData += (String.format("(distanceToPinky %d)"			, this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.PINKY).intValue()));
		inkyData += (String.format("(distanceToSue %d)"				, this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.SUE).intValue()));
		inkyData += (String.format("(distanceToIntersection %d)"	, this.ghostToIntersection.get(GHOST.INKY).intValue()));
		if(!Objects.isNull(this.shieldGhost.get(GHOST.INKY))) {
		inkyData += (String.format("(myShield %s)"					, this.shieldGhost.get(GHOST.INKY)));
		}
		inkyData += (String.format("(edibleTime %d)"				, this.game.getGhostEdibleTime(GHOST.INKY)));
		inkyData += (String.format("(lairTime %d)"					, this.game.getGhostLairTime(GHOST.INKY)));
		
		inkyData += ")";
		facts.add(inkyData);

		// PINKY
		String pinkyData = "(PINKY ";
		
		pinkyData += (String.format("(distanceToPacman %d)"			, this.distanceFromGhostToPacman.get(GHOST.PINKY).intValue()));
		pinkyData += (String.format("(distanceToBlinky %d)"   		, this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.BLINKY).intValue()));
		pinkyData += (String.format("(distanceToInky %d)"			, this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.INKY).intValue()));
		pinkyData += (String.format("(distanceToSue %d)"			, this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.SUE).intValue()));
		pinkyData += (String.format("(distanceToIntersection %d)"	, this.ghostToIntersection.get(GHOST.PINKY).intValue()));
		if(!Objects.isNull(this.shieldGhost.get(GHOST.PINKY))) {
		pinkyData += (String.format("(myShield %s)"					, this.shieldGhost.get(GHOST.PINKY)));
		}
		pinkyData += (String.format("(edibleTime %d)"				, this.game.getGhostEdibleTime(GHOST.PINKY)));
		pinkyData += (String.format("(lairTime %d)"					, this.game.getGhostLairTime(GHOST.PINKY)));
		
		pinkyData += ")";
		facts.add(pinkyData);

		// SUE
		String sueData = "(SUE ";
		
		sueData += (String.format("(distanceToPacman %d)"			, this.distanceFromGhostToPacman.get(GHOST.SUE).intValue()));
		sueData += (String.format("(distanceToBlinky %d)"   		, this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.BLINKY).intValue()));
		sueData += (String.format("(distanceToInky %d)"				, this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.INKY).intValue()));
		sueData += (String.format("(distanceToPinky %d)"			, this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.PINKY).intValue()));
		sueData += (String.format("(distanceToIntersection %d)"		, this.ghostToIntersection.get(GHOST.SUE).intValue()));
		if(!Objects.isNull(this.shieldGhost.get(GHOST.SUE))) {
		sueData += (String.format("(myShield %s)"					, this.shieldGhost.get(GHOST.SUE)));
		}
		sueData += (String.format("(edibleTime %d)"					, this.game.getGhostEdibleTime(GHOST.SUE)));
		sueData += (String.format("(lairTime %d)"					, this.game.getGhostLairTime(GHOST.SUE)));
		
		sueData += ")";
		facts.add(sueData);

		//facts.add("(ROLES (hunter1 NONE) (hunter2 NONE))");

		return facts;
	}
	
	private void parseDistanceFromGhostToPacman() {
		distanceFromGhostToPacman = new HashMap<>();
		for(GHOST g : GHOST.values()) {
			double auxDistance = 0;
			if(game.getGhostLairTime(g) > 0) {
				auxDistance = Integer.MAX_VALUE;
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
				auxDistance = Integer.MAX_VALUE;
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
					auxdistance = Integer.MAX_VALUE;
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
				// el punto de encuentro entre los dos fantasmas, asumiendo que se están moviendo uno hacia el otro respectivamente
				// viene modelado por x - 1/2d = d donde:
				// x es la distancia inicial entre los dos fantasmas
				// d es la distancia que recorre el escudero antes de cruzarse con el fantasma comestible
				// simplificando tenemos d = 2/3x
				double distanceToShielder = 2/3 * game.getDistance(shielderPosition, ghostPosition, game.getGhostLastMoveMade(shield), DM.PATH);
												
				double distanceToPacman = distanceFromPacmanToGhost.get(ghost);
				// ajuste asumiendo que el fantasma se esta alejando de pacman, por lo que pacman tiene que recorrer extra
				// calculamos ese extra basandonos en el limite de la serie geometrica (1/2)^n (n en [1, infinito))
				distanceToPacman += distanceToPacman;

				if (distanceToShielder < distanceToPacman) {
					shieldGhost.put(ghost, shield);
					break;
				}
			}
		}
	}
}
	

