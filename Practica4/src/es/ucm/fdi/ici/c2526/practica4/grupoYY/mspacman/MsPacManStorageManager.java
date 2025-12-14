package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManStorageManager {
	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	Vector<Info> bufferInfo;
	
	private final static Double UMBRAL_SIMILITUD = 0.7; // umbral de similitud propuesto para empezar a considerar casos similares
	
	private class Info
	{
		private int numLives;
		private int numPills;
		private int numGhostEaten;
		private Collection<RetrievalResult> eval;

		public Info(int numLives, int numPills, int ghostNotActive, Collection<RetrievalResult> eval) {
			this.numLives = numLives;
			this.numPills = numPills;
			this.numGhostEaten = ghostNotActive;
			this.eval = eval;
		}
	}
	
	//Constante de tiempo
	private final static int TIME_WINDOW = 5;
	
	//Constante de recuerdo // 
	private static final Integer UMBRAL_CONSERVAR = 30;
	private static final Integer minMediocre = -35;
	private static final Integer maxMediocre = 35;
	
	//Constantes de revision //	
	private static final Integer UMBRAL_DISTANCIA_DEFENSA = 45;
	private final static Integer SCORE_FANTASMA_COMIDO = 200;
	//Recompensas
	private final static Integer RECOMPENSA_FANTASMA_DEBIL_CERCA = 25;
	private final static Double  RECOMPENSA_PILL_COMIDA = 3.05;
	private static final Integer RECOMPENSA_ALEJADO_FANTASMA = 15;
	//Penalizaciones
	private static final Integer PENALIZANDO_ALEJADO_FANTASMA = -30;
	private final static Integer PENALIZACION_PPILL = -80;
	private final static Integer PENALIZACION_MUERTE = -60;
	
	public MsPacManStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
		this.bufferInfo = new Vector<Info>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBase(CBRCaseBase caseBase)
	{
		this.caseBase = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase, Collection<RetrievalResult> eval)
	{			
		this.buffer.add(newCase);
		//We can keep extra information of the game that would be unnecesary in a case.
		this.bufferInfo.add(new Info(game.getPacmanNumberOfLivesRemaining(), game.getNumberOfActivePowerPills(), game.getNumGhostsEaten(),eval));
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		Info infoCase = this.bufferInfo.remove(0);
		reviseCase(bCase, infoCase);
		retainCase(bCase, infoCase.eval);
		
	}
	
	private void reviseCase(CBRCase bCase, Info infoCase) {
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		
		//Base score -> this intersection
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int finalScore = currentScore - oldScore;
		
		//REVISAR SCORES NEGATIVOS
		if(finalScore < 0 ) finalScore = currentScore;
		//Final pill
		int value = 0;
	
		//Con ppill
		if(infoCase.numPills > 0) {
			
			//Fantasmas comidos -> Recompensamos comer fantasmas
			if (finalScore >= SCORE_FANTASMA_COMIDO){
				//Empiricamente el numero de fantasmas
				int num_fantasmas = 0;
				for(int i = SCORE_FANTASMA_COMIDO; finalScore > 0; i *= 2) {
					finalScore -= i;
					if(finalScore > 0) {
						num_fantasmas++;
					}
				}
				
				switch(num_fantasmas) {
				case 0: 
					value += 0;
					break;
				case 1:
					value += 45;
					break;
				case 2:
					value += 85;
					break;
				case 3:
					value += 150;
					break;
				case 4:
					value += 250;
					break;
				}
			}
			
			// Supervivencia 
			// Recompensamos a Pacman si esta mas cerca de la PPill o si esta bastante lejos del fantasma
			List<Integer> toPacmanFromNotEdible = new ArrayList<>();
			List<Integer> toNotEdibleFromPacman = new ArrayList<>();
			for(GHOST g : GHOST.values()) {
				if(game.isGhostEdible(g) || game.getGhostLairTime(g) > 0) {
					continue;
				}
				
				toPacmanFromNotEdible.add(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex()));
				toNotEdibleFromPacman.add(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g)));
			}
			
			
			
			if(!toPacmanFromNotEdible.isEmpty()) {

				for (int i = 0; i < toPacmanFromNotEdible.size(); i++) {
					if(toPacmanFromNotEdible.get(i) > description.getNearestPill() || toPacmanFromNotEdible.get(i) >= UMBRAL_DISTANCIA_DEFENSA) {
						value += RECOMPENSA_ALEJADO_FANTASMA;
					}
					else if (toPacmanFromNotEdible.get(i) <= toNotEdibleFromPacman.get(i)) {
						value += PENALIZANDO_ALEJADO_FANTASMA;
					}
				}
			}
			
			// Caza 
			int num_ghost_reachable = 0;
			for(GHOST g : GHOST.values()) {
				if(game.isGhostEdible(g) && ghostReachable(game, g)) {
					num_ghost_reachable++;
				}
			}
			value += RECOMPENSA_FANTASMA_DEBIL_CERCA * num_ghost_reachable;
			
			//Penalizamos el uso inapropiado de la Power Pills
			if(game.getNumberOfActivePowerPills() < infoCase.numPills && infoCase.numGhostEaten > game.getNumGhostsEaten() && finalScore < SCORE_FANTASMA_COMIDO) {
				value += PENALIZACION_PPILL;
			}
		}
	
		
		//Sin ppill
		else {
			
			if (finalScore >= SCORE_FANTASMA_COMIDO){
				//Empiricamente el numero de fantasmas
				int num_fantasmas = 0;
				for(int i = SCORE_FANTASMA_COMIDO; finalScore > 0; i *= 2) {
					finalScore -= i;
					if(finalScore > 0) {
						num_fantasmas++;
					}
					//If score is negative, we get the score without ghosts and exit the loop
					else if(finalScore < 0) {
						finalScore += i;
						break;
					}
				}
				
				switch(num_fantasmas) {
				case 0: 
					value += 0;
					break;
				case 1:
					value += 45;
					break;
				case 2:
					value += 85;
					break;
				case 3:
					value += 150;
					break;
				case 4:
					value += 250;
					break;
				}
			}
			
			// Supervivencia 
			// Recompensamos a Pacman si esta mas cerca de la PPill o si esta bastante lejos del fantasma
			List<Integer> toPacmanFromNotEdible = new ArrayList<>();
			List<Integer> toNotEdibleFromPacman = new ArrayList<>();
			for(GHOST g : GHOST.values()) {
				if(game.isGhostEdible(g) || game.getGhostLairTime(g) > 0) {
					continue;
				}
				
				toPacmanFromNotEdible.add(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex()));
				toNotEdibleFromPacman.add(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g)));
			}
			
			if(!toPacmanFromNotEdible.isEmpty()) {

				for (int i = 0; i < toPacmanFromNotEdible.size(); i++) {
					if(toPacmanFromNotEdible.get(i) > description.getNearestPill() || toPacmanFromNotEdible.get(i) >= UMBRAL_DISTANCIA_DEFENSA) {
						value += RECOMPENSA_ALEJADO_FANTASMA;
					}
					else if (toPacmanFromNotEdible.get(i) > toNotEdibleFromPacman.get(i)) {
						value += PENALIZANDO_ALEJADO_FANTASMA;
					}
				}
			}
			
			//Consideramos las pills comidas 
			value += Math.round(RECOMPENSA_PILL_COMIDA * finalScore/10);
		}
		
		//Penalizamos la muerte quitando parte del resultado
		if(game.getPacmanNumberOfLivesRemaining() < infoCase.numLives) {
			value += PENALIZACION_MUERTE;
		}
		MsPacManResult result = (MsPacManResult)bCase.getResult();
        result.setScore(value);	
	}
	
	private void retainCase(CBRCase bCase, Collection<RetrievalResult> cases)
	{
		//Options:		
		//If there is no other cases to compare it to, then there needs to be added
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		if(result.score >= minMediocre && result.score <= maxMediocre) {
			return;
		}
		if(Objects.isNull(cases)) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
			return;
		}
		
		//Case outcome
		MsPacManResult scorebCase = (MsPacManResult) bCase.getResult(); 
		MsPacManSolution movebCase = (MsPacManSolution) bCase.getSolution(); 
		
		//Mediocre case
		double umbral_diff = UMBRAL_CONSERVAR;		
		RetrievalResult mediocreRR = null;
		
		//Repeat reuse process
		Map<MOVE, double[]> InitialDirToScore = new HashMap<>();
		for(RetrievalResult cbrCase : cases) {
			//Truncar la lista con 0,7
			if(cbrCase.getEval() > UMBRAL_SIMILITUD) {
				MsPacManResult scoreCase = (MsPacManResult) cbrCase.get_case().getResult(); 
				MsPacManSolution moveCase = (MsPacManSolution) cbrCase.get_case().getSolution(); 
				double weight = scoreCase.getScore() * cbrCase.getEval() * cbrCase.getEval();
				
				//Get mediocre case
				if(movebCase.getAction() == moveCase.getAction() && Math.abs(scoreCase.getScore() - scorebCase.getScore()) > umbral_diff) {
					umbral_diff = Math.abs(scoreCase.getScore() - scorebCase.getScore());
					mediocreRR = cbrCase;
				}
				
				if (!InitialDirToScore.containsKey(moveCase.getAction())) {
					InitialDirToScore.put(moveCase.getAction(), new double[] {weight, 1});
				} 
				else {
					InitialDirToScore.replace(moveCase.getAction(), new double[] {InitialDirToScore.get(moveCase.getAction())[0] + weight, InitialDirToScore.get(moveCase.getAction())[1] + 1});
				}
			}
		}
		
		
		//Add new case and see the change
		Map<MOVE, double[]> FinalDirToScore = new HashMap<Constants.MOVE, double[]>();
		for (Map.Entry<MOVE, double[]> entry : InitialDirToScore.entrySet()) {
		    FinalDirToScore.put(entry.getKey(), entry.getValue().clone() // copia del array
		    );
		}
		if (!FinalDirToScore.containsKey(movebCase.getAction())) {
			StoreCasesMethod.storeCase(caseBase, bCase);
		} 
		else {
			FinalDirToScore.replace(movebCase.getAction(), new double[] {FinalDirToScore.get(movebCase.getAction())[0] + scorebCase.getScore() * 0.87, FinalDirToScore.get(movebCase.getAction())[1] + 1});
		}
		
		// See our choice initially
		MOVE initialBestMove = MOVE.NEUTRAL; Double initialBestScore = Double.MIN_VALUE;
		for (Entry<MOVE, double[]> weight : InitialDirToScore.entrySet()) {
			weight.getValue()[0] = weight.getValue()[0] / weight.getValue()[1];
			if (weight.getValue()[0] > initialBestScore) {
				initialBestScore = weight.getValue()[0];
				initialBestMove = weight.getKey();
			}
		}
		
		// See our choice now
		MOVE finalBestMove = MOVE.NEUTRAL; Double finalBestScore = Double.MIN_VALUE;
		for (Entry<MOVE, double[]> weight : InitialDirToScore.entrySet()) {
			weight.getValue()[0] = weight.getValue()[0] / weight.getValue()[1];
			if (weight.getValue()[0] > finalBestScore) {
				finalBestScore = weight.getValue()[0];
				finalBestMove = weight.getKey();
			}
		}
		
		//If our choice changes, we need to keep it for future reference
		if (initialBestMove != finalBestMove) {
			StoreCasesMethod.storeCase(caseBase, bCase);
		}
		//If the difference between now and then is distinguishable we keep
//		else if (Math.abs(finalBestScore - initialBestScore) > UMBRAL_CONSERVAR){
//			StoreCasesMethod.storeCase(caseBase, bCase);
//		}
		//We exchange it with the mediocre case if necessary
		else if (!Objects.isNull(mediocreRR)){
			//Forget the case
			Collection<CBRCase> aux = new ArrayList<CBRCase>();
			aux.add(mediocreRR.get_case());
			caseBase.forgetCases(aux);
			
			//Add only if outside of the ratio stablished
			StoreCasesMethod.storeCase(caseBase, bCase);
		}
	}

	public void close() {
		for(int i = 0; i < buffer.size(); i++)
		{
			reviseCase(buffer.get(i), bufferInfo.get(i));
			retainCase(buffer.get(i), bufferInfo.get(i).eval);
		}
		this.buffer.removeAllElements();
		this.bufferInfo.removeAllElements();
	}
	
	private boolean ghostReachable(Game game, GHOST ghost) {
		//return 2 * game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH) 
		//		<= game.getGhostEdibleTime(ghost);
		
		if(game.getGhostLairTime(ghost) > 0) {
			return false;
		}
		
		double distanceToGhostPosition = game.getDistance(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH);
		
		if (game.getGhostLastMoveMade(ghost) != game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH)) {
			return game.getGhostEdibleTime(ghost) >= 2 * distanceToGhostPosition;
		}
		
		return game.getGhostEdibleTime(ghost) >= distanceToGhostPosition;
	}
	
	public int getPendingCases() {
		return this.buffer.size();
	}
}
