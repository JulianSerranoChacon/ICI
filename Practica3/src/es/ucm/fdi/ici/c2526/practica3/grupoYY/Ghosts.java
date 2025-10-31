package es.ucm.fdi.ici.c2526.practica3.grupoYY;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.Hunter1Action;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.Hunter2Action;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.JailerAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.OrbitateAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.ProtectEdibleGhostAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.RandomAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.RunOptimalAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.RunToEscuderoAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.ghosts.actions.StartRunningAction;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts  extends GhostController  {
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2526"+File.separator+"practica3"+File.separator+"grupoYY"+File.separator;
	HashMap<String,RulesAction> map;
	
	EnumMap<GHOST,RuleEngine> ghostRuleEngines;
	
	
	public Ghosts() {
		setName("Ghosts XX");
		setTeam("Team XX");
		
		map = new HashMap<String,RulesAction>();
		//Fill Actions
		// Then, for each action type:
		for (GHOST ghost : GHOST.values()) {
		    map.put(ghost.name() + "chases", new Hunter1Action(ghost));
		    map.put(ghost.name() + "advancedChases", new Hunter2Action(ghost));
		    map.put(ghost.name() + "jails", new JailerAction(ghost));
		    map.put(ghost.name() + "orbits", new OrbitateAction(ghost));
		    map.put(ghost.name() + "protects", new ProtectEdibleGhostAction(ghost));
		    map.put(ghost.name() + "randomMoves", new RandomAction(ghost));
		    map.put(ghost.name() + "runsOptimally", new RunOptimalAction(ghost));
		    map.put(ghost.name() + "runsToEscudero", new RunToEscuderoAction(ghost));
		    map.put(ghost.name() + "startsRunning", new StartRunningAction(ghost));
		}

		
		ghostRuleEngines = new EnumMap<GHOST,RuleEngine>(GHOST.class);
		for(GHOST ghost: GHOST.values())
		{
			String rulesFile = String.format("%s%srules.clp", RULES_PATH, ghost.name().toLowerCase());
			RuleEngine engine  = new RuleEngine(ghost.name(),rulesFile, map);
			ghostRuleEngines.put(ghost, engine);
			
			//add observer to every Ghost
			//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(ghost.name(), true);
			//engine.addObserver(observer);
		}
		
		//add observer only to BLINKY
		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(GHOST.BLINKY.name(), true);
		ghostRuleEngines.get(GHOST.BLINKY).addObserver(observer);
		
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		//Process input
		RulesInput input = new GhostsInput(game);
		//load facts
		//reset the rule engines
		for(RuleEngine engine: ghostRuleEngines.values()) {
			engine.reset();
			engine.assertFacts(input.getFacts());
		}
		
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);		
		for(GHOST ghost: GHOST.values())
		{
			RuleEngine engine = ghostRuleEngines.get(ghost);
			MOVE move = engine.run(game);
			result.put(ghost, move);
		}
		
		return result;
	}

}
