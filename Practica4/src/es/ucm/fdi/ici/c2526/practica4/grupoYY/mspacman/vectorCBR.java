package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.util.Vector;
import java.util.function.Function;

public class vectorCBR<T> {
	Vector<T> _v;
	vectorCBR(int size){
		_v = new Vector<T>(size);
	}
	//Parse vector values into String 
	public String toString(){
		String aux = "";
		for(int i = 0; i < _v.size();++i) {
			T value = _v.get(i);
		 	aux += String.valueOf(value);
		 	//If we are not in the last value, add # to split values
		 	if(i < _v.size() - 1) {
		 		aux += "#";
		 	}
		}
		return aux;
	}
	//Parse the string into the data Structure that we decide
	public void ParseString(String toParse,Function<String, T> converter) {
        _v.clear(); // reset vector
        String[] s = toParse.split("#");
        for(String token : s) {
            _v.add(converter.apply(token));
        }
	}
	//Set element value in the index
	public void setElement(int index,T value) {
		_v.set(index, value);
	}
	//Get the index value
	public T getElement(int index) {
		return _v.get(index);
	}
}
