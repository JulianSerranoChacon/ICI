package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import java.util.Objects;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Hunter2Action implements RulesAction  {
	private static final int DISTANCE_LIMIT = 90; //Limit distance between Ghosts in the same road
	
    private GHOST ghost;
	private GHOST hunter1;
    private int targetNode = 0;
	public Hunter2Action(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if(!game.doesGhostRequireAction(ghost))
			return MOVE.NEUTRAL;
		//En teoria esto evita q compruebe el camino por el que viene pacman si no meter un .oposite al movimiento
	int[] targetNodeNb = game.getNeighbouringNodes(
			targetNode, game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
					targetNode, game.getPacmanLastMoveMade(), DM.PATH));
		int nearInterNodeIndex = targetNode;
		int distantToNearInterNode = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), nearInterNodeIndex,
				game.getGhostLastMoveMade(ghost));
		int[] nextNodesNb;
		for(int i = 0; i< targetNodeNb.length;i++) {
			
			MOVE MOVEtoNb = game.getNextMoveTowardsTarget(targetNode,targetNodeNb[i],DM.PATH);
			nextNodesNb = game.getNeighbouringNodes(targetNodeNb[i], MOVEtoNb);
			while(nextNodesNb.length==1) {
				nextNodesNb = game.getNeighbouringNodes(nextNodesNb[0], MOVEtoNb);
			}
			int aux = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), nextNodesNb[0], game.getGhostLastMoveMade(ghost));
			if(aux < distantToNearInterNode ) {
				nearInterNodeIndex = nextNodesNb[0];
				distantToNearInterNode  = aux;
			}
		}
		
		return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), nearInterNodeIndex, game.getGhostLastMoveMade(ghost),DM.PATH);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value target = actionFact.getSlotValue("intersection");
			if(!Objects.isNull(target)) {
				targetNode =  target.intValue(null);
			}
			Value hunter1Role = actionFact.getSlotValue("extraGhost");
			if(!Objects.isNull(hunter1Role)) {
				String strategyValue = hunter1Role.stringValue(null);
				hunter1 = GHOST.valueOf(strategyValue);
			}
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getActionId() {
		return ghost + "Hunter2";
	}

}