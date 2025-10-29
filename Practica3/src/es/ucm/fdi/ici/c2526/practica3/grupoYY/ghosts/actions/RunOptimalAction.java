package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunOptimalAction implements RulesAction {

    GHOST ghost;
    int pacmanNode;
    int mghostNode;
	public RunOptimalAction(GHOST ghost) {
		this.ghost = ghost;
	}
	
	//Huida optima david se lo mejora
	@Override
	public MOVE execute(Game game) {
		if(!game.doesGhostRequireAction(ghost)) 
			return MOVE.NEUTRAL;
        
		int nextIntersectNode = 99999;
		 int nearestDistance = 99999;
		//Calcule the next junction and the distance to it
		 int[] juntions =  game.getJunctionIndices();
		 for(int i = 0; i < juntions.length;i++) {
			 int aux = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), i,game.getGhostLastMoveMade(ghost) );
			 if(aux<nearestDistance) {
				 nextIntersectNode = i;
				 nearestDistance = aux;
			 }
			 
		 }
		//Calcule the paht to this juntion 
		int[] pathToIntersect = game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), nextIntersectNode);
		boolean ghostInMyWay = false;
		for(int n : pathToIntersect) {
			for(GHOST g : GHOST.values())if(g != ghost && game.getGhostCurrentNodeIndex(g)==n)  ghostInMyWay = true;
		}
		 
		if(!ghostInMyWay) {
		return game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),
        game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
		}
		else {
			MOVE toPacman =  game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
			        game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
			for(MOVE m : MOVE.values()) {
				if(m != toPacman &&m != MOVE.NEUTRAL) return m;
			}
			return MOVE.NEUTRAL;
		}
        
       
	}

	@Override
	public void parseFact(Fact actionFact) {
		
		
	}

	@Override
	public String getActionId() {
		return ghost+ "runsOptimal";
	}

}
