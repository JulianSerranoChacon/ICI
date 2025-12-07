import es.ucm.fdi.ici.c2526.practica4.grupoYY.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

import pacman.game.util.Stats;

public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        PacmanController pacMan = new MsPacMan();
        GhostController ghosts = new es.ucm.fdi.ici.c2526.practica1.grupoB.Ghosts();
        
        System.out.println( 
            executor.runGame(pacMan, ghosts, 30) //last parameter defines speed
        );    
        /*Stats stats[] = executor.runExperiment(pacMan, ghosts, 100, "triaje 1");//last parameter defines speed
        for (Stats stat : stats) {
            System.out.println( 
                stat.toString()
            ); 
        }*/
    }
	
}
