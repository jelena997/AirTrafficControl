package aircraftModels;

import java.util.HashMap;

import people.Person;

public class Helicopter extends Aircraft{
	
	public Helicopter() {
	super();
}

public Helicopter(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons ) {
	super(model,id,height,speedOfFlight,characteristics,persons);
	
}

}
