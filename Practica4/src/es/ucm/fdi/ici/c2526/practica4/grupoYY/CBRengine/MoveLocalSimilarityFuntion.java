package es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class MoveLocalSimilarityFuntion implements LocalSimilarityFunction{

	public MoveLocalSimilarityFuntion(){
		
	}
	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		if ((caseObject == null) || (queryObject == null))
			return 0;
		if(caseObject.toString().equals(queryObject.toString())) return 1;
		return 0;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		if ((caseObject == null) || (queryObject == null))
			return false;
		return (caseObject.toString().equals(queryObject.toString()));
	}

}
