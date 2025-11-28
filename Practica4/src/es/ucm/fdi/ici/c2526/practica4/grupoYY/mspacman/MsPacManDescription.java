package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class MsPacManDescription implements CaseComponent {

	Integer id;
	
	Integer score;
	Integer time;
	
	//New variables
	Integer numPPills;
	Integer nearestPPill;
	Integer nearestPill;
	
	
	vectorCBR<Double> ghostToPacman = new vectorCBR<Double>(4);
	/*
	Double  ghostToPacman1;
	Double  ghostToPacman2;
	Double  ghostToPacman3;
	Double  ghostToPacman4;
	*/
	vectorCBR<Double> pacmanToGhost = new vectorCBR<Double>(4);
	/*
	Double  pacmanToGhost1;
	Double  pacmanToGhost2;
	Double  pacmanToGhost3;
	Double  pacmanToGhost4;
	*/
	vectorCBR<Integer> ghostEdibleTime = new vectorCBR<Integer>(4);
	/*
	Integer  ghostEdibleTime1;
	Integer  ghostEdibleTime2;
	Integer  ghostEdibleTime3;
	Integer  ghostEdibleTime4;
	*/
	
	String pacmanMove;
	String  ghostToPacman1Movement;
	String  ghostToPacman2Movement;
	String  ghostToPacman3Movement;
	String  ghostToPacman4Movement;




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

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
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

	public Double getGhostToPacman(int index) {
		return ghostToPacman.getElement(index);
	}

	public void setGhostToPacman(int index,Double ghostToPacmand) {
		this.ghostToPacman.setElement(index, ghostToPacmand);
	}

	public Double getPacmanToGhost(int index) {
		return pacmanToGhost.getElement(index);
	}

	public void setPacmanToGhost(int index,Double pacmanToGhostd) {
		this.pacmanToGhost.setElement(index, pacmanToGhostd);
	}

	public Integer getGhostEdibleTime(int index) {
		return ghostEdibleTime.getElement(index);
	}

	public void setGhostEdibleTime(int index,Integer ghostEdibleTime) {
		this.ghostEdibleTime.setElement(index, ghostEdibleTime);
	}

	public String getPacmanMove() {
		return pacmanMove;
	}

	public void setPacmanMove(String pacmanMove) {
		this.pacmanMove = pacmanMove;
	}

	public String getGhostToPacman1Movement() {
		return ghostToPacman1Movement;
	}

	public void setGhostToPacman1Movement(String ghostToPacman1Movement) {
		this.ghostToPacman1Movement = ghostToPacman1Movement;
	}

	public String getGhostToPacman2Movement() {
		return ghostToPacman2Movement;
	}

	public void setGhostToPacman2Movement(String ghostToPacman2Movement) {
		this.ghostToPacman2Movement = ghostToPacman2Movement;
	}

	public String getGhostToPacman3Movement() {
		return ghostToPacman3Movement;
	}

	public void setGhostToPacman3Movement(String ghostToPacman3Movement) {
		this.ghostToPacman3Movement = ghostToPacman3Movement;
	}

	public String getGhostToPacman4Movement() {
		return ghostToPacman4Movement;
	}

	public void setGhostToPacman4Movement(String ghostToPacman4Movement) {
		this.ghostToPacman4Movement = ghostToPacman4Movement;
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
	            + ", time=" + time

	            + ", numPPills=" + numPPills
	            + ", nearestPPill=" + nearestPPill
	            + ", nearestPill=" + nearestPill

	            + ", ghostToPacman=" + ghostToPacman.toString()

	            + ", pacmanToGhost1=" + pacmanToGhost.toString()

	            + ", ghostEdibleTime1=" + ghostEdibleTime.toString()

	            + ", pacmanMove=" + pacmanMove
	            + ", ghostToPacman1Movement=" + ghostToPacman1Movement
	            + ", ghostToPacman2Movement=" + ghostToPacman2Movement
	            + ", ghostToPacman3Movement=" + ghostToPacman3Movement
	            + ", ghostToPacman4Movement=" + ghostToPacman4Movement
	            + "]";
	}


//TODO: In case we want it stored with ; instead of ,

//	@Override
//	public String toString() {
//	    return "MsPacManDescription ["
//	            + "id=" + id
//	            + "; score=" + score
//	            + "; time=" + time
//
//	            + "; numPPills=" + numPPills
//	            + "; nearestPPill=" + nearestPPill
//	            + "; nearestPill=" + nearestPill
//
//	            + "; ghostToPacman1=" + ghostToPacman1
//	            + "; ghostToPacman2=" + ghostToPacman2
//	            + "; ghostToPacman3=" + ghostToPacman3
//	            + "; ghostToPacman4=" + ghostToPacman4
//
//	            + "; pacmanToGhost1=" + pacmanToGhost1
//	            + "; pacmanToGhost2=" + pacmanToGhost2
//	            + "; pacmanToGhost3=" + pacmanToGhost3
//	            + "; pacmanToGhost4=" + pacmanToGhost4
//
//	            + "; ghostEdibleTime1=" + ghostEdibleTime1
//	            + "; ghostEdibleTime2=" + ghostEdibleTime2
//	            + "; ghostEdibleTime3=" + ghostEdibleTime3
//	            + "; ghostEdibleTime4=" + ghostEdibleTime4
//
//	            + "; pacmanMove=" + pacmanMove
//	            + "; ghostToPacman1Movement=" + ghostToPacman1Movement
//	            + "; ghostToPacman2Movement=" + ghostToPacman2Movement
//	            + "; ghostToPacman3Movement=" + ghostToPacman3Movement
//	            + "; ghostToPacman4Movement=" + ghostToPacman4Movement
//	            + "]";
//	}

	

}
