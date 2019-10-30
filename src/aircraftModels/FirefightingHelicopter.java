package aircraftModels;

import java.util.HashMap;

import interfaces.CarryingWater;
import interfaces.Firefighting;
import people.Person;

public class FirefightingHelicopter extends Helicopter implements CarryingWater,Firefighting {
	
	public FirefightingHelicopter() {
		super();
	}

	public FirefightingHelicopter(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons ) {
		super(model,id,height,speedOfFlight,characteristics,persons);
		
	}

}
