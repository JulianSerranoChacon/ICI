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
	
	private Map<GHOST,Double> distanceFromGhostToPacman;
	private Map<GHOST,Double> distanceFromPacmanToGhost;
	private Map<GHOST,Map<GHOST,Double>> distanceFromGhostToGhost;
	private Map<GHOST,GHOST> shieldGhost;
	private Map<GHOST,Double> ghostToIntersection; 
	
	public boolean[] behaviourChanged;
	
	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		game.isGhostEdible(GHOST.BLINKY);
		game.isGhostEdible(GHOST.INKY);
		game.isGhostEdible(GHOST.PINKY);
		game.isGhostEdible(GHOST.SUE);
	
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
		//ADD EDIBLE FACTS //
		facts.add(String.format("(MSPACMAN (mindistancePPill %f))", 
								(float)this.minPacmanDistancePPill));
		
		//ADD DISTANCE FROM GHOST TO PACMAN //
		facts.add(String.format("(BLINKYtoPacman (distanceTo %f))", 
				this.distanceFromGhostToPacman.get(GHOST.BLINKY)));
		facts.add(String.format("(INKYtoPacman (distanceTo %f))", 
				this.distanceFromGhostToPacman.get(GHOST.INKY)));
		facts.add(String.format("(PINKYtoPacman (distanceTo %f))", 
				this.distanceFromGhostToPacman.get(GHOST.PINKY)));
		facts.add(String.format("(SUEtoPacman (distanceTo %f))", 
				this.distanceFromGhostToPacman.get(GHOST.SUE)));
		
		//ADD DISTANCE FROM PACMAN TO GHOST //
		facts.add(String.format("(PacmanToBLINKY (distanceTo %f))", 
				this.distanceFromPacmanToGhost.get(GHOST.BLINKY)));
		facts.add(String.format("(PacmanToINKY (distanceTo %f))", 
				this.distanceFromPacmanToGhost.get(GHOST.INKY)));
		facts.add(String.format("(PacmanToPINKY (distanceTo %f))", 
				this.distanceFromPacmanToGhost.get(GHOST.PINKY)));
		facts.add(String.format("(PacmanToSUE (distanceTo %f))", 
				this.distanceFromPacmanToGhost.get(GHOST.SUE)));
		
		//ADD DISTANCE FROM GHOST TO GHOST //
		//BLINKY
		facts.add(String.format("(BLINKYtoINKY (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.INKY)));
		facts.add(String.format("(BLINKYtoPINKY (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.PINKY)));
		facts.add(String.format("(BLINKYtoSUE (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.BLINKY).get(GHOST.SUE)));
		//INKY
		facts.add(String.format("(INKYToBLINKY (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.BLINKY)));
		facts.add(String.format("(INKYToPINKY (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.PINKY)));
		facts.add(String.format("(INKYToSUE (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.INKY).get(GHOST.SUE)));
		//PINKY
		facts.add(String.format("(PINKYToBLINKY (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.BLINKY)));
		facts.add(String.format("(PINKYToINKY (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.INKY)));
		facts.add(String.format("(PINKYToSUE (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.PINKY).get(GHOST.SUE)));
		//SUE
		facts.add(String.format("(SUEToBLINKY (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.BLINKY)));
		facts.add(String.format("(SUEToINKY (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.INKY)));
		facts.add(String.format("(SUEToPINKY (distanceTo %f))", 
				this.distanceFromGhostToGhost.get(GHOST.SUE).get(GHOST.PINKY)));
					
		
		// SHIELD GHOST //
		
		if(!Objects.isNull(this.shieldGhost.get(GHOST.BLINKY))) {
			facts.add(String.format("(BLINKYshieldGhost ", "ghost %s))", 
					this.shieldGhost.get(GHOST.BLINKY)));			
		}
		
		if(!Objects.isNull(this.shieldGhost.get(GHOST.INKY))) {
			facts.add(String.format("(INKYshieldGhost ", "(ghost %s))",
					this.shieldGhost.get(GHOST.INKY)));		
		}
		
		if(!Objects.isNull(this.shieldGhost.get(GHOST.PINKY))) {
			facts.add(String.format("(PINKYshieldGhost ", "(ghost %s))", 
					this.shieldGhost.get(GHOST.PINKY)));		
		}
		
		if(!Objects.isNull(this.shieldGhost.get(GHOST.SUE))) {
			facts.add(String.format("(SUEshieldGhost ", "(ghost %s))",
					this.shieldGhost.get(GHOST.SUE)));		
		}
		
		// DISTANCE TO NEXT PACMAN INTERSECTION // 
		facts.add(String.format("(BLINKYToIntersection (distanceTo  %f))", 
				ghostToIntersection.get(GHOST.BLINKY)));
		facts.add(String.format("(INKYToIntersection (distanceTo  %f))", 
				ghostToIntersection.get(GHOST.INKY)));
		facts.add(String.format("PINKYToIntersection (distanceTo  %f))", 
				ghostToIntersection.get(GHOST.PINKY)));
		facts.add(String.format("SUEToIntersection (distanceTo  %f))", 
				ghostToIntersection.get(GHOST.SUE)));
		
		
		// EDIBLE TIME // 
		facts.add(String.format("(BLINKYedible (edibleTime %d))", 
				this.game.getGhostEdibleTime(GHOST.BLINKY)));
		facts.add(String.format("(INKYedible (edibleTime %d))", 
				this.game.getGhostEdibleTime(GHOST.INKY)));
		facts.add(String.format("(PINKYedible (edibleTime %d))", 
				this.game.getGhostEdibleTime(GHOST.PINKY)));
		facts.add(String.format("(SUEedible (edibleTime %d))", 
				this.game.getGhostEdibleTime(GHOST.SUE)));
		
		// LAIR TIME // 
		facts.add(String.format("(BLINKYlair (lairTime %d))", 
				this.game.getGhostEdibleTime(GHOST.BLINKY)));
		facts.add(String.format("(INKYlair (lairTime %d))", 
				this.game.getGhostEdibleTime(GHOST.INKY)));
		facts.add(String.format("(PINKYlair (lairTime %d))", 
				this.game.getGhostEdibleTime(GHOST.PINKY)));
		facts.add(String.format("(SUElair (lairTime %d))", 
				this.game.getGhostEdibleTime(GHOST.SUE)));
				
		return facts;
	}
	
	private void parseDistanceFromGhostToPacman() {
		distanceFromGhostToPacman = new HashMap<>();
		for(GHOST g : GHOST.values()) {
			double auxDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g) , game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
			distanceFromGhostToPacman.put(g, auxDistance);
		}
		
	}
	
	private void parseDistanceFromPacManToGhost() {
		distanceFromPacmanToGhost = new HashMap<>();
		
		for(GHOST g : GHOST.values()) {
		if(game.getGhostLairTime(g)==0) {	
			double auxDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),game.getPacmanLastMoveMade());
			distanceFromPacmanToGhost.put(g, auxDistance);
			}
		}
		
	}
	
	private void parseDistanceGhostToGhost() {
		distanceFromGhostToGhost= new HashMap<>();
		for(GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g)==0) {
				Map<GHOST,Double> auxMap = new HashMap<>();
				for(GHOST otherghost : GHOST.values()) {
					if(game.getGhostLairTime(otherghost)==0){ 	
						double auxdistance = game.getShortestPathDistance( game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(otherghost),game.getGhostLastMoveMade(g));
						auxMap.put(otherghost, auxdistance);
					}
				}
			distanceFromGhostToGhost.put(g, auxMap);
			
			}
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
				double distanceToShielder = game.getDistance(shielderPosition, ghostPosition, game.getGhostLastMoveMade(shield), DM.PATH);
				// ajuste asumiendo que el fantasma se esta dirigiendo a su escudero, camina la mitad que el otro porque es comestible 
				// asi que nos basamos en la velocidad media de los fantasmas para ver cual seria la distancia que recorreria el escudero
				// antes de encontrarse. Simplificado con las constantes de velocidad de los fantasmas queda 3/4 de la distancia
				distanceToShielder -= 1/4 * distanceToShielder;
								
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
	

