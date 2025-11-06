package es.ucm.fdi.ici.c2526.practica3.grupoYY;

import java.io.File;
import java.util.HashMap;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.MsPacManInput;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.EatPpillAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.GoToNearestPillAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.GoToPpillAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.GreedyPointsAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.HideAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.HideFromOneAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.HuntAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.MorePillsSuicidaAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.MoveAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.PPillSuicidaAction;
import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.RandomAction;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class msPacMan  extends PacmanController  {
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2526"+File.separator+"practica3"+File.separator+"grupoYY"+File.separator;
	HashMap<String,RulesAction> map;
	RuleEngine MsPacManRuleEngine;
	
	
	public msPacMan() {
		setName("RandomJuegaMejor");
		setTeam("Team 06");
		
		map = new HashMap<String,RulesAction>();
		//Fill Actions
		RulesAction act1 = new EatPpillAction(); 
		map.put(act1.getActionId(), act1);
		RulesAction act2 = new GoToNearestPillAction();
		map.put(act2.getActionId(), act2);
		RulesAction act3 = new GoToPpillAction();
		map.put(act3.getActionId(), act3);
		RulesAction act4 = new GreedyPointsAction();
		map.put(act4.getActionId(), act4);
		RulesAction act5 = new HideAction();
		map.put(act5.getActionId(), act5);
		RulesAction act6 = new HuntAction();
		map.put(act6.getActionId(), act6);
		RulesAction act7 = new HideFromOneAction();
		map.put(act7.getActionId(), act7);
		RulesAction act8 = new MorePillsSuicidaAction();
		map.put(act8.getActionId(), act8);
		RulesAction act9 = new MoveAction();
		map.put(act9.getActionId(), act9);
		RulesAction act10 = new PPillSuicidaAction();
		map.put(act10.getActionId(), act10);
		RulesAction act11 = new RandomAction();
		map.put(act11.getActionId(), act11);

		String rulesFile = String.format("%s%srules.clp", RULES_PATH, "MsPacMan");
		MsPacManRuleEngine = new RuleEngine("MsPacMan",rulesFile,map);	
	}
	
	@Override
    public MOVE getMove(Game game, long timeDue) {
    	RulesInput input = new MsPacManInput(game);
    	MsPacManRuleEngine.reset();
    	MsPacManRuleEngine.assertFacts(input.getFacts());
    	return MsPacManRuleEngine.run(game);
    }

}
