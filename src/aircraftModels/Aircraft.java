package aircraftModels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;

import application.WarningObject;
import exceptionHandler.LoggerClass;
import people.Person;
import radar.RadarSystem;
import simulator.Map;
import simulator.Simulator;

public class Aircraft extends AirVehicle{

	private static int MAX_NUMBER_OF_PASSENGERS = 20;
	protected String model = "";
	protected String uniqueID = "";
	protected int height;
	protected int speedOfFlight;
	public HashMap<String, String> characteristics = new HashMap<String, String>();
	private Person[] persons = new Person[MAX_NUMBER_OF_PASSENGERS];
	public static boolean isForeignMilitaryObjectPresent = false;
	public static boolean stopSimulation = false;
	private static LoggerClass logger;

	public Aircraft() {

	}

	public Aircraft(String model, String id, int height, int speedOfFlight, HashMap<String, String> characteristics,
			Person[] persons) {
		this.model = model;
		this.uniqueID = id;
		this.height = height;
		this.speedOfFlight = speedOfFlight;
		this.characteristics = characteristics;
		this.persons = persons;
		logger = new LoggerClass(Aircraft.class.getName());
		start();
	}

	public String getModel() {
		return model;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public int getHeight() {
		return height;
	}

	public double getSpeedOfFlight() {
		return speedOfFlight;
	}

	public HashMap<String, String> getCharacteristics() {
		return characteristics;
	}

	public Person[] getPersons() {
		return persons;
	}

	@Override
	public void run() {
		if ("U".equals(characteristics.get("Position"))) {
            int column=Integer.parseInt(characteristics.get("Column position"));
			for (int position = 0; position < Map.getNumberRows(); position++) {

				if (checkIfIsCrashedAircraft()) {
					Simulator.threads.remove(this);
					return;
				}
			
				if ((!isForeignMilitaryObjectPresent && !stopSimulation)) {

					synchronized (Simulator.monitor) {

						if (Map.matrix[position][column].getSize() != 0)

						{
							if (checkIfCrashHappenedUpOrDown(position,column, "U")) {
								Simulator.threads.remove(this);
								return;
							}
							
						} else {

							this.updateMapUpOrDownMoving(position,column, "U");
							if(position==(Map.getNumberRows()-1)) Simulator.threads.remove(this);
						}
					}
					tSleep(speedOfFlight);

				} else {

					if (this.checkExitUpOrDown(position, column, "U"))
						return;

					String sideForExit = findShortestPath((position - 1),column);

					if ("U".equals(sideForExit)) {
						int newColumn;
						if ((column + 1) < Map.getNumberColumns()) {
							newColumn = column + 1;
							Map.matrix[position - 1][newColumn].setElement(this);
							Map.matrix[position - 1][column].deleteElement(this);

						} else {
							newColumn = column - 1;
							Map.matrix[position - 1][newColumn].setElement(this);
							Map.matrix[position - 1][column].deleteElement(this);

						}

						for (int i = position - 2; i >= 0; i--) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[i][newColumn].getSize() != 0) {
									if (checkIfCrashHappenedUpOrDown(i, newColumn, "D")) {
										Simulator.threads.remove(this);
										return;
									}

								} else {
									this.updateMapUpOrDownMoving(i, newColumn, "D");
									if(i==0) Simulator.threads.remove(this);
									
								}
							}
							tSleep(speedOfFlight);

						}

						return;
					} else if ("D".equals(sideForExit)) {

						for (int i = position; i < Map.getNumberRows(); i++) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}
							synchronized (Simulator.monitor) {
								if (Map.matrix[i][column].getSize() != 0) {
									if (checkIfCrashHappenedUpOrDown(position,column, "U")) {
										Simulator.threads.remove(this);
										return;
									}

								} else {
									this.updateMapUpOrDownMoving(i,column, "U");
									if(i==(Map.getNumberRows()-1)) Simulator.threads.remove(this);
								}

							}
							tSleep(speedOfFlight);

						}
						return;
					} else if ("L".equals(sideForExit)) {
						int row = position - 1;
                           for (int i = column - 1; i >= 0; i--) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[row][i].getSize() != 0) {
									if (checkIfCrashHappenedLeftOrRight(row, i, "R")) {
										Simulator.threads.remove(this);
										return;
										
									}

								} else {
									this.updateMapLeftOrRightMoving(row, i, "R");
									if(i==0) Simulator.threads.remove(this);
								}
							}
							tSleep(speedOfFlight);
						}
						return;
					} else if ("R".equals(sideForExit)) {
						int row = position - 1;

						for (int i = column + 1; i < Map.getNumberColumns(); i++) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[row][i].getSize() != 0) {
									if (checkIfCrashHappenedLeftOrRight(row, i, "L")) {
										Simulator.threads.remove(this);
										return;
									}

								} else {
									this.updateMapLeftOrRightMoving(row, i, "L");
									if(i==(Map.getNumberColumns()-1)) Simulator.threads.remove(this);
								}
							}
							tSleep(speedOfFlight);

						}

						return;
					}
				}

			}
		} else if ("D".equals(characteristics.get("Position"))) {
             int column=Integer.parseInt(characteristics.get("Column position"));
			for (int position = Map.getNumberRows() - 1; position >= 0; position--) {
				
				if (checkIfIsCrashedAircraft()) {
					Simulator.threads.remove(this);
					return;
				}
				if (!isForeignMilitaryObjectPresent &&  !stopSimulation) {

					synchronized (Simulator.monitor) {

						if (Map.matrix[position][column].getSize() != 0) {
							
							if (checkIfCrashHappenedUpOrDown(position,column, "D")) {
								Simulator.threads.remove(this);
								return;
								
							}
						}

						else {

							this.updateMapUpOrDownMoving(position,column, "D");
							if(position==0) Simulator.threads.remove(this);

						}
					}
					tSleep(speedOfFlight);
				} else {

					if (this.checkExitUpOrDown(position, column, "D"))
						return;

					String sideForExit = findShortestPath((position + 1),column);

					if ("U".equals(sideForExit)) {

						for (int i = position; i >= 0; i--) {

							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[i][column].getSize() != 0) {
									if (checkIfCrashHappenedUpOrDown(i,column, "D")) {
										Simulator.threads.remove(this);
										return;
									}

								} else {
									this.updateMapUpOrDownMoving(i,column, "D");
									if(i==0) Simulator.threads.remove(this);
								}
							}
							tSleep(speedOfFlight);

						}
						return;
					} else if ("D".equals(sideForExit)) {
						int newColumn;
						if (column + 1 < Map.getNumberColumns()) {
							newColumn = column + 1;
							Map.matrix[position + 1][newColumn].setElement(this);
							Map.matrix[position + 1][column].deleteElement(this);

						} else {
							newColumn = column - 1;
							Map.matrix[position + 1][newColumn].setElement(this);
							Map.matrix[position + 1][column].deleteElement(this);

						}

						for (int i = position + 2; i < Map.getNumberRows(); i++) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[i][newColumn].getSize() != 0) {
									if (checkIfCrashHappenedUpOrDown(i, newColumn, "U")) {
										Simulator.threads.remove(this);
										return;
									}
								} else {
									this.updateMapUpOrDownMoving(i, newColumn, "U");
									if(i==(Map.getNumberRows()-1)) Simulator.threads.remove(this);
								}

							}
							tSleep(speedOfFlight);

						}
						return;
					} else if ("L".equals(sideForExit)) {
						int row = position + 1;

						for (int i = column - 1; i >= 0; i--) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}
							synchronized (Simulator.monitor) {
								if (Map.matrix[row][i].getSize() != 0) {
									if (checkIfCrashHappenedLeftOrRight(row, i, "R")) {
										Simulator.threads.remove(this);
										return;
									}

								} else {
									this.updateMapLeftOrRightMoving(row, i, "R");
									if (i == 0) Simulator.threads.remove(this);
								}
							}
							tSleep(speedOfFlight);
						}

						return;
					} else if ("R".equals(sideForExit)) {
						int row = position + 1;

						for (int i = column + 1; i < Map.getNumberColumns(); i++) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[row][i].getSize() != 0) {
									if (checkIfCrashHappenedLeftOrRight(row, i, "L")) {
										Simulator.threads.remove(this);
										return;
									}
								} else {

									this.updateMapLeftOrRightMoving(row, i, "L");
									if(i == (Map.getNumberColumns() - 1)) Simulator.threads.remove(this);
								}
							}
							tSleep(speedOfFlight);

						}

						return;
					}

				}
			}
		}

		else if ("L".equals(characteristics.get("Position"))) {
            int row=Integer.parseInt(characteristics.get("Row position"));
			for (int position = 0; position < Map.getNumberColumns(); position++) {
				
				if (checkIfIsCrashedAircraft()) {
					Simulator.threads.remove(this);
					return;
				}
				if (!isForeignMilitaryObjectPresent &&  !stopSimulation) {

					synchronized (Simulator.monitor) {
						if (Map.matrix[row][position].getSize() != 0) {

							if (checkIfCrashHappenedLeftOrRight(row,position, "L")) {
								Simulator.threads.remove(this);
								return;
							}
						}

						else {

							this.updateMapLeftOrRightMoving(row,position, "L");
						    if(position==(Map.getNumberColumns()-1)) Simulator.threads.remove(this);
						}

					}
					tSleep(speedOfFlight);
				} else {

					if (this.checkExitLeftOrRight(row, position, "L"))
						return;
					String sideForExit = findShortestPath(row,position - 1);

					if ("U".equals(sideForExit)) {

						int column = position - 1;
						for (int i = row - 1; i >= 0; i--) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[i][column].getSize() != 0) {
									if (checkIfCrashHappenedUpOrDown(i, column, "D")) {
										Simulator.threads.remove(this);
										return;
									}
								} else {
									this.updateMapUpOrDownMoving(i, column, "D");
									if (i == 0) Simulator.threads.remove(this);
								}

							}
							tSleep(speedOfFlight);
						}

						return;
					} else if ("D".equals(sideForExit)) {
						int column = position - 1;

						for (int i =row + 1; i < Map.getNumberRows(); i++) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[i][column].getSize() != 0) {
									if (checkIfCrashHappenedUpOrDown(i, column, "U")) {
										Simulator.threads.remove(this);
										return;
									}
								} else {
									this.updateMapUpOrDownMoving(i, column, "U");
									if (i == (Map.getNumberRows() - 1)) Simulator.threads.remove(this);
								}

							}
							tSleep(speedOfFlight);

						}
						return;
					} else if ("L".equals(sideForExit)) {
						int newRow;
						if (row + 1 < Map.getNumberRows()) {
							newRow = row + 1;
							Map.matrix[newRow][position - 1].setElement(this);
							Map.matrix[row][position - 1].deleteElement(this);

						} else {
							newRow = row - 1;
							Map.matrix[newRow][position - 1].setElement(this);
							Map.matrix[row][position - 1].deleteElement(this);

						}

						for (int i = position - 2; i >= 0; i--) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[newRow][i].getSize() != 0) {
									if (checkIfCrashHappenedLeftOrRight(newRow, i, "R")) {
										Simulator.threads.remove(this);
										return;
									}

								} else {
									this.updateMapLeftOrRightMoving(newRow, i, "R");
									if (i == 0) Simulator.threads.remove(this);
								}

							}
							tSleep(speedOfFlight);
						}

						return;
					} else if ("R".equals(sideForExit)) {

						for (int i = position; i < Map.getNumberColumns(); i++) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[row][i].getSize() != 0) {
									if (checkIfCrashHappenedLeftOrRight(row, i, "L")) {
										Simulator.threads.remove(this);
										return;
									}
								} else {
									this.updateMapLeftOrRightMoving(row, i, "L");
									if (i == (Map.getNumberColumns() - 1)) Simulator.threads.remove(this);
								}

							}
							tSleep(speedOfFlight);
						}
						return;
					}
				}

			}
		} else if ("R".equals(characteristics.get("Position"))) {
            int row=Integer.parseInt(characteristics.get("Row position"));
			for (int position = Map.getNumberColumns() - 1; position >= 0; position--) {
				
				if (checkIfIsCrashedAircraft()) {
					Simulator.threads.remove(this);
					return;
				}
				if (!isForeignMilitaryObjectPresent &&  !stopSimulation) {

					synchronized (Simulator.monitor) {
						if (Map.matrix[row][position].getSize() != 0) {
						
							if (checkIfCrashHappenedLeftOrRight(row,position, "R")) {
								Simulator.threads.remove(this);
								return;
							}
						}

						else {

							this.updateMapLeftOrRightMoving(row,position, "R");
							if(position==0) Simulator.threads.remove(this);

						}
					}

					tSleep(speedOfFlight);
				}

				else {

					if (this.checkExitLeftOrRight(row, position, "R"))
						return;
					String sideForExit = findShortestPath(row,position + 1);

					if ("U".equals(sideForExit)) {
						int column = position + 1;

						for (int i = row - 1; i >= 0; i--) {

							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}
							synchronized (Simulator.monitor) {
								if (Map.matrix[i][column].getSize() != 0) {
									if (checkIfCrashHappenedUpOrDown(i, column, "D")) {
										Simulator.threads.remove(this);
										return;
									}
								} else {
									this.updateMapUpOrDownMoving(i, column, "D");
									if (i == 0) Simulator.threads.remove(this);
								}

							}
							tSleep(speedOfFlight);
						}
						return;
					} else if ("D".equals(sideForExit)) {
						int column = position + 1;

						for (int i = row + 1; i < Map.getNumberRows(); i++) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {

								if (Map.matrix[i][column].getSize() != 0) {
									if (checkIfCrashHappenedUpOrDown(i, column, "U")) {
										Simulator.threads.remove(this);
										return;
									}
								} else {
									this.updateMapUpOrDownMoving(i, column, "U");
									if (i == (Map.getNumberRows() - 1)) Simulator.threads.remove(this);
								}

							}
							tSleep(speedOfFlight);

						}
						return;
					} else if ("L".equals(sideForExit)) {

						for (int i = position; i >= 0; i--) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[row][i].getSize() != 0) {
									if (checkIfCrashHappenedLeftOrRight(row, i, "R")) {
										Simulator.threads.remove(this);
										return;
									}
								} else {
									this.updateMapLeftOrRightMoving(row, i, "R");
									if (i == 0) Simulator.threads.remove(this);
								}
							}
							tSleep(speedOfFlight);
						}
						return;
					} else if ("R".equals(sideForExit)) {
						int newRow;
						if (row + 1 < Map.getNumberRows()) {
							newRow = row + 1;
							Map.matrix[newRow][position + 1].setElement(this);
							Map.matrix[row][position + 1].deleteElement(this);

						} else {
							newRow = row - 1;
							Map.matrix[newRow][position + 1].setElement(this);
							Map.matrix[row][position + 1].deleteElement(this);

						}

						for (int i = position + 2; i < Map.getNumberColumns(); i++) {
							if (checkIfIsCrashedAircraft()) {
								Simulator.threads.remove(this);
								return;
							}

							synchronized (Simulator.monitor) {
								if (Map.matrix[newRow][i].getSize() != 0) {
									if (checkIfCrashHappenedLeftOrRight(newRow, i, "L")) {
										Simulator.threads.remove(this);
										return;
									}
								} else {
									this.updateMapLeftOrRightMoving(newRow, i, "L");
									if (i == (Map.getNumberColumns() - 1))  Simulator.threads.remove(this);
								}

							}
							tSleep(speedOfFlight);
						}

						return;
					}

				}

			}

		}
	}

	public static String findShortestPath(int i, int j) {
		String nearestSide = "";
		ArrayList<Integer> list = new ArrayList<Integer>();
		int leftSide = j;
		int upSide = i;
		int rightSide = (Map.getNumberColumns() - 1) - j;
		int downSide = (Map.getNumberRows() - 1) - i;
		list.add(leftSide);
		list.add(upSide);
		list.add(rightSide);
		list.add(downSide);
		Collections.sort(list);

		if (list.get(0) == leftSide) {
			nearestSide = "L";
		} else if (list.get(0) == upSide) {
			nearestSide = "U";
		} else if (list.get(0) == rightSide) {
			nearestSide = "R";
		} else {
			nearestSide = "D";
		}

		return nearestSide;
	}

	public  boolean checkIfCrashHappenedUpOrDown(int row, int column, String side) {
		int sizeBeforeAdding = Map.matrix[row][column].getSize();
		Map.matrix[row][column].setElement(this);
		if ("U".equals(side)) {
			if (row > 0)
				Map.matrix[row - 1][column].deleteElement(this);
		} else {
			if (row < (Map.getNumberRows() - 1))
				Map.matrix[row + 1][column].deleteElement(this);
		}
		  if(sizeBeforeAdding>0) {
		for (int i = 0; i < sizeBeforeAdding; i++) {
			if (Map.matrix[row][column].getSize() != 0) {
				Aircraft temp = Map.matrix[row][column].getElement(i);
				if (getHeight() == temp.getHeight()) {
					WarningObject object = new WarningObject("Sudar letjelica: " + model + " " + temp.model, row,
							column);
					serializeObject(object);
					Map.matrix[row][column].deleteElement(this);
					Map.matrix[row][column].deleteElement(temp);

					Simulator.chrashedAircrafts.add(temp);

					return true;
				}

			}
		}
		  }
		return false;
	}

	public boolean checkIfCrashHappenedLeftOrRight(int row, int column, String side) {
		int sizeBeforeAdding = Map.matrix[row][column].getSize();
		Map.matrix[row][column].setElement(this);
		if ("L".equals(side)) {
			if (column > 0)
				Map.matrix[row][column - 1].deleteElement(this);
		} else {
			if (column < (Map.getNumberColumns() - 1))
				Map.matrix[row][column + 1].deleteElement(this);

		}
        if(sizeBeforeAdding>0) {
		for (int i = 0; i < sizeBeforeAdding; i++) {
			if (Map.matrix[row][column].getSize() != 0) {
				Aircraft temp = Map.matrix[row][column].getElement(i);
				if (this.getHeight() == temp.getHeight()) {
					WarningObject object = new WarningObject("Sudar letjelica: " + model + " " + temp.model, row,
							column);
					serializeObject(object);
					Map.matrix[row][column].deleteElement(this);
					Map.matrix[row][column].deleteElement(temp);

					Simulator.chrashedAircrafts.add(temp);
					return true;
				}

			}

		}
        }
		return false;

	}

	public void updateMapUpOrDownMoving(int row, int column, String side) {

		Map.matrix[row][column].setElement(this);
		if ("U".equals(side)) {
			if (row > 0) {
				Map.matrix[row - 1][column].deleteElement(this);

			}
			if (row == (Map.getNumberRows() - 1)) {
				Map.matrix[row][column].deleteElement(this);

			}
		} else {
			if (row < (Map.getNumberRows() - 1)) {
				Map.matrix[row + 1][column].deleteElement(this);

			}

			if (row == 0) {
				Map.matrix[row][column].deleteElement(this);

			}

		}

	}

	public void updateMapLeftOrRightMoving(int row, int column, String side) {
		Map.matrix[row][column].setElement(this);
		if ("L".equals(side)) {
			if (column > 0) {
				Map.matrix[row][column - 1].deleteElement(this);

			}
			if (column == (Map.getNumberColumns() - 1)) {
				Map.matrix[row][column].deleteElement(this);

			}
		} else {
			if (column < (Map.getNumberColumns() - 1)) {
				Map.matrix[row][column + 1].deleteElement(this);

			}
			if (column == 0) {
				Map.matrix[row][column].deleteElement(this);

			}

		}

	}

	public boolean checkExitUpOrDown(int row, int column, String side) {
		
		if("U".equals(side)) {
			if(row==0) { 
			Simulator.threads.remove(this);
			return true;
			}
			if(row==1 || row==(Map.getNumberRows()-1) || row==(Map.getNumberRows()-2)) {
				Map.matrix[row-1][column].deleteElement(this);
				Simulator.threads.remove(this);
				return true;
			}
			if(column==0 || column==1 || column==(Map.getNumberColumns()-1)|| column==(Map.getNumberColumns()-2)) {
				Map.matrix[row-1][column].deleteElement(this);
				Simulator.threads.remove(this);
				return true;
				
			}
		}
		else {
			if(row==(Map.getNumberRows()-1)) {
				Simulator.threads.remove(this);
				return true;
			}
			
			if(row==(Map.getNumberRows()-2) || row==0 || row==1) {
				Map.matrix[row+1][column].deleteElement(this);
				Simulator.threads.remove(this);
				return true;
			}
			if(column==0 || column==1 || column==(Map.getNumberColumns()-1)|| column==(Map.getNumberColumns()-2)) {
				Map.matrix[row+1][column].deleteElement(this);
				Simulator.threads.remove(this);
				return true;
				
			}
			
		}
		return false;

	}

	public boolean checkExitLeftOrRight(int row, int column, String side) {
		if("L".equals(side)) {
			if(column==0) {
				Simulator.threads.remove(this);
				return true;
			}
			
			if(column==1 || column==(Map.getNumberColumns()-1)|| column==(Map.getNumberColumns()-2)) {
				Map.matrix[row][column-1].deleteElement(this);
				Simulator.threads.remove(this);
				return true;
			}
			
			if(row==0 || row==1|| row==(Map.getNumberRows()-1)|| row==(Map.getNumberRows()-2)) {
				Map.matrix[row][column-1].deleteElement(this);
				Simulator.threads.remove(this);
				return true;
			}
			
			
		}
		else if("R".equals(side)) {
			if(column==(Map.getNumberColumns()-1)) {
				Simulator.threads.remove(this);
				return true;
			}
			
			if(column==(Map.getNumberColumns()-2)|| column==0 || column==1) {
				Map.matrix[row][column+1].deleteElement(this);
				Simulator.threads.remove(this);
				return true;
			}
			
			if(row==0 || row==1 || row==(Map.getNumberRows()-1)|| row==(Map.getNumberRows()-2)) {
				Map.matrix[row][column+1].deleteElement(this);
				Simulator.threads.remove(this);
				return true;
			}
		}

		return false;
	}

	

	public static void tSleep(int timeInS) {
		try {
			Thread.sleep(timeInS * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkIfIsCrashedAircraft() {
		for (Aircraft a : Simulator.chrashedAircrafts) {
			if (this.uniqueID.equals(a.uniqueID)) {
				return true;

			}
		}
		return false;
	}

	private static void serializeObject(WarningObject object) {
		try {
			ObjectOutputStream printWarningObjects = new ObjectOutputStream(
					new FileOutputStream(new File(System.getProperty("user.home") + File.separator + "eclipse-workspace"
							+ File.separator + "AirTrafficControl2019" + File.separator + "alert" + File.separator
							+ System.currentTimeMillis())));
			printWarningObjects.writeObject(object);
		} catch (Exception ex) {
			logger.log(Level.WARNING, ex);
		}
	}
}
