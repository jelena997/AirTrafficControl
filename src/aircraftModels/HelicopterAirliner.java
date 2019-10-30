package aircraftModels;

import java.util.HashMap;

import people.Person;

public class HelicopterAirliner extends Helicopter{
	
	private int numberOfSeats;
	
	public HelicopterAirliner() {
		super();
	}

	public HelicopterAirliner(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons ,int numberOfSeats) {
		super(model,id,height,speedOfFlight,characteristics,persons);
		this.numberOfSeats=numberOfSeats;
		
	}
	
	

}
