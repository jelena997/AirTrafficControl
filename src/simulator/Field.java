package simulator;

import java.util.ArrayList;

import aircraftModels.Aircraft;

public class Field {
	
	private ArrayList<Aircraft> listOfAircrafts;
	
	public Field() {
		super();
		listOfAircrafts=new ArrayList<Aircraft>();	
		}

	public Aircraft getElement(int index) {
		return listOfAircrafts.get(index);
	}
	
	public void  setElement(Aircraft element) {
		listOfAircrafts.add(element);
	}
	
	public int getSize() {
		return listOfAircrafts.size();
	}
	
	public void deleteElement(Aircraft a) {
		listOfAircrafts.remove(a);
	}
}
