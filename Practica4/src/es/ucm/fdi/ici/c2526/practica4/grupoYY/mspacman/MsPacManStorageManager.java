package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
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

		public Info(int numLives, int numPills) {
			this.numLives = numLives;
			this.numPills = numPills;
		}
	}
	
	private final static Integer PENALIZACION_MUERTE = 500;
	private final static Integer PENALIZACION_PPILL = 500;
	private final static Integer RECOMPENSA_FANTASMAS = 500;

	private final static Integer SCORE_FANTASMA_COMIDO = 200;

	private final static int TIME_WINDOW = 3;
	
	public MsPacManStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBase(CBRCaseBase caseBase)
	{
		this.caseBase = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase)
	{			
		this.buffer.add(newCase);
		//We can keep extra information of the game that would be unnecesary in a case.
		this.bufferInfo.add(new Info(game.getPacmanNumberOfLivesRemaining(), game.getNumberOfActivePowerPills()));
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		Info infoCase = this.bufferInfo.remove(0);
		reviseCase(bCase, infoCase);
		retainCase(bCase);
		
	}
	
	private void reviseCase(CBRCase bCase, Info infoCase) {
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		
		//Base score
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int resultValue = currentScore - oldScore;
		
		//TODO: Alter points if dead, ate ghosts...
		
		//Penalizamos el uso inapropiado de la Power Pills
		if(game.getNumberOfActivePowerPills() < infoCase.numPills && resultValue / SCORE_FANTASMA_COMIDO < 1) {
			resultValue -= PENALIZACION_PPILL;
		}
		//Recompensamos comer fantasmas
		else {
			resultValue += (resultValue / SCORE_FANTASMA_COMIDO) * RECOMPENSA_FANTASMAS;
		}
			
		//Penalizamos la muerte quitando parte del resultado
		if(game.getPacmanNumberOfLivesRemaining() < infoCase.numLives) {
			resultValue -= PENALIZACION_MUERTE;
		}
		
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		result.setScore(resultValue);	
	}
	
	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		
		//TODO: Si hay similares mezclar, sino añadir si se cumple los valores de similitud y puntuaje (puntuaje a revisar)
		//TODO: Traer los K más similares y guardarlos en un buffer desde cbr.cycle()
		
		StoreCasesMethod.storeCase(this.caseBase, bCase);
	}

	public void close() {
		//TODO: ¿Cuando salvo la información, lo hago con el método habitual o usamos otro distinto?
		for(int i = 0; i < buffer.size(); i++)
		{
			reviseCase(buffer.get(i), bufferInfo.get(i));
			retainCase(buffer.get(i));
		}
		this.buffer.removeAllElements();
		this.bufferInfo.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
