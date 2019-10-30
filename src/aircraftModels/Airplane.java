package aircraftModels;

import java.util.HashMap;

import people.Person;

public class Airplane extends Aircraft{
	
	

	public Airplane() {
		super();
	}
	
	public Airplane(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons ) {
		super(model,id,height,speedOfFlight,characteristics,persons);
		
	}


}
