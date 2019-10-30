package aircraftModels;

import java.util.HashMap;

import people.Person;

public class Airliner extends Airplane{
	
	private int numberOfSeats;
	private double baggageAllowance;
	
	public Airliner() {
		super();
	}
	
	public Airliner(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons, int numberOfSeats, double baggageAllowance ) {
		super(model,id,height,speedOfFlight,characteristics,persons);
		this.numberOfSeats=numberOfSeats;
		this.baggageAllowance=baggageAllowance;
		
		
	}


}
