package aircraftModels;

import java.util.HashMap;

import interfaces.AttackingTarget;
import interfaces.CarryingWeapons;
import people.Person;
import simulator.Map;
import simulator.Simulator;

public class MilitaryPlane extends Airplane implements CarryingWeapons, AttackingTarget{
	private boolean hostMilitaryPlane;
	public  boolean isInAir;
	
	public MilitaryPlane() {
		super();
	}
	
	public MilitaryPlane(String model, String id, int height, int speedOfFlight,HashMap<String, String>characteristics, Person[] persons,boolean status ) {
		super(model,id,height,speedOfFlight,characteristics,persons);
		this.hostMilitaryPlane=status;
		isInAir=true;
		
		
	}
	
	
	public void setIsInAir(boolean status) {
		isInAir=status;
		
	}

	
	public boolean IsHost() {
		return hostMilitaryPlane;
	}
	
	@Override
	public void run() {
		
	
		if(!hostMilitaryPlane) isForeignMilitaryObjectPresent=true;
			
		if ("U".equals(characteristics.get("Position"))) {
            int column=Integer.parseInt(characteristics.get("Column position"));
			for (int position = 0; position < Map.getNumberRows(); position++) {

				//if (isDestroyed) return;
				if (checkIfIsCrashedAircraft()) {
					Simulator.militaryThreads.remove(this);
					return;
				}
				
				synchronized (Simulator.monitor) {

					if (Map.matrix[position][column].getSize() != 0)

					{
						if (checkIfCrashHappenedUpOrDown(position,column, "U")) {
							Simulator.militaryThreads.remove(this);
							return;
						}
						
					} else {

						this.updateMapUpOrDownMoving(position,column, "U");
						if(position==(Map.getNumberRows()-1)) Simulator.militaryThreads.remove(this);
					}
				}
				tSleep(speedOfFlight);
			}
		}
		 else if ("D".equals(characteristics.get("Position"))) {
             int column=Integer.parseInt(characteristics.get("Column position"));
			for (int position = Map.getNumberRows() - 1; position >= 0; position--) {
				//if (isDestroyed)
					//return;
				if (checkIfIsCrashedAircraft()) {
					Simulator.militaryThreads.remove(this);
					return;
				}
				synchronized (Simulator.monitor) {

					if (Map.matrix[position][column].getSize() != 0) {
						
						if (checkIfCrashHappenedUpOrDown(position,column, "D")) {
							Simulator.militaryThreads.remove(this);
							return;
						}
					}

					else {

						this.updateMapUpOrDownMoving(position,column, "D");
						if(position==0) Simulator.militaryThreads.remove(this);

					}
				}
				tSleep(speedOfFlight);
			}
		 }
		 else if ("L".equals(characteristics.get("Position"))) {
	            int row=Integer.parseInt(characteristics.get("Row position"));
				for (int position = 0; position < Map.getNumberColumns(); position++) {

					//if (isDestroyed)
						//return;
					if (checkIfIsCrashedAircraft()) {
						Simulator.militaryThreads.remove(this);
						return;
					}
					
					synchronized (Simulator.monitor) {
						if (Map.matrix[row][position].getSize() != 0) {

							if (checkIfCrashHappenedLeftOrRight(row,position, "L")) {
								Simulator.militaryThreads.remove(this);
								return;
							}
						}

						else {

							this.updateMapLeftOrRightMoving(row,position, "L");
						    if(position==(Map.getNumberColumns()-1)) Simulator.militaryThreads.remove(this);
						}

					}
					tSleep(speedOfFlight);
				}
		 }
		
		 else if ("R".equals(characteristics.get("Position"))) {
	            int row=Integer.parseInt(characteristics.get("Row position"));
				for (int position = Map.getNumberColumns() - 1; position >= 0; position--) {
					//if (isDestroyed)
						//return;
					if (checkIfIsCrashedAircraft()) {
						Simulator.militaryThreads.remove(this);
						return;
					}
						synchronized (Simulator.monitor) {
							if (Map.matrix[row][position].getSize() != 0) {
							
								if (checkIfCrashHappenedLeftOrRight(row,position, "R")) {
									Simulator.militaryThreads.remove(this);
									return;
								}
							}

							else {

								this.updateMapLeftOrRightMoving(row,position, "R");
								if(position==0) Simulator.militaryThreads.remove(this);

							}
						
						}
						tSleep(speedOfFlight);
					}
		 }
		
	}
}
