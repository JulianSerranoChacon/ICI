package es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman.vectorCBRDouble;

public class IntervalVectorCBR implements LocalSimilarityFunction{

	double _interval;
	public IntervalVectorCBR(double interval) {
		_interval = interval;
	}
	@Override
	public double compute(Object o1, Object o2) throws NoApplicableSimilarityFunctionException {
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof vectorCBRDouble))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o1.getClass());
		if (!(o2 instanceof vectorCBRDouble))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());
		
		vectorCBRDouble i1 = (vectorCBRDouble) o1;
		vectorCBRDouble i2 = (vectorCBRDouble) o2;
		
		double sol = 0.0;
		
		//Priority Value to give more relevance to the closest Ghosts
		double[] prioriyValues = new double[4];
		prioriyValues[0] = 1.0;
		prioriyValues[1] = 0.75;
		prioriyValues[2] = 0.5;
		prioriyValues[3] = 0.25;
		
		//For every ghost distance we compare the similitude of o1,o2 with the priority value
		for(int i = 0; i < 4; ++i) {
			double v1 = i1.getElement(i).doubleValue();
			double v2 = i2.getElement(i).doubleValue();
			sol += (1.0 - ((double) Math.abs(v1 - v2) / _interval)) * prioriyValues[i];
		}
		return sol/2.5; //We return the half of the similitudes
	}

	@Override
	public boolean isApplicable(Object o1, Object o2) {
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof vectorCBRDouble;
		else if(o2==null)
			return o1 instanceof vectorCBRDouble;
		else
			return (o1 instanceof vectorCBRDouble)&&(o2 instanceof vectorCBRDouble);
	}

}
