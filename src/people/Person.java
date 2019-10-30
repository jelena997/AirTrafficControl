package people;

public class Person {

	

	private String name="";
	private String lastName="";
	
	public Person() {
		
	}
	
	public Person(String name,String lastName) {
		this.name=name;
		this.lastName=lastName;
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}
	
}
