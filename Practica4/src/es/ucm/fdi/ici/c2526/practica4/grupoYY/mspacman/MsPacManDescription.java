package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;



import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class MsPacManDescription implements CaseComponent {

	Integer id;
	Integer score;
	Integer numPPills;
	Integer nearestPill;
	vectorCBRDouble ghostToPacman = new vectorCBRDouble(4);
	vectorCBRDouble pacmanToGhost = new vectorCBRDouble(4);
	vectorCBRDouble ghostEdibleTime = new vectorCBRDouble(4);
	Integer pacmanNode;
	vectorCBRDouble ghostPosition = new vectorCBRDouble(4);
	String pacmanLastMove;
	vectorCBR ghostLastMoves = new vectorCBR(4);
	
	

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

	// NEW ATRIBUTES //
	
	public vectorCBRDouble getGhostPositions() {
		return ghostPosition;
	}
	public void setGhostPositions(vectorCBRDouble ghostP) {
		ghostPosition = ghostP;
	}
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

	public vectorCBRDouble getGhostToPacman() {
		return ghostToPacman;
	}

	public void setGhostToPacman(vectorCBRDouble ghostToPacmand) {
		this.ghostToPacman = ghostToPacmand;
	}

	public vectorCBRDouble getPacmanToGhost() {
		return pacmanToGhost;
	}

	public void setPacmanToGhost(vectorCBRDouble pacmanToGhostd) {
		this.pacmanToGhost = pacmanToGhostd;
	}

	public  vectorCBRDouble getGhostEdibleTime() {
		return ghostEdibleTime;
	}

	public void setGhostEdibleTime(vectorCBRDouble ghostEdibleTime) {
		this.ghostEdibleTime = ghostEdibleTime;
	}

	public String getPacmanLastMove() {
		return pacmanLastMove;
	}

	public Integer getPacmanNode() {
		return this.pacmanNode;
	}
	public void setPacmanNode(Integer pacmanNode) {
		this.pacmanNode = pacmanNode;
	}
	public void setPacmanLastMove(String pacmanMove) {
		this.pacmanLastMove = pacmanMove;
	}

	public vectorCBR getGhostLastMoves() {
		return ghostLastMoves;
	}

	public void setGhostLastMoves( vectorCBR lastMove ) {
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
	            + ", nearestPill=" + nearestPill

	            + ", ghostToPacman=" + ghostToPacman.toString()

	            + ", pacmanToGhost=" + pacmanToGhost.toString()

	            + ", ghostEdibleTime=" + ghostEdibleTime.toString()
	            + ", pacmanNode=" + pacmanNode
	            + ", ghostPosition=" + ghostPosition
	            
	            + ", pacmanMove=" + pacmanLastMove
	            + ", ghostLastMoves=" + ghostLastMoves.toString()
	            
	            + "]";
	}


}
