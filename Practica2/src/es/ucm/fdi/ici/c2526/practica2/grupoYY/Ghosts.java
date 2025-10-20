package es.ucm.fdi.ici.c2526.practica2.grupoYY;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.EnumMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

//General
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;

//Actions
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.Hunter1Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.Hunter2Action;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.JailerAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.RandomAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.RunOptimalAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.RunSubOptimalAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.OrbitateAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.ProtectEdibleGhostAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.RunToEscuderoAction;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.StartRunningAction;

//Transitions
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostHayFantasmaEnMiCamino;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsEdibleTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsHayEscudero;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsHePasadoAlEscudero;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsNoFantasmasCerca;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsNoHayFantasmasCercaEuclidiana;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPacmanEstaLejos;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPacmanEstaCerca;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPacManHaComidoPP;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPacManLejosParpadeo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPacManNoVaAPillarPP;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPasoAHunter1;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPasoAHunter2;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPasoAJailer;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPasoARandom;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsSoyComestible;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsVoyASerEscuderoQueProteje;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.PacManNearPPillTransition;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsSeHanComidoAMiEdible;
import es.ucm.fdi.ici.fsm.CompoundState;
//State Machine
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	EnumMap<GHOST,FSM> fsms;
	GhostInfo gi;
	public Ghosts()
	{
		setName("Fantasmikos");

		gi = new GhostInfo();
		fsms = new EnumMap<GHOST,FSM>(GHOST.class);
		for(GHOST ghost: GHOST.values()) {
			
			FSM fsm = new FSM(ghost.name());
			
			//fsm.addObserver(new ConsoleFSMObserver(ghost.name()));
			GraphFSMObserver graphObserver = new GraphFSMObserver(ghost.name());
			fsm.addObserver(graphObserver);
			
			//CompoundStates
			//CHASE STATE
			FSM cfsChase = new FSM(ghost.name() + "CHASE");
			GraphFSMObserver ChaseObserver = new GraphFSMObserver(cfsChase.toString());
			cfsChase.addObserver(ChaseObserver);
			
			//ChaseStates
			SimpleState hunter1 = new SimpleState(new Hunter1Action(ghost));
			SimpleState hunter2 = new SimpleState(new Hunter2Action(ghost,gi));
			SimpleState jailer = new SimpleState(new JailerAction(ghost,gi));
			SimpleState random = new SimpleState(new RandomAction(ghost,gi));
			SimpleState protectTheEdible = new SimpleState(new ProtectEdibleGhostAction(ghost,gi));
			
			//ChaseTransitions
			GhostsPasoAHunter1 hunter1Trans0 = new GhostsPasoAHunter1(ghost,gi,0);
			GhostsPasoAHunter2 hunter2Trans0 = new GhostsPasoAHunter2(ghost,gi,0);
			GhostsPasoAJailer jailerTrans0 = new GhostsPasoAJailer(ghost,gi,0);
			GhostsPasoARandom randomTrans0 = new GhostsPasoARandom(ghost,gi,0);
			GhostsVoyASerEscuderoQueProteje escuderoQueProteje0 = new GhostsVoyASerEscuderoQueProteje(ghost,gi,0);
			
			GhostsPasoAHunter1 hunter1Trans1 = new GhostsPasoAHunter1(ghost,gi,1);
			GhostsPasoAHunter2 hunter2Trans1 = new GhostsPasoAHunter2(ghost,gi,1);
			GhostsPasoAJailer jailerTrans1 = new GhostsPasoAJailer(ghost,gi,1);
			GhostsPasoARandom randomTrans1 = new GhostsPasoARandom(ghost,gi,1);
			GhostsVoyASerEscuderoQueProteje escuderoQueProteje1 = new GhostsVoyASerEscuderoQueProteje(ghost,gi,1);
			
			GhostsPasoAHunter1 hunter1Trans2 = new GhostsPasoAHunter1(ghost,gi,2);
			GhostsPasoAHunter2 hunter2Trans2 = new GhostsPasoAHunter2(ghost,gi,2);
			GhostsPasoAJailer jailerTrans2 = new GhostsPasoAJailer(ghost,gi,2);
			GhostsPasoARandom randomTrans2 = new GhostsPasoARandom(ghost,gi,2);
			GhostsVoyASerEscuderoQueProteje escuderoQueProteje2 = new GhostsVoyASerEscuderoQueProteje(ghost,gi,2);
			
			GhostsSeHanComidoAMiEdible noTengoEdible = new GhostsSeHanComidoAMiEdible(ghost,gi);
			GhostsVoyASerEscuderoQueProteje escuderoQueProteje3 = new GhostsVoyASerEscuderoQueProteje(ghost,gi,3);
			
			//Hunter1
			cfsChase.add(hunter1, hunter2Trans0, hunter2);
			cfsChase.add(hunter1, jailerTrans0, jailer);
			cfsChase.add(hunter1, randomTrans0, random);
			cfsChase.add(hunter1, escuderoQueProteje0, protectTheEdible);
			
			//Hunter2
			cfsChase.add(hunter2, hunter1Trans0, hunter1);
			cfsChase.add(hunter2, jailerTrans1, jailer);
			cfsChase.add(hunter2, randomTrans1, random);
			cfsChase.add(hunter2, escuderoQueProteje1, protectTheEdible);
			
			//Jailer
			cfsChase.add(jailer, hunter1Trans1, hunter1);
			cfsChase.add(jailer, hunter2Trans1, hunter2);
			cfsChase.add(jailer, randomTrans2, random);
			cfsChase.add(jailer, escuderoQueProteje2, protectTheEdible);
			
			//Random
			cfsChase.add(random, hunter1Trans2, hunter1);
			cfsChase.add(random, hunter2Trans2, hunter2);
			cfsChase.add(random, jailerTrans2, jailer);
			cfsChase.add(random, escuderoQueProteje3, protectTheEdible);
			
			//DefiendoAlComestible
			cfsChase.add(protectTheEdible, noTengoEdible, hunter1);
			
			cfsChase.ready(hunter1);
			
			CompoundState persecucion = new CompoundState("PERSECUCION", cfsChase);
			
			//RUNNING STATE
			FSM cfsRun = new FSM(ghost.name() + "RUN");
			GraphFSMObserver RunObserver = new GraphFSMObserver(cfsRun.toString());
			cfsRun.addObserver(RunObserver);
			
			//RunStates
			SimpleState runAway = new SimpleState(new RunOptimalAction(ghost));
			//SimpleState runSubOptimal = new SimpleState(new RunSubOptimalAction(ghost));
			SimpleState orbit = new SimpleState(new OrbitateAction(ghost));
			SimpleState runToEscudero = new SimpleState(new RunToEscuderoAction(ghost,gi));
			
			//RunTransitions
			GhostHayFantasmaEnMiCamino FantEnMiCaminoHuida = new GhostHayFantasmaEnMiCamino(ghost);
			GhostsHayEscudero hayEscuderoHuida0 = new GhostsHayEscudero(ghost,gi,0);
			GhostsHayEscudero hayEscuderoHuida1 = new GhostsHayEscudero(ghost,gi,1);
			GhostsHePasadoAlEscudero paseAlEscudero = new GhostsHePasadoAlEscudero(ghost,gi);
			GhostsNoFantasmasCerca noFantCerca = new GhostsNoFantasmasCerca(ghost); 
			GhostsNoHayFantasmasCercaEuclidiana noFantCercaEu = new GhostsNoHayFantasmasCercaEuclidiana(ghost);
			GhostsPacmanEstaLejos pacManLejos = new GhostsPacmanEstaLejos(ghost);
			GhostsPacmanEstaCerca pacManCerca = new GhostsPacmanEstaCerca(ghost);
			
			//Orbit Changes
			cfsRun.add(orbit, pacManCerca, runAway);
			
			//RunOptimal Changes
			cfsRun.add(runAway, hayEscuderoHuida0, runToEscudero);

			cfsRun.add(runAway, pacManLejos, orbit);
		
		
			//RunToEscudero Changes
			cfsRun.add(runToEscudero, paseAlEscudero, runAway);

			cfsRun.ready(runAway);
			CompoundState huida = new CompoundState("HUIDA", cfsRun);
			
			
			//FSM
			//Actions
			SimpleState startRunning = new SimpleState(new StartRunningAction(ghost));
			//Transitions
			GhostsPacManLejosParpadeo lejosParpadeo = new GhostsPacManLejosParpadeo(ghost);
			GhostsPacManHaComidoPP comioPP = new GhostsPacManHaComidoPP(ghost);
			PacManNearPPillTransition CrecadePP = new PacManNearPPillTransition();
			GhostsPacManNoVaAPillarPP NoVaAPillarPP = new GhostsPacManNoVaAPillarPP(ghost);
			GhostsEdibleTransition isGhostEdible = new GhostsEdibleTransition(ghost);
			//FSM CHANGES
			fsm.add(persecucion, CrecadePP, startRunning);
			fsm.add(startRunning, NoVaAPillarPP, persecucion);
			fsm.add(startRunning, comioPP, huida);
			fsm.add(huida,lejosParpadeo, persecucion);
			fsm.add(persecucion,isGhostEdible, huida);
			fsm.ready(persecucion);
			
			//RENDER
			JFrame frame = new JFrame();
	    	JPanel main = new JPanel();
	    	main.setLayout(new BorderLayout());
	    	main.add(RunObserver.getAsPanel(true, null), BorderLayout.EAST);
	    	main.add(ChaseObserver.getAsPanel(true, null), BorderLayout.WEST);
	    	frame.getContentPane().add(main);
	    	frame.pack();
	    	frame.setVisible(true);
			graphObserver.showInFrame(new Dimension(300,200));
			
			
			
			
			fsms.put(ghost, fsm);
			
		}
	}
	
	public void preCompute(String opponent) {
    	for(FSM fsm: fsms.values())
    		fsm.reset();
    }
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);
		
		GhostsInput in = new GhostsInput(game,gi);
		
		for(GHOST ghost: GHOST.values())
		{
			FSM fsm = fsms.get(ghost);
			MOVE move = fsm.run(in);
			result.put(ghost, move);
		}
		
		return result;
		
	
		
	}

}
