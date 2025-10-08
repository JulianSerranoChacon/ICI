//import es.ucm.fdi.ici.c2425.practica0.SerranoChacon.GhostsAggresive;
//import es.ucm.fdi.ici.c2425.practica0.SerranoChacon.GhostsRandom;
//import es.ucm.fdi.ici.c2425.practica0.SerranoChacon.MsPacManRandom;
//import es.ucm.fdi.ici.c2425.practica0.SerranoChacon.MsPacManRunAway;
import es.ucm.fdi.ici.c2425.practica0.SerranoChacon.Ghosts;
import es.ucm.fdi.ici.c2425.practica0.SerranoChacon.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
    	
 public class ExecutorTest {
    public static void main(String[] args) {
    	Executor executor = new Executor.Builder()
    		.setTickLimit(4000)
    		.setVisual(true)
    		.setScaleFactor(3.0)
    		.build();
    	PacmanController pacMan = new MsPacMan();
    	GhostController ghosts = new Ghosts();
    	
    	System.out.println(
    	executor.runGame(pacMan, ghosts, 50)
    	);
    }
}	
