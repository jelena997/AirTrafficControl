package simulator;

import java.io.File;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import aircraftModels.Aircraft;
import aircraftModels.Airliner;
import aircraftModels.AntihailRocket;
import aircraftModels.FirefightingHelicopter;
import aircraftModels.FirefightingPlane;
import aircraftModels.Helicopter;
import aircraftModels.HelicopterAirliner;
import aircraftModels.Hunter;
import aircraftModels.MilitaryPlane;
import aircraftModels.TransportHelicopter;
import aircraftModels.TransportPlane;
import aircraftModels.UnmannedAircraft;
import exceptionHandler.LoggerClass;
import fileWatching.ConfigFileWatcher;
import people.Passenger;
import people.Person;
import people.Pilot;
import radar.RadarSystem;

public class Simulator extends Thread{
	
	public static final Object monitor=new Object();
	
	public Map map;
	private int n;
	private Random rand=new Random();
	private FileInputStream input;
	private static boolean foreignMilitaryObjects=false;
	private static boolean hostMilitaryObjects=false;
	public static ArrayList<Aircraft> threads=new ArrayList<Aircraft>();
	public static ArrayList<Aircraft> militaryThreads=new ArrayList<Aircraft>();
	public static ArrayList<Aircraft> chrashedAircrafts=new ArrayList<Aircraft>();
	private static LoggerClass logger;
	private static boolean signalForProtection=false;
	
	public Simulator(Map map) {
		
		Properties properties=new Properties();
		logger=new LoggerClass(Simulator.class.getName());
		try {
			
		
		 input=new FileInputStream(System.getProperty("user.home")+ File.separator+"eclipse-workspace"+ File.separator+ "AirTrafficControl2019"+ File.separator+ "resources"+File.separator+ "config.properties");
		
		     properties.load(input);
		    n=Integer.parseInt(properties.getProperty("n"));
			input.close();
		}
		catch(Exception ex) {
			logger.log(Level.WARNING, ex);
		}
		
	
	   this.map=map; 
	  start();
}
	
	public static void setForeignMilitaryVariable(boolean expression) {
		foreignMilitaryObjects=expression;
		
	}
	
	public static void setHostMilitaryVariable(boolean expression) {
		hostMilitaryObjects=expression;
	}
	
	@Override
	public  void run() {
		
		
		while(true) {
			
			
			   Person persons[]=createPersons();
			   String[] positions= {"U","D","L","R"};//up,down,left,right
			   int[] heights= {3046,2050};
				 
			   while(Aircraft.stopSimulation) {
				  tSleep(2);
				   
			   }
				if(foreignMilitaryObjects) {
					System.out.println("Create military plane");
					createMilitaryPlane(true,persons,positions,heights);
				}
				if(hostMilitaryObjects) {
					createMilitaryPlane(false, persons, positions, heights);
					
				}
				
				if(Aircraft.isForeignMilitaryObjectPresent) {
				while(!threads.isEmpty()) {
					if(threads.size()<=3) {
						threads.clear();
						
					}
					tSleep(1);
					}
					
				Aircraft.isForeignMilitaryObjectPresent=false;
			
				}
				
				
			int randomNumber=rand.nextInt(8);
		
			switch(randomNumber) {
			case 0:{
			
				HashMap<String, String> characteristics=new HashMap<String, String>();
				int height=setDataForAircraft(positions,heights,characteristics);
				threads.add(new TransportPlane("Model"+randomNumber, UUID.randomUUID().toString(), height, (rand.nextInt(3)+1), characteristics, persons));
	            break;
			}
			case 1:{
			
				HashMap<String, String> characteristics=new HashMap<String, String>();
				int height=setDataForAircraft(positions, heights, characteristics);
				threads.add(new Airliner("Model"+randomNumber, UUID.randomUUID().toString(), height, (rand.nextInt(3)+1), characteristics, persons,100,50));
				break;
				
			}
			case 2:{
				
				HashMap<String, String> characteristics=new HashMap<String, String>();
				int height=setDataForAircraft(positions, heights, characteristics);
				threads.add(new FirefightingPlane("Model"+randomNumber, UUID.randomUUID().toString(), height, (rand.nextInt(3)+1), characteristics, persons));
				break;
				
			}
			case 3:{
				
				
				HashMap<String, String> characteristics=new HashMap<String, String>();
				int height=setDataForAircraft(positions, heights, characteristics);
				threads.add(new UnmannedAircraft("Model"+randomNumber, UUID.randomUUID().toString(), height, (rand.nextInt(3)+1), characteristics, persons));
		        break;
			}
			
			case 4:{
				
			
				HashMap<String, String> characteristics=new HashMap<String, String>();
				int height=setDataForAircraft(positions, heights, characteristics);
				threads.add(new Helicopter("Model"+randomNumber,UUID.randomUUID().toString(), height, (rand.nextInt(3)+1), characteristics, persons));
				break;
				
			}
			case 5:{
				
				HashMap<String, String> characteristics=new HashMap<String, String>();
				int height=setDataForAircraft(positions, heights, characteristics);
				threads.add(new TransportHelicopter("Model"+randomNumber, UUID.randomUUID().toString(), height, (rand.nextInt(3)+1), characteristics, persons));
				break;
			}
			case 6:{
				
				HashMap<String, String> characteristics=new HashMap<String, String>();
				int height=setDataForAircraft(positions, heights, characteristics);
				threads.add(new HelicopterAirliner("Model"+randomNumber, UUID.randomUUID().toString(), height, (rand.nextInt(3)+1), characteristics, persons,15));
				break;
				
			}
			case 7:{
				
				HashMap<String, String> characteristics=new HashMap<String, String>();
				int height=setDataForAircraft(positions, heights, characteristics);
			    threads.add(new FirefightingHelicopter("Model"+randomNumber, UUID.randomUUID().toString(), height, (rand.nextInt(3)+1), characteristics, persons));
			    break;
			}
			
		}
			tSleep(n);
			
		}
		
}
	
	 public static void setPositions(HashMap<String,String> characteristics,String randomPosition) {
	    	Random rand=new Random();
	    	if("U".equals(randomPosition)|| "D".equals(randomPosition)) {
	    
	    		int columnIndex=rand.nextInt(Map.getNumberColumns());
	    	
	    		characteristics.put("Position",randomPosition);
				characteristics.put("Column position",Integer.toString(columnIndex));
	    		
	    	}
	    	else if("L".equals(randomPosition)|| "R".equals(randomPosition)) {
	    		
	    		int rowIndex=rand.nextInt(Map.getNumberRows());
	    	
	    		characteristics.put("Position",randomPosition);
				characteristics.put("Row position",Integer.toString(rowIndex));
	    	}
	    	
	    }
	
	 
	  
	  public static void sendHunters(int row, int column,String side,int height) {
		  Random random=new Random();
		  if("U".equals(side) || "D".equals(side)){
			  HashMap<String, String> characteristicsH1=new HashMap<String, String>();
			  HashMap<String, String> characteristicsH2=new HashMap<String, String>();
			  if("U".equals(side)) {
			  characteristicsH1.put("Position","U");
			  characteristicsH2.put("Position","U");
			  }
			  else {
				  characteristicsH1.put("Position","D");
				  characteristicsH2.put("Position","D"); 
			  }
			  if(column==0) {
				  characteristicsH1.put("Column position",Integer.toString(column));
			       characteristicsH2.put("Column position",Integer.toString(column+1));
			  }
			  else if(column==(Map.getNumberColumns()-1)) {
				  characteristicsH1.put("Column position",Integer.toString(column-1));
			       characteristicsH2.put("Column position",Integer.toString(column));
			  }
			  else {
				  characteristicsH1.put("Column position",Integer.toString(column-1));
			       characteristicsH2.put("Column position",Integer.toString(column+1));
			  }
				
				  militaryThreads.add(new Hunter("HunterLeft",UUID.randomUUID().toString(),(height+1),(random.nextInt(3)+1),characteristicsH1,null,true));
				  militaryThreads.add(new Hunter("HunterRight",UUID.randomUUID().toString(),(height+1),(random.nextInt(3)+3),characteristicsH2,null,true));
			  
		  }
		  else if("L".equals(side)|| "R".equals(side)) {
			  HashMap<String, String> characteristicsH1=new HashMap<String, String>();
			  HashMap<String, String> characteristicsH2=new HashMap<String, String>();
			  
			  if("L".equals(side)) {
			  characteristicsH1.put("Position","L");
			  characteristicsH2.put("Position","L");
			  }
			  else {
				  characteristicsH1.put("Position","R");
				  characteristicsH2.put("Position","R");
			  }
			  
			  if(row==0) {
				  characteristicsH1.put("Row position",Integer.toString(row));
			      characteristicsH2.put("Row position",Integer.toString(row+1));
				  
			  }
			  else if(row==(Map.getNumberRows()-1)) {
				  characteristicsH1.put("Row position",Integer.toString(row-1));
			      characteristicsH2.put("Row position",Integer.toString(row));
			  }
			  else {
				  characteristicsH1.put("Row position",Integer.toString(row-1));
			      characteristicsH2.put("Row position",Integer.toString(row+1));
			  }
			 
			  militaryThreads.add(new Hunter("HunterLeft",UUID.randomUUID().toString(),height,(random.nextInt(3)+1),characteristicsH1,null,true));
			  militaryThreads.add(new Hunter("HunterRight",UUID.randomUUID().toString(),height,(random.nextInt(3)+1),characteristicsH2,null,true));
		  }
	  }
   private Person[] createPersons() {
	   Person persons[]=new Person[5];
		Pilot pilot=new Pilot("Pilot name", "Pilot last name");
		Pilot coPilot=new Pilot("Co-pilot name", "Co-pilot last name");
		Passenger[] passengers=new Passenger[3];
		for(int i=0; i<3;i++) {
			passengers[i]=new Passenger("Passenger name","Passenger last name",Integer.toString(i)+ rand.nextInt(1000));
		}
		persons[0]=pilot;
		persons[1]=coPilot;
		persons[2]=passengers[0];
		persons[3]=passengers[1];
		persons[4]=passengers[2];
		
		return persons;
	   
   }
	
   private void createMilitaryPlane(boolean status,Person[] persons,String[] positions,int[] heights) {
	   if(status) {
		   HashMap<String, String> characteristics=new HashMap<String, String>();
			int height=setDataForAircraft(positions, heights, characteristics);
			setForeignMilitaryVariable(false);
			
			militaryThreads.add(new MilitaryPlane("Military plane",UUID.randomUUID().toString(), height, (rand.nextInt(3)+1), characteristics, persons,false));
		   
	   }
	   else {
		   HashMap<String, String> characteristics=new HashMap<String, String>();
			int height=setDataForAircraft(positions, heights, characteristics);
			setHostMilitaryVariable(false);
			threads.add(new MilitaryPlane("Military plane", UUID.randomUUID().toString(), height, (rand.nextInt(3)+1), characteristics, persons,true));	
		   
	   }
	   
   }
   
   private int setDataForAircraft(String[] positions,int[] heights,HashMap<String, String> characteristics) {

		Random randPrivate=new Random();
		int randomNum=randPrivate.nextInt(4);
		String randomPosition=positions[randomNum];
		Simulator.setPositions(characteristics, randomPosition);
		int randomHeight=randPrivate.nextInt(2);
		int height=heights[randomHeight];
		
		return height;
	   
   }
   

   
   public static void tSleep(int timeInS) {
       try {
           Thread.sleep(timeInS*1000);
       } catch (Exception ex) { logger.log(Level.WARNING, ex); }
   }

}


	
	
		
		
		
	
	


