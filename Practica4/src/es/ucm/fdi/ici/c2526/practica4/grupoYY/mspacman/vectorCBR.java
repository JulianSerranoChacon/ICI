package es.ucm.fdi.ici.c2526.practica4.grupoYY.mspacman;

import java.util.Vector;
import es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor;

public class vectorCBR implements TypeAdaptor {
	Vector<String> _v = new Vector<String>();
	public vectorCBR(){
		
	}
	public vectorCBR(int size){
		for(int i = 0; i < size; ++i) {
			_v.add(i, null);
		}
	}
	//Parse vector values into String 
	@Override
	public String toString(){
		String aux = "";
		for(int i = 0; i < _v.size();++i) {
			String value = _v.get(i);
		 	aux += String.valueOf(value);
		 	//If we are not in the last value, add # to split values
		 	if(i < _v.size() - 1) {
		 		aux += "#";
		 	}
		}
		return aux;
	}
	//Parse the string into the data Structure that we decide
	public void ParseString(String toParse) {
        _v.clear(); // reset vector
        String[] s = toParse.split("#");
        for(String token : s) {
            _v.add(token);
        }
	}
	//Set element value in the index
	public void setElement(int index,String value) {
		_v.set(index, value);
	}
	//Get the index value
	public String getElement(int index) {
		return _v.get(index);
	}
	@Override
	public void fromString(String content) throws Exception {
		  _v.clear(); // reset vector
	        String[] s = content.split("#");
	        for(String token : s) {
	            _v.add(token);
	        }
	}
}
