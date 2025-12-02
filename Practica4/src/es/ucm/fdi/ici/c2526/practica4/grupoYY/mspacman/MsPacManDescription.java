package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;



import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class MsPacManDescription implements CaseComponent {

	Integer id;
	Integer score;
	Integer numPPills;
	Integer nearestPPill;
	Integer nearestPill;
	vectorCBR<Double> ghostToPacman = new vectorCBR<Double>(4);
	vectorCBR<Double> pacmanToGhost = new vectorCBR<Double>(4);
	vectorCBR<Double> ghostEdibleTime = new vectorCBR<Double>(4);
	String pacmanLastMove;
	vectorCBR<String> ghostLastMoves = new vectorCBR<String>(4);
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getNearestPPill() {
		return nearestPPill;
	}

	public void setNearestPPill(Integer nearestPPill) {
		this.nearestPPill = nearestPPill;
	}
	
	// NEW ATRIBUTES //
	
	public Integer getNumPPills() {
		return numPPills;
	}

	public void setNumPPills(Integer numPPills) {
		this.numPPills = numPPills;
	}

	public Integer getNearestPill() {
		return nearestPill;
	}

	public void setNearestPill(Integer nearestPill) {
		this.nearestPill = nearestPill;
	}

	public vectorCBR<Double> getGhostToPacman() {
		return ghostToPacman;
	}

	public void setGhostToPacman(vectorCBR<Double> ghostToPacmand) {
		this.ghostToPacman = ghostToPacmand;
	}

	public vectorCBR<Double> getPacmanToGhost() {
		return pacmanToGhost;
	}

	public void setPacmanToGhost(vectorCBR<Double>pacmanToGhostd) {
		this.pacmanToGhost = pacmanToGhostd;
	}

	public  vectorCBR<Double> getGhostEdibleTime() {
		return ghostEdibleTime;
	}

	public void setGhostEdibleTime(vectorCBR<Double>ghostEdibleTime) {
		this.ghostEdibleTime = ghostEdibleTime;
	}

	public String getPacmanLastMove() {
		return pacmanLastMove;
	}

	public void setPacmanLastMove(String pacmanMove) {
		this.pacmanLastMove = pacmanMove;
	}

	public vectorCBR<String> getGhostLastMoves() {
		return ghostLastMoves;
	}

	public void setGhostLastMoves( vectorCBR<String> lastMove ) {
		ghostLastMoves = lastMove;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}

	@Override
	public String toString() {
	    return "MsPacManDescription ["
	            + "id=" + id
	            + ", score=" + score
	            + ", numPPills=" + numPPills
	            + ", nearestPPill=" + nearestPPill
	            + ", nearestPill=" + nearestPill

	            + ", ghostToPacman=" + ghostToPacman.toString()

	            + ", pacmanToGhost=" + pacmanToGhost.toString()

	            + ", ghostEdibleTime=" + ghostEdibleTime.toString()

	            + ", pacmanMove=" + pacmanLastMove
	            + ", ghostLastMoves=" + ghostLastMoves.toString()
	            
	            + "]";
	}


}
