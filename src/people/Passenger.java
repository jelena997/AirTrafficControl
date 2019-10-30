package people;




public class Passenger extends Person {
	
	
private String passportNumber="";
	
	public Passenger() {
		super();
	}
	
	public Passenger(String name,String lastName,String passportNumber) {
		super(name,lastName);
		this.passportNumber=passportNumber;
	}

	public String getPassportNumber() {
		return passportNumber;
	}


}

