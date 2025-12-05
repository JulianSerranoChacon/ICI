package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.util.ArrayList;
import java.util.Collection;
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
	
	//Constante de recuerdo
	private static final double UMBRAL_CONSERVAR = 0.75;
	private static final int UMBRAL_CASOS_NO_CONSERVAR = 5;
	private static final double UMBRAL_CASO_SUFICIENTE_SIMILAR = 0.85;
	
	//Constantes de revision	
	private final static Integer SCORE_FANTASMA_COMIDO = 200;
	private final static Integer RECOMPENSA_FANTASMA_DEBIL_CERCA = 30;
	private final static Integer RECOMPENSA_FANTASMAS = 30;
	private final static Double RECOMPENSA_PILL_COMIDA = 3.05;
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
				
				value += num_fantasmas * RECOMPENSA_FANTASMAS;
			}
			
			// TODO: Supervivencia --> alomejor aquí cuenta menos que abajo + si muere maybe no sumar esto
			for(GHOST g : GHOST.values()) {
				
			}
			
			// Caza 
			int num_ghost_reachable = 0;
			for(GHOST g : GHOST.values()) {
				if(ghostReachable(game, g)) {
					num_ghost_reachable++;
				}
			}
			value += RECOMPENSA_FANTASMA_DEBIL_CERCA * num_ghost_reachable;
			
			//Penalizamos el uso inapropiado de la Power Pills
			if(game.getNumberOfActivePowerPills() < infoCase.numPills && num_ghost_reachable == 0) {
				finalScore -= PENALIZACION_PPILL;
			}
		}
		//Sin ppill
		else {
			
			// TODO: Supervivencia --> alomejor aquí cuenta menos que abajo + si muere maybe no sumar esto
			for(GHOST g : GHOST.values()) {
				
			}
			
			//Consideramos las pills comidas 
			value += Math.round(RECOMPENSA_PILL_COMIDA * finalScore);
		}
		
		//Penalizamos la muerte quitando parte del resultado
		if(game.getPacmanNumberOfLivesRemaining() < infoCase.numLives) {
			value -= PENALIZACION_MUERTE;
		}
		
		MsPacManResult result = (MsPacManResult)bCase.getResult();
        result.setScore(value);	
	}
	
	private void retainCase(CBRCase bCase, Collection<RetrievalResult> eval)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		
		//TODO: Si hay similares mezclar, sino añadir si se cumple los valores de similitud y puntuaje (puntuaje a revisar)

		//Options:		
		//If there is no other cases to compare it to, then there needs to be added
		if(Objects.isNull(eval)) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
			return;
		}
		
		//Get case resolution
		//Result es score, solution es accion
		MsPacManSolution bCaseSolution = (MsPacManSolution) bCase.getSolution();
		MsPacManResult bCaseResult = (MsPacManResult) bCase.getResult();
		
		//Obtenemos los casos muy similares
		Double maxSimilarity = Double.MIN_VALUE; Integer countCasesAbove = 0;
		//Obtenemos el caso mas parecido 
		RetrievalResult mostSimilar = null; Double maxSimCase = Double.MIN_VALUE;
		
		for(RetrievalResult cbrCase : eval) {
			MsPacManSolution cbrSolution =(MsPacManSolution) cbrCase.get_case().getSolution();
			
			if(maxSimilarity < cbrCase.getEval()) {
				maxSimilarity = cbrCase.getEval();
			}
			
			if(UMBRAL_CASO_SUFICIENTE_SIMILAR <= cbrCase.getEval()) {
				countCasesAbove++;
			}
			
			if(bCaseSolution.getAction() == cbrSolution.getAction() && maxSimCase < cbrCase.getEval()) {
				maxSimCase = cbrCase.getEval();
				mostSimilar = cbrCase;
			}
		}
		
		// 1. Not store it
		// Varios valores muy similar --> Casos muy similares entre si
		if (countCasesAbove >= UMBRAL_CASOS_NO_CONSERVAR) {
			return;
		}
		
		// 2. Store it
		// Si la mayor similitud es menor que nuestra constante, se añade directamente
		if(maxSimilarity < UMBRAL_CONSERVAR) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);			
			return;
		}
		
		//3. Do a mix of similar cases
		if(Objects.isNull(mostSimilar)) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);			
		}
		else {
			//Forget previous case
			Collection<CBRCase> aux = new ArrayList<CBRCase>();
			aux.add(mostSimilar.get_case());
			caseBase.forgetCases(aux);
			
			//New "Frankenstein" case
			//TODO: conservar el numero de veces que esto sucede --> (n/10 + 0.8) * resultMostSimilar + (0.2 - n/10) * bCaseResult
			MsPacManResult mostSimilarResult =(MsPacManResult) mostSimilar.get_case().getResult();
			Integer newScore = (int) Math.round(0.8 * mostSimilarResult.getScore() + 0.2 * bCaseResult.getScore());
			
			//TODO: Cambiar input tb si lo veis necesario
			
			bCaseResult.setScore(newScore);
			StoreCasesMethod.storeCase(this.caseBase, bCase);
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
