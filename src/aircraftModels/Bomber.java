package aircraftModels;

import java.util.HashMap;

import interfaces.GroundBombing;
import people.Person;

public class Bomber extends MilitaryPlane implements GroundBombing {

	public Bomber() {
		super();
	}
	
	public Bomber(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons ,boolean status) {
		super(model,id,height,speedOfFlight,characteristics,persons,status);
		
	}
}
