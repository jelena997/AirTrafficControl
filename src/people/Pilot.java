package people;




public class Pilot extends Person{

	
private boolean licenseForFlying=false;
	
	public Pilot() {
		super();
	}
	
	public Pilot(String name,String lastName) {
	    super(name,lastName);
		this.licenseForFlying=true;
	}

	public boolean isLicenseForFlying() {
		return licenseForFlying;
	}
	
	
}
