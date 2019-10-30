package aircraftModels;

import java.util.HashMap;

import interfaces.AerialBombing;
import interfaces.GroundBombing;
import people.Person;
import simulator.Map;
import simulator.Simulator;

public class Hunter extends MilitaryPlane implements AerialBombing, GroundBombing{
	
	public Hunter() {
		super();
	}
	
	public Hunter(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons,boolean status ) {
		super(model,id,height,speedOfFlight,characteristics,persons,status);
		
	}
	


}
