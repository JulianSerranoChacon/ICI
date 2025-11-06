package es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Hunter2Action implements RulesAction  {
	private GHOST ghost;
	
	public Hunter2Action(GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public MOVE execute(Game game) {
		if(!game.doesGhostRequireAction(ghost))
			return MOVE.NEUTRAL;
		
		//Gather candidate nodes
		List<Integer> candidateNodes = getCandidateNodes(game);
		
		int bestNode = game.getPacmanCurrentNodeIndex(); int bestPacmanDistance = Integer.MAX_VALUE; boolean found = false;
		//We go through all junctions near pacman
		for(int n : candidateNodes) {
			if(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), n, game.getGhostLastMoveMade(ghost)) 
					<= game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), n, game.getPacmanLastMoveMade())
				&& bestPacmanDistance > game.getShortestPathDistance(n, game.getPacmanCurrentNodeIndex())) {
				bestNode = n;
				bestPacmanDistance = game.getShortestPathDistance(n, game.getPacmanCurrentNodeIndex());
				found = true;
			}
		}
		
		if(found) {
			return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), bestNode, game.getGhostLastMoveMade(ghost), DM.PATH);
		}
		
		List<Integer> secondCandidates = getSecondCandidates(game, candidateNodes);
		
		bestNode = game.getPacmanCurrentNodeIndex(); double bestDistance = Double.MAX_VALUE;
		if(game.getNumberOfActivePowerPills() != 0) {
			for(int n : secondCandidates) {
				for(int ppillNode : game.getActivePowerPillsIndices()) {
					if(game.isPowerPillStillAvailable(ppillNode) 
							&& bestDistance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), n, game.getGhostLastMoveMade(ghost))) {
						bestNode = n;
						bestDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), n, game.getGhostLastMoveMade(ghost));
					}
				}
			}
		}
		else {
			for(int n : secondCandidates) {
				if(bestPacmanDistance > game.getShortestPathDistance(n, game.getPacmanCurrentNodeIndex())) {
					bestNode = n;
					bestPacmanDistance = game.getShortestPathDistance(n, game.getPacmanCurrentNodeIndex());
				}
			}
		}
		
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), bestNode, game.getGhostLastMoveMade(ghost), DM.PATH);
	}
	
	private List<Integer> getCandidateNodes(Game game){
		//Auxiliar variables
		int node = game.getPacmanCurrentNodeIndex();
		Set<Integer> visited = new HashSet<>();
		//Variables that keep the junction
		List<Integer> candidateNodes = new ArrayList<>();
		
		//Pacman possible moves (BFS profundidad 1)
		for (MOVE m : game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
			// Get node
			node = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);
			MOVE dirToMove = m;

			while (!game.isJunction(node)) {
				// If the node is one corner
				if (game.getNeighbour(node, dirToMove) == -1) {
					int[] possibleNodes = game.getNeighbouringNodes(node);
					for (int i : possibleNodes) {
						if (!visited.contains(i) && i != -1) {
							dirToMove = game.getMoveToMakeToReachDirectNeighbour(node, i);
							node = i;
						}
					}
				} 
				else {
					node = game.getNeighbour(node, dirToMove);
				}
				visited.add(node);
			}
			candidateNodes.add(node);
		}
		
		return candidateNodes;
	}
	
	private List<Integer> getSecondCandidates(Game game, List<Integer> candidateNodes){
		//Pacman possible moves (BFS profundidad 2)
		//Auxiliar variables
		int node = game.getPacmanCurrentNodeIndex();
		Set<Integer> visited = new HashSet<>();
		//Solution
		List<Integer> secondCandidates = new ArrayList<>();
		
		for (int n : candidateNodes) {
			node = n;
			secondCandidates.add(node);
			for(MOVE m : game.getPossibleMoves(n)) {
				MOVE dirToMove = m;
				while (!game.isJunction(node)) {
					// If the node is one corner
					if (game.getNeighbour(node, dirToMove) == -1) {
						int[] possibleNodes = game.getNeighbouringNodes(node);
						for (int i : possibleNodes) {
							if (!visited.contains(i) && i != -1) {
								dirToMove = game.getMoveToMakeToReachDirectNeighbour(node, i);
								node = i;
							}
						}
					} 
					else {
						node = game.getNeighbour(node, dirToMove);
					}
					visited.add(node);
				}
			}
			secondCandidates.add(node);
		}
		
		return secondCandidates;
	}
	
	@Override
	public void parseFact(Fact actionFact) {}

	@Override
	public String getActionId() {
		return ghost + "Hunter2";
	}

}