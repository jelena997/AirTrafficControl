package aircraftModels;

import java.util.HashMap;

import interfaces.CarryingWater;
import interfaces.Firefighting;
import people.Person;

public class FirefightingPlane extends Airplane implements Firefighting, CarryingWater{
	
	public FirefightingPlane() {
		super();
	}
	
	public FirefightingPlane(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons ) {
		super(model,id,height,speedOfFlight,characteristics,persons);
		
	}


}
