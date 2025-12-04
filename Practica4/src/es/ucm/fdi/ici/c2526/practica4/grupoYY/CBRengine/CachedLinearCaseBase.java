package es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine;

import java.util.ArrayList;
import java.util.Collection;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman.MsPacManDescription;

/**
 * Cached case base that only persists cases when closing.
 * learn() and forget() are not synchronized with the persistence until close() is invoked.
 * <p>
 * This class presents better performance that LinelCaseBase as only access to the persistence once.
 * This case base is used for evaluation.
 * 
 * @author Juan A. Recio-García
 */
public class CachedLinearCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<java.util.ArrayList<CBRCase>> originalCases;
	//private java.util.ArrayList<CBRCase> workingCases;
	//LAS CUATRO PRIMERAS LISTAS SON LAS DE CUANDO HAY PPILLS 
	private java.util.ArrayList< java.util.ArrayList<CBRCase>> workingCaseArray; //TODO parsear esto a Array de ArrayList (Cada elemento del array equivale a un CSV distinto)
	private Collection<CBRCase> casesToRemove;
	
	private Integer nextId;
	
	/**
	 * Closes the case base saving or deleting the cases of the persistence media
	 */
	public void close() {
		//workingCases.removeAll(casesToRemove);
		for(int i = 0; i< workingCaseArray.size();i++) {
			workingCaseArray.get(i).removeAll(casesToRemove); //quizas no funcione TODO
		}
		Collection<CBRCase> casesToStore = new ArrayList<>(workingCaseArray); //TODO crear metodo para pasar toda la estructura a una lista
		casesToStore.removeAll(originalCases);

		connector.storeCases(casesToStore);
		connector.deleteCases(casesToRemove);
		connector.close();
	}

	/**
	 * Forgets cases. It only removes the cases from the storage media when closing.
	 */
	public void forgetCases(Collection<CBRCase> cases) {
		for(int i = 0; i< workingCaseArray.size();i++) workingCaseArray.get(i).removeAll(cases); // se podria optimizar con ifs creo
		casesToRemove.addAll(cases);
	}

	/**
	 * Returns working cases.
	 */
	public Collection<CBRCase> getCases(int index) {
		
		return workingCaseArray.get(index);
	}

	/**
	 * TODO.
	 */
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {
		// TODO
		return null;
	}

	/**
	 * Initializes the Case Base with the cases read from the given connector.
	 */
	public void init(Connector connector) throws InitializingException {
		this.connector = connector;
		originalCases = this.connector.retrieveAllCases();	
		workingCaseArray = new java.util.ArrayList< java.util.ArrayList<CBRCase>>(originalCases);
		casesToRemove = new ArrayList<>();
		boolean allListEmpty = true;
	
			nextId = 1;
		for(int i = 0; i< workingCaseArray.size();i++) {
			if(!workingCaseArray.get(i).isEmpty()) {
				//quizas no parsee bien
				int aux = (int)workingCaseArray.get(i).get(workingCaseArray.get(i).size()-1).getID();
				if(aux > nextId) nextId = aux+1;
			}
		}
	}
	

	public Integer getNextId()
	{
		return nextId;
	}
	
	/**
	 * Learns cases that are only saved when closing the Case Base.
	 */
	public void learnCases(Collection<CBRCase> cases) {
		//Miramos los casos uno a uno de la coleccion que nos entra y lo metemos en su lista correspondiente
		for(CBRCase aux : cases) {
			MsPacManDescription mMsDescription = (MsPacManDescription)aux.getDescription();
			String lastMove = mMsDescription.getPacmanLastMove();
			if(mMsDescription.getNumPPills() != 0) {
				//quedan ppills
				if(lastMove == "UP") {
					workingCaseArray.get(0).add(aux);
				}
				else if (lastMove == "DOWN") {
					workingCaseArray.get(1).add(aux);
				} else if(lastMove == "LEFT") {
					workingCaseArray.get(2).add(aux);
				}else {
					workingCaseArray.get(3).add(aux);
				}
				
			}
			else {
				//no quedan
				if(lastMove == "UP") {
					workingCaseArray.get(4).add(aux);
				}
				else if (lastMove == "DOWN") {
					workingCaseArray.get(5).add(aux);
				} else if(lastMove == "LEFT") {
					workingCaseArray.get(6).add(aux);
				}else {
					workingCaseArray.get(7).add(aux);
				}
			}
			
		}
		nextId += cases.size();
	}

}

