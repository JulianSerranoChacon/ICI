package es.ucm.fdi.ici.c2526.practica1.grupoH;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import java.util.LinkedList;


public class MsPacMan extends PacmanController{
	 class Chunckdata{
		 	 LinkedList<Integer> nodepillList = new LinkedList<>();
		 
	}
	LinkedList<Chunckdata> chunkList;
	int iniNumChunk =  0;
	int limit = 65;
	boolean firstFrame = true;
	int maxNodeperChunk = 25;
    @Override
    
    //dividimos entre 4
    public MOVE getMove(Game game, long timeDue) {
    	if(firstFrame) {
    	this.chunkList = new LinkedList<>();
		int aux[] = game.getActivePillsIndices();
		int i = 0;
		
		while( i < aux.length) {
			Chunckdata auxChunk = new Chunckdata();
			for(int j = 0; j< maxNodeperChunk;j++) {
			if(i<aux.length) {
			 auxChunk.nodepillList.push(aux[i]);
			
			 i++;
			 }
			}
			chunkList.push(auxChunk);
			}

		iniNumChunk = chunkList.size();

		firstFrame = false;
		return MOVE.NEUTRAL;
    	}
    	int nearGhost = limit;
		int nearGhostIndex = 0;
		int nearGhostTime = 0;
		boolean nearghost = false;
		for (int i = 0; i <  GHOST.values().length; i++) {
			int auxgpos = game.getGhostCurrentNodeIndex((GHOST.values()[i]));
			int auxnear = limit;
			if(game.getGhostLairTime(GHOST.values()[i])==0)  auxnear = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex() ,auxgpos, MOVE.NEUTRAL);
			if(nearGhost>auxnear) {
				nearGhost = auxnear;
				nearGhostIndex = i;
				nearGhostTime = game.getGhostEdibleTime(GHOST.values()[i]);
				nearghost = true;
			}
		}
		MOVE auxTorwardsToGhost = MOVE.NEUTRAL;
		MOVE auxAwayMove = MOVE.NEUTRAL;
		//if powerup
		if(nearghost) {
			auxTorwardsToGhost= game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(GHOST.values()[nearGhostIndex]),DM.PATH);
			 auxAwayMove= game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(GHOST.values()[nearGhostIndex]), DM.PATH);
		if(nearGhostTime>0) {
			//Va a por el fantasma porq se puede comer
			return auxTorwardsToGhost;
		}
		else {
			return auxAwayMove;
		}
		}
    	//Calculate msPacman act chunk
    	int MSPCactChunk = 0;
    	int  i = 0;
    	boolean flag = false;
    	while(i<chunkList.size() &&!flag) {
    		Chunckdata aux = chunkList.get(i);
    
    		if(game.getPacmanCurrentNodeIndex()>=chunkList.get(i).nodepillList.get(chunkList.get(i).nodepillList.size()-1)&&
    				game.getPacmanCurrentNodeIndex()<chunkList.get(i).nodepillList.get(0)) {MSPCactChunk = i;
    		flag = true;
    		}
    		i++;
    	}

    	MOVE auxMOVEtoPill = MOVE.NEUTRAL;
    	int nearPillIndex = -1;
    	int nearPillDistance = 99999;
    	
    	//remove a pill in pacman pos
    	int auxNode = -1;
    	for(int a :chunkList.get(MSPCactChunk).nodepillList) { 
    		if(a==game.getPacmanCurrentNodeIndex()) { auxNode = a;
    
    			}
    		}
    	
    	if (auxNode !=-1)for(int j =0;j<chunkList.get(MSPCactChunk).nodepillList.size();j++) {
    		if(chunkList.get(MSPCactChunk).nodepillList.get(j)==auxNode) {chunkList.get(MSPCactChunk).nodepillList.remove(j);
    		
    		if(chunkList.get(MSPCactChunk).nodepillList.size()==0) chunkList.remove(MSPCactChunk);
    		}
    		}
    	
    	for(int a  :chunkList.get(MSPCactChunk).nodepillList) {
    		
    			int auxDistance =game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), a );
    			if(auxDistance<nearPillDistance) {
    			 nearPillIndex = a;
    			 nearPillDistance = auxDistance;
    		}
    	
    		
    	}
    	
  
    	if(nearPillIndex !=-1) auxMOVEtoPill = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearPillIndex, DM.PATH);
    	return auxMOVEtoPill;
    	
    	//return MOVE.NEUTRAL;
    }
    
    public String getName() {
    	return "MsPacManNeutral";
    }

}