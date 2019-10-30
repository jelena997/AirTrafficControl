package aircraftModels;

import java.util.HashMap;

import people.Person;

public class TransportHelicopter extends Helicopter {
	
	public TransportHelicopter() {
		super();
	}

	public TransportHelicopter(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons ) {
		super(model,id,height,speedOfFlight,characteristics,persons);
		
	}

}
