package es.ucm.fdi.ici.c2425.practica0.SerranoChacon;

import java.util.EnumMap;
import java.util.Random;


import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public final class Ghosts extends GhostController {
	
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
    private MOVE[] allMoves = MOVE.values();
    private Random rnd = new Random();
    private int limitPill = 10;
    
    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
    	moves.clear();
        
        for (GHOST ghostType : GHOST.values()) {
        	
            if (game.doesGhostRequireAction(ghostType)) {
                
                int nearestPill;
        		nearestPill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),
        				game.getActivePowerPillsIndices(),DM.PATH);
        		
                if(game.isGhostEdible(ghostType) || nearestPill < limitPill) {
                	//System.out.println("hide");
                	moves.put(ghostType,game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType),
            				game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(ghostType),DM.EUCLID));
                }
                
                float next = rnd.nextFloat();
                
                if(next >= 90)
                	moves.put(ghostType,game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
            				game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(ghostType),DM.EUCLID));
                else 

                    moves.put(ghostType, allMoves[rnd.nextInt(allMoves.length)]);
                
            }
            
        }
        return moves;
    }
    
    public String getName() {
    	return "Ghosts";
    }
}
