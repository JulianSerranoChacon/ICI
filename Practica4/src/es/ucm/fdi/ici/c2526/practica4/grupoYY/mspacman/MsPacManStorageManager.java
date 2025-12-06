package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class MsPacManStorageManager {
	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	Vector<Info> bufferInfo;
	
	private class Info
	{
		private int numLives;
		private int numPills;
		private Collection<RetrievalResult> eval;

		public Info(int numLives, int numPills, Collection<RetrievalResult> eval) {
			this.numLives = numLives;
			this.numPills = numPills;
			this.eval = eval;
		}
	}
	
	//Constante de tiempo
	private final static int TIME_WINDOW = 3;
	
	//Constante de recuerdo // 
	private static final Double UMBRAL_CONSERVAR = 0.87;
	private static final Double UMBRAL_CASO_SUFICIENTE_SIMILAR = 0.90;
	private static final Integer NUM_CASOS_NO_CONSERVAR = 3;
	private static final Integer minMediocre = -35;
	private static final Integer maxMediocre = 35;
	
	//Constantes de revision //	
	private static final Integer UMBRAL_DISTANCIA_DEFENSA = 45;
	private final static Integer SCORE_FANTASMA_COMIDO = 200;
	//Recompensas
	private final static Integer RECOMPENSA_FANTASMA_DEBIL_CERCA = 25;
	private final static Double  RECOMPENSA_PILL_COMIDA = 3.05;
	private static final Integer RECOMPENSA_ALEJADO_FANTASMA = 60;
	//Penalizaciones
	private static final Integer PENALIZANDO_ALEJADO_FANTASMA = -30;
	private final static Integer PENALIZACION_PPILL = -50;
	private final static Integer PENALIZACION_MUERTE = -75;
	
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
		this.bufferInfo.add(new Info(game.getPacmanNumberOfLivesRemaining(), game.getNumberOfActivePowerPills(), eval));
		
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
		
		//Final pill
		int value = 0;
	
		//Con ppill
		if(infoCase.numPills > 0) {
			
			//Fantasmas comidos -> Recompensamos comer fantasmas
			if (finalScore / SCORE_FANTASMA_COMIDO < 1){
				//Empiricamente el numero de fantasmas
				int num_fantasmas = 0; int score = finalScore;
				for(int i = SCORE_FANTASMA_COMIDO; score > 0; i *= 2) {
					score -= i;
					if(score > 0) {
						num_fantasmas++;
					}
				}
				
				switch(num_fantasmas) {
				case 0: 
					value += 0;
					break;
				case 1:
					value += 20;
					break;
				case 2:
					value += 40;
					break;
				case 3:
					value += 70;
					break;
				case 4:
					value += 100;
					break;
				}
			}
			
			// Supervivencia 
			// Hacemos la mediana de la distancia de los fantasmas
			// Recompensamos a Pacman si esta mas cerca de la PPill o si esta bastante lejos del fantasma
			List<Integer> distGhostNotEdible = new ArrayList<>();
			for(GHOST g : GHOST.values()) {
				if(game.isGhostEdible(g)) {
					continue;
				}
				
				distGhostNotEdible.add(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex()));
			}
			
			
			
			if(!distGhostNotEdible.isEmpty()) {
				Collections.sort(distGhostNotEdible);
				if(distGhostNotEdible.get(0) > description.getNearestPPill() || distGhostNotEdible.get(0) >= UMBRAL_DISTANCIA_DEFENSA) {
					value += RECOMPENSA_ALEJADO_FANTASMA;
				}
				else {
					value += PENALIZANDO_ALEJADO_FANTASMA;
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
			if(game.getNumberOfActivePowerPills() < infoCase.numPills && num_ghost_reachable == 0) {
				value += PENALIZACION_PPILL;
			}
		}
		//Sin ppill
		else {
			
			// Supervivencia 
			// Hacemos la mediana de la distancia de los fantasmas
			// Recompensamos a Pacman si esta mas cerca de la PPill o si esta bastante lejos del fantasma
			List<Integer> distGhostNotEdible = new ArrayList<>();
			for(GHOST g : GHOST.values()) {
				if(game.isGhostEdible(g)) {
					continue;
				}
				
				distGhostNotEdible.add(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex()));
			}
			
			if(!distGhostNotEdible.isEmpty()) {
				Collections.sort(distGhostNotEdible);
				if(distGhostNotEdible.get(0) > description.getNearestPPill() || distGhostNotEdible.get(0) >= UMBRAL_DISTANCIA_DEFENSA) {
					value += RECOMPENSA_ALEJADO_FANTASMA;
				}
				else {
					value += PENALIZANDO_ALEJADO_FANTASMA;
				}
			}
			
			//Consideramos las pills comidas 
			value += Math.round(RECOMPENSA_PILL_COMIDA * finalScore);
		}
		
		//Penalizamos la muerte quitando parte del resultado
		if(game.getPacmanNumberOfLivesRemaining() < infoCase.numLives) {
			value += PENALIZACION_MUERTE;
		}
		
		MsPacManResult result = (MsPacManResult)bCase.getResult();
        result.setScore(value);	
	}
	
	private void retainCase(CBRCase bCase, Collection<RetrievalResult> eval)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		
		//Options:		
		//If there is no other cases to compare it to, then there needs to be added
		if(Objects.isNull(eval)) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
			return;
		}
		
		//Get case resolution
		//Result es score, solution es accion
		MsPacManDescription bCaseDesc = (MsPacManDescription) bCase.getDescription();
		MsPacManSolution bCaseSolution = (MsPacManSolution) bCase.getSolution();
		MsPacManResult bCaseResult = (MsPacManResult) bCase.getResult();
		
		
		//VARIABLES PRUEBA VALORES MEDIOCRES
		double mediocreScore = 100;		
		RetrievalResult mediocreRR = null;
		
		//Obtenemos los casos muy similares
		Double maxSimilarity = Double.MIN_VALUE; Integer countCasesAbove = 0;
		//Obtenemos el caso mas parecido 
		RetrievalResult mostSimilar = null; Double maxSimCase = Double.MIN_VALUE;
		
		for(RetrievalResult cbrCase : eval) {
			MsPacManSolution cbrSolution =(MsPacManSolution) cbrCase.get_case().getSolution();
			MsPacManResult cbrResult = (MsPacManResult) cbrCase.get_case().getResult();

			// get biggest similarity
			if(maxSimilarity < cbrCase.getEval()) {
				maxSimilarity = cbrCase.getEval();
			}
			
			// count if similar enough
			if(UMBRAL_CASO_SUFICIENTE_SIMILAR <= cbrCase.getEval()) {
				countCasesAbove++;
			}
			
			// update most similar
			if(bCaseSolution.getAction() == cbrSolution.getAction() && maxSimCase < cbrCase.getEval()) {
				maxSimCase = cbrCase.getEval();
				mostSimilar = cbrCase;
			}
			
			// get mediocre
			if (Math.abs(cbrResult.getScore()) < mediocreScore) {
				mediocreScore = cbrResult.getScore();
				mediocreRR = cbrCase;
			}
		}

		// 1. Not store it
		// Varios valores muy similar --> Casos muy similares entre si
		if (countCasesAbove >= NUM_CASOS_NO_CONSERVAR) {
			return;
		}
		
		// 2. Store it
		// Si la mayor similitud es menor que nuestra constante, se añade directamente
		if(maxSimilarity < UMBRAL_CONSERVAR) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);			
			return;
		}
		
		
		//3. Replace bad RR
		if (mediocreRR != null && mediocreScore <= maxMediocre && mediocreScore >= minMediocre) {
			Collection<CBRCase> aux = new ArrayList<CBRCase>();
			aux.add(mediocreRR.get_case());
			caseBase.forgetCases(aux);
			
			//Add only if outside of the ratio stablished
			if(!(bCaseResult.getScore() <= maxMediocre && bCaseResult.getScore() >= minMediocre)) {				
				StoreCasesMethod.storeCase(caseBase, bCase);
			}
		}
		
		//4. Do a mix of similar cases --> Only if similarity is above 0.85
		if(Objects.isNull(mostSimilar)) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);			
		}
		// New "Frankenstein" case
		else {
			//Forget previous case
			Collection<CBRCase> aux = new ArrayList<CBRCase>();
			aux.add(mostSimilar.get_case());
			caseBase.forgetCases(aux);
			
			
			//TODO: conservar el numero de veces que esto sucede --> (n/10 + 0.8) * resultMostSimilar + (0.2 - n/10) * bCaseResult
			MsPacManResult mostSimilarResult =(MsPacManResult) mostSimilar.get_case().getResult();
			MsPacManDescription mostSimilarDesc = (MsPacManDescription) mostSimilar.get_case().getDescription();
			Integer newScore = (int) Math.round(0.8 * mostSimilarResult.getScore() + 0.2 * bCaseResult.getScore());
			
			// Media de las distancias y de los tiempos de comestible
			double new_case;
			double most_similar_case;
			for(int i = 0; i < 4; i++){
				//Dist ghost to pacman
				new_case = bCaseDesc.getGhostToPacman().getElement(i);
				most_similar_case = mostSimilarDesc.getGhostToPacman().getElement(i);
				mostSimilarDesc.getGhostToPacman().setElement(i,  (double) Math.round(0.8 * new_case + 0.2 * most_similar_case));
				
				//Dist pacman to ghost
				new_case = bCaseDesc.getPacmanToGhost().getElement(i);
				most_similar_case = mostSimilarDesc.getPacmanToGhost().getElement(i);
				mostSimilarDesc.getPacmanToGhost().setElement(i,  (double) Math.round(0.8 * new_case + 0.2 * most_similar_case));
				
				//Edible values
				new_case = bCaseDesc.getGhostEdibleTime().getElement(i);
				most_similar_case = mostSimilarDesc.getGhostEdibleTime().getElement(i);
				mostSimilarDesc.getGhostEdibleTime().setElement(i,  (double) Math.round(0.8 * new_case + 0.2 * most_similar_case));
			}
			
			new_case = bCaseDesc.getNearestPPill();
			most_similar_case = mostSimilarDesc.getNearestPPill();
			mostSimilarDesc.setNearestPPill( (int)Math.round(0.8 * new_case + 0.2 * most_similar_case));
			
			new_case = bCaseDesc.getNearestPill();
			most_similar_case = mostSimilarDesc.getNearestPill();
			mostSimilarDesc.setNearestPill( (int)Math.round(0.8 * new_case + 0.2 * most_similar_case));
			
			// Nppills, ppillmascercana, pillmascercana y Movimientos se mantienen 
					
			bCaseResult.setScore(newScore);
			StoreCasesMethod.storeCase(this.caseBase, mostSimilar.get_case());
		}
		
	}

	public void close() {
		//TODO: ¿Cuando salvo la información, lo hago con el método habitual o usamos otro distinto?
		//TODO: ¿Y con retain, hacemos tambien lo mismo?
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
