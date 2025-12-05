package es.ucm.fdi.ici.c2526.practica4.grupoYY.CBRengine;

import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman.vectorCBR;

public class vectorCBRSimilarity implements LocalSimilarityFunction {
	
	public vectorCBRSimilarity(){
		
	}

	/**
	 * Applies the similarity function.
	 * 
	 * @param o1
	 * @param o2
	 * @return result of apply the similarity function.
	 */
	public double compute(Object o1, Object o2) throws NoApplicableSimilarityFunctionException {
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof  vectorCBR))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o1.getClass());
		if (!(o2 instanceof  vectorCBR))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());
		
		double sim = 0;
		
		String[] s1 = o1.toString().split("#");
		String[] s2 = o2.toString().split("#");
		
		for(int i = 0; i < Math.min(s1.length, s2.length);i++) {
			if(s1[i].equals(s2[i])) {
				sim++;
			}
		}

		return sim/4;
	}
	
	/** Applicable to Integer */
	public boolean isApplicable(Object o1, Object o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof vectorCBR;
		else if(o2==null)
			return o1 instanceof vectorCBR;
		else
			return (o1 instanceof vectorCBR)&&(o2 instanceof vectorCBR);
	}
}
