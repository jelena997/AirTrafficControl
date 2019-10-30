package aircraftModels;

import java.util.HashMap;

import interfaces.AerialPhotography;
import people.Person;

public class UnmannedAircraft extends Aircraft implements AerialPhotography{
	
	public UnmannedAircraft() {
		super();
	}
	
	public UnmannedAircraft(String model, String id, int height,int speedOfFlight,HashMap<String, String>characteristics, Person[] persons ) {
		super(model,id,height,speedOfFlight,characteristics,persons);
		
	}


}
