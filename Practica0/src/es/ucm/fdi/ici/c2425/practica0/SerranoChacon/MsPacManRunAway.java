package es.ucm.fdi.ici.c2425.practica0.SerranoChacon;


import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManRunAway extends PacmanController{

	@Override
	public MOVE getMove(Game game, long timeDue) {
		//this search the nearest ghost
		MOVE move = game.getPacmanLastMoveMade(); 
		double nearestDistance = 0;
		
		for (GHOST ghostType : GHOST.values()) {
			if(nearestDistance == 0 || 
					game.getEuclideanDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType)) 
					< nearestDistance) {
				
				move = game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),
						game.getGhostCurrentNodeIndex(ghostType),game.getPacmanLastMoveMade(),DM.EUCLID);
			}
		}
		
		return move;
	}

    public String getName() {
    	return "MsPacManRunAway";
    }
}
