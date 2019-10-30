package aircraftModels;

import java.util.HashMap;

import people.Person;

public class TransportPlane extends Airplane{
	
	public TransportPlane() {
		super();
	}
	
	public TransportPlane(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons ) {
		super(model,id,height,speedOfFlight,characteristics,persons);
		
	}


}
