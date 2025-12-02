package es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class EqualNumLocalSimilarityFuntion implements LocalSimilarityFunction{

	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		if ((caseObject == null) || (queryObject == null))
			return 0;
		if(caseObject == queryObject) return 1;
		return 0;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		if ((caseObject == null) || (queryObject == null))
			return false;
		return (caseObject == queryObject);
	}

}
