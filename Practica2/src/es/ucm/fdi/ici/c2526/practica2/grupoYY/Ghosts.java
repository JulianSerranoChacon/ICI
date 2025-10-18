package es.ucm.fdi.ici.c2526.practica2.grupoYY;

import java.awt.Dimension;
import java.util.EnumMap;

//General
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.GhostsInput;

//Actions
import es.ucm.fdi.ici.c2526.practica2.grupoYY.ghosts.actions.ChaseAction;
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
			
			//ChaseStates
			SimpleState chase = new SimpleState(new ChaseAction(ghost));
			SimpleState protectTheEdible = new SimpleState(new ProtectEdibleGhostAction(ghost,gi));
			
			//ChaseTransitions
			GhostsPasoAHunter1 hunter1 = new GhostsPasoAHunter1(ghost,gi);
			GhostsPasoAHunter2 hunter2 = new GhostsPasoAHunter2(ghost,gi);
			GhostsPasoAJailer jailer = new GhostsPasoAJailer(ghost,gi);
			GhostsPasoARandom random = new GhostsPasoARandom(ghost,gi);
			GhostsNotEdibleAndPacManFarPPill toChaseTransition = new GhostsNotEdibleAndPacManFarPPill(ghost);
			GhostsVoyASerEscuderoQueProteje escuderoQueProteje = new GhostsVoyASerEscuderoQueProteje(ghost,gi);
			
			//MiddleStates
			SimpleState startRunning = new SimpleState(new StartRunningAction(ghost));
			
			//MiddleTransitions
			PacManNearPPillTransition near = new PacManNearPPillTransition();
			GhostsEdibleTransition edible = new GhostsEdibleTransition(ghost);
			GhostsPacManHaComidoPP comioPP = new GhostsPacManHaComidoPP(ghost);
			GhostsPacManLejosParpadeo lejosParpadeo = new GhostsPacManLejosParpadeo(ghost);
			GhostsPacManVaAPillarPP VaAPillarLaPP = new GhostsPacManVaAPillarPP(ghost);
			
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
		
		
			
			//CHASE CHANGES
			fsm.add(chase, edible, runAway);
			fsm.add(chase, near, runAway);
			fsm.add(runAway, toChaseTransition, chase);
			
			//TODO METER LOS CAMBIOS PARA LOS ESTADOS DE CAZAR
			
			//MIDDLE CHANGES
			fsm.add(startRunning, comioPP, runAway);
			//TODO METER EL ESTADO DE PASO PARA TODOS LOS ESTADOS DE CAZAR
			
		
			
			//FLEE CHANGES
			//Orbit Changes
			fsm.add(orbit, pacManCerca, runAway);
		//	fsm.add(orbit, lejosParpadeo, chase);
			
			//RunOptimal Changes
			fsm.add(runAway, hayEscuderoHuida, runToEscudero);
			fsm.add(runAway, FantEnMiCaminoHuida, runSubOptimal);
			fsm.add(runAway, pacManLejos, orbit);
			fsm.add(runAway, lejosParpadeo, chase);
			
			//RunSubOptimal Changes
			fsm.add(runSubOptimal, noFantCerca, runAway);
		//	fsm.add(runSubOptimal, hayEscuderoHuida, runToEscudero);
		//	fsm.add(runSubOptimal, lejosParpadeo, chase);

			//RunToEscudero Changes
			fsm.add(runToEscudero, paseAlEscudero, runAway);
		//	fsm.add(runToEscudero, lejosParpadeo, chase);
			
			fsm.ready(chase);
			
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
