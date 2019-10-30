package aircraftModels;



import simulator.Map;
import simulator.Simulator;

public class Rocket extends AirVehicle{
	
	private double range;
	private int height;
	private int speedOfFlight;
	private String smjerKretanja;
	private int column;
	
	public Rocket() {
		
	}

	
	public Rocket(double range, int height, int speedOfFlight,String smjerKretanja, int column) {
		this.range=range;
		this.height=height;
		this.speedOfFlight=speedOfFlight;
		this.smjerKretanja=smjerKretanja;
		this.column=column;
		start();
		
		
	}
	
	@Override
	public void run() {
		
		if("DOLE".equals(smjerKretanja)) {
			for(int i=0;i<Map.getNumberRows()-1;i++) {
				synchronized (Simulator.monitor) {

					if (Map.matrix[i][column].getSize() != 0) {
						checkIfRocketCrashed();
					}
					System.out.println("Krecem se");
					  updateRocketMoving();

				try {
					sleep(speedOfFlight*1000);
				}
				catch(InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		}
		if("GORE".equals(smjerKretanja)) {
			for(int i=Map.getNumberRows()-1;i>=0;i--) {
				synchronized (Simulator.monitor) {

					if (Map.matrix[i][column].getSize() != 0) {
						checkIfRocketCrashed();
					}
					System.out.println("Krecem se");
                    updateRocketMoving();
					try {
						sleep(speedOfFlight*1000);
					}
					catch(InterruptedException ex) {
						ex.printStackTrace();
					}

				
			}
			}
			
		}
	}
	
	public void updateRocketMoving() {
		System.out.println("Updatee");
	}
	
	private void checkIfRocketCrashed() {
		System.out.println("Cheeck");
		
	}
}
