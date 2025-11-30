package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine.Average;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine.CustomPlainTextConnector;
import pacman.game.Constants.MOVE;

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CachedLinearCaseBase caseBase;
	NNConfig simConfig;
	
	
	final static String TEAM = "grupoYY";  //Cuidado!! poner el grupo aquí
	
	private final static Double UMBRAL_SIMILITUD = 0.7; // umbral de similitud propuesto para empezar a considerar casos similares
	private final static Double UMBRAL_ALEATORIO = 0.5;
	private final static Double UMBRAL_CONTRADECIR = 0.5;
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2425/practica5/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator;

	
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
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("score",MsPacManDescription.class), new Interval(15000));
		simConfig.addMapping(new Attribute("time",MsPacManDescription.class), new Interval(4000));
		simConfig.addMapping(new Attribute("nearestPPill",MsPacManDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("nearestGhost",MsPacManDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("edibleGhost",MsPacManDescription.class), new Equal());
		
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		Collection<RetrievalResult> eval = null;
		
		if(caseBase.getCases().isEmpty()) {
			this.action = MOVE.NEUTRAL;
		}
		else {
			//Compute retrieve
			eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			//Compute reuse
			this.action = reuse(eval);
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase, eval);
		
	}

	private MOVE reuse(Collection<RetrievalResult> eval)
	{
		// kNNs with majority voting
		Collection<RetrievalResult> cases = SelectCases.selectTopKRR(eval, 5);
		//Truncar la lista para conservar casos que cumplen con el umbral
		Collection<RetrievalResult> list = new ArrayList<>();
		Map<MOVE, Double> dirToScore = new HashMap<>();
		MOVE bestMove = MOVE.NEUTRAL; Double bestScore = Double.MIN_VALUE;
		List<MOVE> isNotCandidate = new ArrayList<>();

		for(MOVE m : MOVE.values()) {
				isNotCandidate.add(m);
		}

		for(RetrievalResult cbrCase : cases) {
			if(cbrCase.getEval() < UMBRAL_SIMILITUD) {
				list.add(cbrCase);

				MsPacManResult scoreCase = (MsPacManResult) cbrCase.get_case().getResult(); 
				MsPacManSolution moveCase = (MsPacManSolution) cbrCase.get_case().getSolution(); 
				double weight = scoreCase.getScore() * cbrCase.getEval();

				if(weight > dirToScore.get(moveCase.getAction())) {
					dirToScore.replace(moveCase.getAction(), weight);
					if(bestScore < weight) {
						bestScore = weight;
						bestMove = moveCase.getAction();
					}
				}
				
				isNotCandidate.remove(moveCase.getAction());
			
			}
		}
		
		//Si tamaño = 0 --> Aleatorio
		if(list.isEmpty()) {
			MOVE[] availableMoves = MOVE.values(); Random rnd = new Random();
			return availableMoves[rnd.nextInt(availableMoves.length - 1)];
		}

		//TODO: Do the majority voting between cases
		//TODO: Implement when we want random --> because not similar or not enough.
		//TODO: No se si queremos que random sea una de las posibilidades a hacer, por eso el -1 en el valor aleatorio
		//Si tamaño > 0 --> Opciones


		else {
			
			// 3. Escoger por descarte
			if(bestScore < UMBRAL_CONTRADECIR && isNotCandidate.size() > 0) {
				Random rnd = new Random();
	        	return isNotCandidate.get(rnd.nextInt(isNotCandidate.size() - 1));
			}
			// 2. Escoger aleatorio
			else if(bestScore < UMBRAL_ALEATORIO) {
				MOVE[] availableMoves = MOVE.values(); Random rnd = new Random();
	        	return availableMoves[rnd.nextInt(availableMoves.length - 1)];
			}
			// 1. Escoger por mayoría
			else if(bestMove != MOVE.NEUTRAL){
				return bestMove;
			}			
			
			MOVE[] availableMoves = MOVE.values(); Random rnd = new Random();
        	return availableMoves[rnd.nextInt(availableMoves.length - 1)];
		}		
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

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();
	}

}
