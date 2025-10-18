package es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.PacmanInfo;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToNearestPillAction implements Action {
	PacmanInfo pi;

	public GoToNearestPillAction(PacmanInfo pi) {
		this.pi = pi;
	}
	
	@Override
	public MOVE execute(Game game) {
		Map<Double, Integer> distToPill = new HashMap<>();
		PriorityQueue <Double> queue = new PriorityQueue<Double>();
		for(int pill : game.getPillIndices()) {
			if (game.isPillStillAvailable(pill)) {
				queue.add(game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH));
				distToPill.put(game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH), pill);
			}
		}
		
		//We go through the pills trying to see what is the first one that is easier to reach and does not kill us
		while(!queue.isEmpty()) {
			double aux = queue.poll();
			for (MOVE m : pi.getCandidateMoves()) {
				if (game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), distToPill.get(aux), game.getPacmanLastMoveMade(), DM.PATH) == m) {
					return m;
				}
			}
		}
		
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), pi.getClosestPPill(), game.getPacmanLastMoveMade(), DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Goes to nearest pill action";
	}

}
