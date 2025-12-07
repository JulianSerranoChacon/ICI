package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine.Average;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine.EqualNumLocalSimilarityFuntion;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine.IntervalVectorCBR;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine.MoveLocalSimilarityFuntion;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine.vectorCBRSimilarity;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CachedLinearCaseBase caseBase;
	NNConfig simConfig;
	
	
	final static String TEAM = "grupoYY";  //Cuidado!! poner el grupo aquí
	
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2526/practica4/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator;
	
	private final static Double UMBRAL_SIMILITUD = 0.7; // umbral de similitud propuesto para empezar a considerar casos similares
	private static final Double UMBRAL_SCORE_MINIMO = 20.00;

	
	public MsPacManCBRengine(MsPacManStorageManager storageManager)
	{
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new CachedLinearCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");
		
		this.storageManager.setCaseBase(caseBase);
		Attribute aux;
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());

		aux = new Attribute("nearestPPill",MsPacManDescription.class);
		simConfig.addMapping(aux, new Interval(650));
		simConfig.setWeight(aux,0.05);
		
		aux = new Attribute("nearestPill",MsPacManDescription.class);
		simConfig.addMapping(aux, new Interval(650));
		simConfig.setWeight(aux,0.05);
		
		aux = new Attribute("ghostToPacman",MsPacManDescription.class);
		simConfig.addMapping(aux , new IntervalVectorCBR(650)); 
		simConfig.setWeight(aux,0.05);
		
		aux = new Attribute("pacmanToGhost",MsPacManDescription.class);
		simConfig.addMapping(aux,  new IntervalVectorCBR(650)); 
		simConfig.setWeight(aux,0.05);
		
		aux = new Attribute("ghostEdibleTime",MsPacManDescription.class);
		simConfig.addMapping(aux,  new IntervalVectorCBR(Constants.EDIBLE_TIME)); 
		simConfig.setWeight(aux,0.2);
		
		aux = new Attribute("pacmanNode",MsPacManDescription.class);
		simConfig.addMapping(aux, new Interval(500));
		simConfig.setWeight(aux,0.4);
		
		aux = new Attribute("ghostLastMoves",MsPacManDescription.class);
		simConfig.addMapping(aux, new vectorCBRSimilarity()); 
		simConfig.setWeight(aux,0.2);
		
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	
	//Get the CSV where this new case will be part 
	public int getCaseList(CBRQuery query) {
		MsPacManDescription msDescription = (MsPacManDescription)query.getDescription();
		int index = 0;
		String lastPacManMove = msDescription.getPacmanLastMove();
		
		//Follow the representaiton of the CachedLinearCaseBase (0,1,2,3 if PPILS) (4,5,6,7 if NOTPPILS) --> (Up,Down,Left,Right) of the lastMove
		if(msDescription.getNumPPills() != 0) { //If ppils > 0
			switch(lastPacManMove) { 
			case("UP"):
				index = 0;
				break;
			case("DOWN"):
				index = 1;
				break;
			case("LEFT"):
				index = 2;
				break;
			case("RIGHT"):
				index = 3;
				break;
			}
		}
		else {
			switch(lastPacManMove) {
			case("UP"):
				index = 4;
				break;
			case("DOWN"):
				index = 5;
				break;
			case("LEFT"):
				index = 6;
				break;
			case("RIGHT"):
				index = 7;
				break;
			}
		}
		return index;
	}
	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		
		caseBase.setActListIndex(getCaseList(query));
		Collection<RetrievalResult> eval = null;
		Collection<RetrievalResult> cases = null;
		
		if(caseBase.getCases().isEmpty()) {
			Random rnd = new Random();
			MsPacManDescription currCase = (MsPacManDescription) query.getDescription();
			MOVE lastMove = MOVE.valueOf(currCase.getPacmanLastMove());
			
			MOVE finalMove;
			do {
				finalMove = MOVE.values()[rnd.nextInt(4)];
			} while (lastMove.opposite() == finalMove);
			
			this.action =  finalMove;
		}
		else {
			//Compute retrieve
			
			eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			// kNNs with majority voting
			cases = SelectCases.selectTopKRR(eval, 8);
			//Compute reuse
			this.action = reuse(cases, query);
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase, cases);
		
	}

	private MOVE reuse(Collection<RetrievalResult> cases, CBRQuery query) {
		//Generate a random number generator for future randomness
		Random rnd = new Random();

		//Obtain pacman last move (we do not want to do an illegal move)
		MsPacManDescription currCase = (MsPacManDescription) query.getDescription();
		MOVE lastMove = MOVE.valueOf(currCase.getPacmanLastMove());
		
		Map<MOVE, double[]> dirToScore = new HashMap<>();
		MOVE bestMove = MOVE.NEUTRAL; Double bestScore = Double.MIN_VALUE;
		
		for(RetrievalResult cbrCase : cases) {
			//Truncar la lista con 0,7

			if(cbrCase.getEval() > UMBRAL_SIMILITUD) {
			
				MsPacManResult scoreCase = (MsPacManResult) cbrCase.get_case().getResult(); 
				MsPacManSolution moveCase = (MsPacManSolution) cbrCase.get_case().getSolution(); 
				double weight = scoreCase.getScore() * cbrCase.getEval() * cbrCase.getEval();

				if (!dirToScore.containsKey(moveCase.getAction())) {
					dirToScore.put(moveCase.getAction(), new double[] {weight, 1});
				} 
				else {
					dirToScore.replace(moveCase.getAction(), new double[] {dirToScore.get(moveCase.getAction())[0] + weight, dirToScore.get(moveCase.getAction())[1] + 1});
				}
			}
		}
		
		//Si tamaño = 0 --> Aleatorio
		if(dirToScore.isEmpty()) {
			// MOVE[] availableMoves = MOVE.values(); Random rnd = new Random(); We are not doing this because it may return the move we did before
			MOVE finalMove;
			do {
				finalMove = MOVE.values()[rnd.nextInt(4)];
			} while (lastMove.opposite() == finalMove);
			return finalMove;
		}
		
		for (Entry<MOVE, double[]> weight : dirToScore.entrySet()) {
			weight.getValue()[0] = weight.getValue()[0] / weight.getValue()[1];
			if (weight.getValue()[0] > bestScore) {
				bestScore = weight.getValue()[0];
				bestMove = weight.getKey();
			}
		}
		//Opciones: 
		
		//1.Caso aleatorio entre posibles contrarios
		if(bestScore < UMBRAL_SCORE_MINIMO) {
			MOVE finalMove;
			do {
				finalMove = MOVE.values()[rnd.nextInt(4)];
			} while (dirToScore.size()<3 && (dirToScore.containsKey(finalMove) || lastMove.opposite() == finalMove));
			return finalMove;
		}
		
		//2.Mejor caso
		return bestMove;
	}
	
	
	

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		MsPacManDescription newDescription = (MsPacManDescription) query.getDescription();
		MsPacManResult newResult = new MsPacManResult();
		MsPacManSolution newSolution = new MsPacManSolution();
		int newId = this.caseBase.getNextId();
		newId+= storageManager.getPendingCases();
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
		newSolution.setAction(this.action);
		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
		return newCase;
	}
	
	public MOVE getSolution() {
		return this.action;
	}

	public int caseBaseSize() {
		return caseBase.caseBaseSize();
	}
	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();
	}

}
