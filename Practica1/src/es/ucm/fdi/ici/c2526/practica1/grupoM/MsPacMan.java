
package es.ucm.fdi.ici.c2526.practica1.grupoM;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MsPacMan extends PacmanController{

	private int _dist = 40;
	private int _d = 20;
	
    @Override
    public MOVE getMove(Game game, long timeDue) {
    	
    	int pacManPos = game.getPacmanCurrentNodeIndex();
		int moveTowards = -1;
    	MOVE nMove = null; 
    	
    	//Comprobar si tiene 2 o mas fantasmas a menos de D distancia
    	int nearG = getNearGhosts(game, pacManPos, _dist);
    	
    	//si >= 2 va hacia la powerPill mas cercana
    	if(nearG >= 2) {
    		int nPP = getNearestPowerPill(pacManPos, game);
			//Si quedan PowerPills activas
			if (nPP != -1)
				moveTowards = nPP;
    	}
    	
	//si no, intenta perseguir un fantasma que se pueda comer
		int ghostToChase = fantasmaComestible(game, pacManPos);
		if(moveTowards == -1 && ghostToChase != -1) {
			moveTowards = ghostToChase;
		}

		//va hacia la pill mas cercana que no tenga fantasmas a cierta distancia d
		if(moveTowards == -1) moveTowards = nextPillSegura(game, pacManPos);
		
		int[] path = game.getShortestPath(pacManPos, moveTowards);
		int ghostCollide = hayFantasmaEnMedio(game, path, pacManPos, moveTowards);
		//si hay un fantasma en el camino que va a seguir pacman hacia su objetivo, huye del fantasma en su lugar
		if(ghostCollide != -1) {
			GameView.addLines(game, Color.YELLOW, pacManPos, ghostCollide);
			return game.getApproximateNextMoveAwayFromTarget(pacManPos, ghostCollide , game.getPacmanLastMoveMade() , DM.PATH);
		}
		else{
			GameView.addLines(game, Color.RED, pacManPos, moveTowards);
			return game.getApproximateNextMoveTowardsTarget(pacManPos, moveTowards, game.getPacmanLastMoveMade() , DM.PATH);	
		}
    }
    
    private int fantasmaComestible(Game game, int pacManPos) {
    	GHOST ghostToChase = null;
    	double dist = Integer.MAX_VALUE;
    	for(GHOST ghostType : GHOST.values()) {
    		double gD = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), pacManPos, game.getPacmanLastMoveMade());
    				//game.getDistance(game.getGhostCurrentNodeIndex(ghostType), pacManPos, game.getPacmanLastMoveMade(),DM.PATH);
    		//System.out.println(ghostType.name() + ": distancia: " + gD);
    		if(game.isGhostEdible(ghostType) && 
    				//game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), pacManPos, game.getPacmanLastMoveMade()) < dist
    				 gD < dist
    				) {
    			ghostToChase = ghostType;
    			dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), pacManPos, game.getPacmanLastMoveMade());
    		}
    	}
    	if(ghostToChase == null) return -1;
    	return game.getGhostCurrentNodeIndex(ghostToChase);
    }
    
    private int hayFantasmaEnMedio(Game game, int[] path, int pacManPos, int nPP) {
    	List<Integer> ghostPos = new ArrayList<Integer>();
    	for	(GHOST ghostType : GHOST.values())
    		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0) {
	    		ghostPos.add(game.getGhostCurrentNodeIndex(ghostType));
	    	
	    	for(int i : path) {
	    		if (ghostPos.contains(i)) return i;
	    	}
    	}
    	return -1;
    	
    }
    
    private int nextPillSegura(Game game, int pacManPos) {
    	
    	double minDist = Integer.MAX_VALUE;
    	int nPill = 0;
    	for	(int pill : game.getActivePillsIndices()) {
    		double dist = game.getShortestPathDistance(pacManPos, pill, game.getPacmanLastMoveMade());
    		if(dist < minDist && esSegura(game, pill)) {
    			minDist = dist;
    			nPill = pill;
    		}
    	}
    	return nPill;
    }
    
    private Boolean esSegura(Game game, int pill) {
    	return getNearGhosts(game, pill, _d) == 0;
    }
    
    private int getNearGhosts(Game game, int pacManPos, int d) {
    	int i = 0;
    	
    	for(GHOST ghostType : GHOST.values()) {
    		int ghostPos = game.getGhostCurrentNodeIndex(ghostType);
    		
    		if(game.getGhostLairTime(ghostType) <= 0 && game.getGhostEdibleTime(ghostType) <= 0) {
    			double dist = game.getDistance(pacManPos, ghostPos,DM.PATH);
    			if(dist < d){
    				i++;
    				GameView.addLines(game, Color.BLUE, pacManPos, ghostPos);
    			}
    		}
    	}
    	return i;
    }
    
    private int getNearestPowerPill(int pacManPos, Game game) {
    	double minDist = Integer.MAX_VALUE;
    	int nPpill = -1;
    	//Si no hay PowerPills activas devuelve -1
    	if(game.getNumberOfActivePowerPills() == 0) return -1;
    	
    	for	(int ppill : game.getActivePowerPillsIndices()) {
    		double dist = game.getShortestPathDistance(pacManPos, ppill, game.getPacmanLastMoveMade());
    		if(dist < minDist) {
    			minDist = dist;
    			nPpill = ppill;
    		}
    	}
    	return nPpill;
    }
    
    public String getName() {
    	return "MsPacManNeutral";
    }

}
