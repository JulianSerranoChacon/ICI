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
//import es.ucm.fdi.ici.c2526.practica3.grupoYY.MsPacMan.actions.RunAwayAction;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class msPacMan  extends PacmanController  {
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2526"+File.separator+"practica3"+File.separator+"grupoYY"+File.separator;
	HashMap<String,RulesAction> map;
	RuleEngine MsPacManRuleEngine;
	
	
	public msPacMan() {
		setName("PapiPacMan");
		setTeam("Team GH");
		
		map = new HashMap<String,RulesAction>();
		//Fill Actions
		RulesAction act = new EatPpillAction(); 
		map.put(act.getActionId(), act);
		act = new GoToNearestPillAction();
		map.put(act.getActionId(), act);
		act = new GoToPpillAction();
		map.put(act.getActionId(), act);
		act = new GreedyPointsAction();
		map.put(act.getActionId(), act);
		act = new HideAction();
		map.put(act.getActionId(), act);
		act = new HuntAction();
		map.put(act.getActionId(), act);
		act = new HideFromOneAction();
		map.put(act.getActionId(), act);
		act = new MorePillsSuicidaAction();
		map.put(act.getActionId(), act);
		act = new MoveAction();
		map.put(act.getActionId(), act);
		act = new PPillSuicidaAction();
		map.put(act.getActionId(), act);
		act = new RandomAction();
		map.put(act.getActionId(), act);
		//act = new RunAwayAction();
		map.put(act.getActionId(), act);

		String rulesFile = String.format("%s%srules.clp", RULES_PATH, "MsPacMan");
		MsPacManRuleEngine = new RuleEngine("MsPacMan",rulesFile,map);		
	}
	
	@Override
    public MOVE getMove(Game game, long timeDue) {
    	RulesInput input = new MsPacManInput(game);
    	input.parseInput();
    	MsPacManRuleEngine.reset();
    	MsPacManRuleEngine.assertFacts(input.getFacts());
    	MOVE move = MsPacManRuleEngine.run(game);
    	return move;
    }

}
