package es.ucm.fdi.ici.c2526.practica2.grupoYY;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.ChaseAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.EatPpillAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.GoToNearestPillAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.GoToPpillAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.GreedyPointsAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.HideAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.HideFromOneAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.MorePillsSuicidaAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.MoveAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.PPillSuicidaAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.actions.RandomAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.EdibleGhostsNearTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.GoToNearestPillToGreedyPathTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.MenacedToEatPpillTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.MoveToHideTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.NearDangerousGhostsTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.NoCandidateMovesTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.MoveToGreedyTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.OnlyOneMovePossibleTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.PPillSuicidaToPointsSuicidaTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.PointsSuicidaToRandomTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.RunFromAllToMenacedTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.mspacman.transitions.RunFromAllToRunFromOneTrasition;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * The Class NearestPillPacMan.
 */
public class MsPacMan extends PacmanController {
	PacmanInfo pi;
	FSM fsm;
	
	public MsPacMan() {
		setName("DumbMsPacMan");
		
		pi = new PacmanInfo();
    	fsm = new FSM("MsPacMan");
    	
    	GraphFSMObserver observer = new GraphFSMObserver(fsm.toString());
    	fsm.addObserver(observer);
    	
    	//Compound states
    	// SUICIDA STATE //
    	FSM cfsSuicida = new FSM("Suicida");
    	GraphFSMObserver c1observer = new GraphFSMObserver(cfsSuicida.toString());
    	cfsSuicida.addObserver(c1observer);
    	
    	//State declaration
    	SimpleState suicida1 = new SimpleState("PPill suicida", new PPillSuicidaAction(pi));
    	SimpleState suicida2 = new SimpleState("Pills suicida", new MorePillsSuicidaAction(pi));
    	SimpleState suicida3 = new SimpleState("cstate2", new RandomAction());
    	
    	//Transition declaration
    	Transition sTrans1 = new PPillSuicidaToPointsSuicidaTransition();
    	Transition sTrans2 = new PointsSuicidaToRandomTransition();
    	
    	cfsSuicida.add(suicida1, sTrans1, suicida2);
    	cfsSuicida.add(suicida2, sTrans2, suicida3);
    	cfsSuicida.ready(suicida1);
    	CompoundState suicida = new CompoundState("Suicida", cfsSuicida);
    	// HIDE STATE //
    	FSM cfsHide = new FSM("Hide");
    	GraphFSMObserver c2observer = new GraphFSMObserver(cfsHide.toString());
    	cfsHide.addObserver(c2observer);
    	
    	//State declaration
    	SimpleState hide1 = new SimpleState("Run away from everyone", new HideAction(pi));
    	SimpleState hide2 = new SimpleState("Run away from one", new HideFromOneAction(pi));
    	SimpleState hide3 = new SimpleState("Go to PPill", new GoToPpillAction(pi));
    	SimpleState hide4 = new SimpleState("Eat PPill", new EatPpillAction(pi));
    	
    	//Transition declaration
    	Transition hTrans1 = new RunFromAllToRunFromOneTrasition();
    	Transition hTrans2 = new RunFromAllToMenacedTransition();
    	Transition hTrans3 = new MenacedToEatPpillTransition();
    	
    	cfsHide.add(hide1, hTrans1, hide2);
    	cfsHide.add(hide1, hTrans2, hide3);
    	cfsHide.add(hide3, hTrans3, hide4);
    	cfsHide.ready(hide1);
    	CompoundState hide = new CompoundState("Hide", cfsHide);
    	// GREEDY STATE //
    	FSM cfsGreedy = new FSM("Greedy");
    	GraphFSMObserver c3observer = new GraphFSMObserver(cfsGreedy.toString());
    	cfsGreedy.addObserver(c3observer);
    	
    	//State declaration
    	SimpleState greedy1 = new SimpleState("Go to near pill", new GoToNearestPillAction(pi));
    	SimpleState greedy2 = new SimpleState("More points", new GreedyPointsAction(pi));
    	
    	//Transition declaration
    	Transition gTrans1 = new GoToNearestPillToGreedyPathTransition();
    	
    	cfsGreedy.add(greedy1, gTrans1, greedy2);
    	cfsGreedy.ready(greedy1);
    	CompoundState greedy = new CompoundState("Greedy", cfsGreedy);
    	
    	// FSM //
    	SimpleState move = new SimpleState("Move", new MoveAction(pi));
    	SimpleState hunt = new SimpleState("Hunt", new ChaseAction(pi));
    	
		Transition tran14 = new EdibleGhostsNearTransition();
		fsm.add(move, tran14, hunt);
		
    	Transition tran1 = new NoCandidateMovesTransition();
    	Transition tran2 = new OnlyOneMovePossibleTransition();
    	fsm.add(move, tran1, suicida);
    	fsm.add(suicida, tran2, move);
    	
    	Transition tran3 = new MoveToHideTransition();
    	Transition tran4 = new OnlyOneMovePossibleTransition();
    	fsm.add(move, tran3, hide);
    	fsm.add(hide, tran4, move);
    	
    	Transition tran5 = new MoveToGreedyTransition();
    	Transition tran6 = new OnlyOneMovePossibleTransition();
    	fsm.add(move, tran5, greedy);
    	fsm.add(greedy, tran6, move);
    	
    	Transition tran7 = new NoCandidateMovesTransition();
    	fsm.add(hide, tran7, suicida);
    	
    	Transition tran8 = new MoveToGreedyTransition();
    	Transition tran9 = new NearDangerousGhostsTransition();
    	fsm.add(hide, tran8, greedy);
    	fsm.add(greedy, tran9, hide);
    	
    	Transition tran10 = new NoCandidateMovesTransition();
    	fsm.add(hunt, tran10, suicida);
    	
    	Transition tran11 = new EdibleGhostsNearTransition();
    	Transition tran12 = new NearDangerousGhostsTransition();
    	fsm.add(hide, tran11, hunt);
    	fsm.add(hunt, tran12, hide);
    	
    	Transition tran13 = new MoveToGreedyTransition();
    	fsm.add(hunt, tran13, greedy);

    	
    	fsm.ready(greedy);
    	
    	
    	JFrame frame = new JFrame();
    	JPanel main = new JPanel();
    	main.setLayout(new BorderLayout());
    	main.add(observer.getAsPanel(true, null), BorderLayout.CENTER);
    	main.add(c1observer.getAsPanel(true, null), BorderLayout.SOUTH);
    	main.add(c2observer.getAsPanel(true, null), BorderLayout.EAST);
    	main.add(c3observer.getAsPanel(true, null), BorderLayout.WEST);
    	frame.getContentPane().add(main);
    	frame.pack();
    	frame.setVisible(true);
    	
	}
	
	
	public void preCompute(String opponent) {
    		fsm.reset();
    }
	
	
	
    /* (non-Javadoc)
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {
    	Input in = new MsPacManInput(game, pi); 
    	return fsm.run(in);
    }
    
    
}