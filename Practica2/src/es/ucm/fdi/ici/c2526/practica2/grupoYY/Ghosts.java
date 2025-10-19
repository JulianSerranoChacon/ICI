package es.ucm.fdi.ici.c2526.practica2.grupoYY;

import java.awt.Dimension;
import java.util.EnumMap;

//General
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;

//Actions
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.ChaseAction;
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
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsNotEdibleAndPacManFarPPill;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPacmanEstaLejos;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPacmanEstaCerca;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPacManHaComidoPP;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPacManLejosParpadeo;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPacManVaAPillarPP;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPasoAHunter1;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPasoAHunter2;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPasoAJailer;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsPasoARandom;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsSoyComestible;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.GhostsVoyASerEscuderoQueProteje;
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.transitions.PacManNearPPillTransition;
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
			GhostsPasoAHunter1 hunter1Trans = new GhostsPasoAHunter1(ghost,gi);
			GhostsPasoAHunter2 hunter2Trans = new GhostsPasoAHunter2(ghost,gi);
			GhostsPasoAJailer jailerTrans = new GhostsPasoAJailer(ghost,gi);
			GhostsPasoARandom randomTrans = new GhostsPasoARandom(ghost,gi);
			GhostsVoyASerEscuderoQueProteje escuderoQueProteje = new GhostsVoyASerEscuderoQueProteje(ghost,gi);
			
			//Hunter1
			cfsChase.add(hunter1, hunter2Trans, hunter2);
			cfsChase.add(hunter1, jailerTrans, jailer);
			cfsChase.add(hunter1, randomTrans, random);
			cfsChase.add(hunter1, escuderoQueProteje, protectTheEdible);
			
			//Hunter2
			cfsChase.add(hunter2, hunter1Trans, hunter1);
			cfsChase.add(hunter2, jailerTrans, jailer);
			cfsChase.add(hunter2, randomTrans, random);
			cfsChase.add(hunter2, escuderoQueProteje, protectTheEdible);
			
			//Jailer
			cfsChase.add(jailer, hunter1Trans, hunter1);
			cfsChase.add(jailer, hunter2Trans, hunter2);
			cfsChase.add(jailer, randomTrans, random);
			cfsChase.add(jailer, escuderoQueProteje, protectTheEdible);
			
			//Random
			cfsChase.add(random, hunter1Trans, hunter1);
			cfsChase.add(random, hunter2Trans, hunter2);
			cfsChase.add(random, jailerTrans, jailer);
			cfsChase.add(random, escuderoQueProteje, protectTheEdible);
			
			//DefiendoAlComestible
			cfsChase.add(protectTheEdible, hunter1Trans, hunter1);
			cfsChase.add(protectTheEdible, hunter2Trans, hunter2);
			cfsChase.add(protectTheEdible, jailerTrans, jailer);
			cfsChase.add(protectTheEdible, randomTrans, random);
			
			cfsChase.ready(hunter1);
			
			CompoundState persecucion = new CompoundState("PERSECUCION", cfsChase);
			
			//RUNNING STATE
			FSM cfsRun = new FSM(ghost.name() + "RUN");
			GraphFSMObserver RunObserver = new GraphFSMObserver(cfsRun.toString());
			cfsRun.addObserver(RunObserver);
			
			//RunStates
			SimpleState runAway = new SimpleState(new RunOptimalAction(ghost));
			SimpleState runSubOptimal = new SimpleState(new RunSubOptimalAction(ghost));
			SimpleState orbit = new SimpleState(new OrbitateAction(ghost));
			SimpleState runToEscudero = new SimpleState(new RunToEscuderoAction(ghost,gi));
			
			//RunTransitions
			GhostHayFantasmaEnMiCamino FantEnMiCaminoHuida = new GhostHayFantasmaEnMiCamino(ghost);
			GhostsHayEscudero hayEscuderoHuida = new GhostsHayEscudero(ghost);
			GhostsHePasadoAlEscudero paseAlEscudero = new GhostsHePasadoAlEscudero(ghost,gi);
			GhostsNoFantasmasCerca noFantCerca = new GhostsNoFantasmasCerca(ghost); 
			GhostsNoHayFantasmasCercaEuclidiana noFantCercaEu = new GhostsNoHayFantasmasCercaEuclidiana(ghost);
			GhostsPacmanEstaLejos pacManLejos = new GhostsPacmanEstaLejos(ghost);
			GhostsPacmanEstaCerca pacManCerca = new GhostsPacmanEstaCerca(ghost);
			GhostsSoyComestible soyComestible = new GhostsSoyComestible(ghost);
			
			//Orbit Changes
			cfsRun.add(orbit, pacManCerca, runAway);
			
			//RunOptimal Changes
			cfsRun.add(runAway, hayEscuderoHuida, runToEscudero);
			cfsRun.add(runAway, FantEnMiCaminoHuida, runSubOptimal);
			cfsRun.add(runAway, pacManLejos, orbit);
		
			//RunSubOptimal Changes
			cfsRun.add(runSubOptimal, noFantCerca, runAway);
			cfsRun.add(runSubOptimal, hayEscuderoHuida, runToEscudero);
		
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
			GhostsPacManVaAPillarPP VaAPillarLaPP = new GhostsPacManVaAPillarPP(ghost);
			//FSM CHANGES
			fsm.add(persecucion, VaAPillarLaPP, startRunning);
			fsm.add(startRunning, comioPP, huida);
			fsm.add(huida,lejosParpadeo, persecucion);
			fsm.ready(persecucion);
			
			
			
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
