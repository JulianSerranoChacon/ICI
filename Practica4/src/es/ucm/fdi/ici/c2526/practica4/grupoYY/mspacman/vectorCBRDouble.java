package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.util.Vector;
import es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor;

public class vectorCBRDouble implements TypeAdaptor  {
	Vector<Double> _v = new Vector<Double>();
	public vectorCBRDouble(){
		for(int i = 0; i < 4; ++i) {
			_v.add(i, null);
		}
	}
	public vectorCBRDouble(int size){
		for(int i = 0; i < size; ++i) {
			_v.add(i, null);
		}
	}
	@Override
	//Parse vector values into String 
	public String toString(){
		String aux = "";
		for(int i = 0; i < _v.size();++i) {
			Double value = _v.get(i);
		 	aux += String.valueOf(value);
		 	//If we are not in the last value, add # to split values
		 	if(i < _v.size() - 1) {
		 		aux += "#";
		 	}
		}
		return aux;
	}
	//Set element value in the index
	public void setElement(int index,Double value) {
		_v.set(index, value);
	}
	//Get the index value
	public Double getElement(int index) {
		return _v.get(index);
	}
	@Override
	public void fromString(String content) throws Exception {
        _v.clear(); // reset vector
        String[] s = content.split("#");
        for(String token : s) {
            _v.add(Double.parseDouble(token));
        }
		
	}
}
