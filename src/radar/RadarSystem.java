package radar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import aircraftModels.Aircraft;
import aircraftModels.Airliner;
import aircraftModels.Airplane;
import aircraftModels.Bomber;
import aircraftModels.Helicopter;
import aircraftModels.Hunter;
import aircraftModels.MilitaryPlane;
import aircraftModels.TransportPlane;
import aircraftModels.UnmannedAircraft;
import exceptionHandler.LoggerClass;
import interfaces.Firefighting;
import simulator.Map;
import simulator.Simulator;

public class RadarSystem extends Thread {
	private Map map;
	private int updatingTime;
	private PrintWriter writer;
	private LoggerClass logger;
	public static final Object monitor=new Object();
	
	public RadarSystem(Map map) {
		Properties properties=new Properties();
		logger=new LoggerClass(RadarSystem.class.getName());
	
		try {
		FileInputStream input=new FileInputStream(System.getProperty("user.home")+ File.separator+"eclipse-workspace"+ File.separator+ "AirTrafficControl2019"+ File.separator+ "resources"+File.separator+"radar.properties");
		properties.load(input);
	
		}
		catch(FileNotFoundException ex) {
			
			logger.log(Level.WARNING, ex);
		
		}
		catch(IOException ex) {
			logger.log(Level.WARNING, ex);
		}
		
		this.map=map;
		String temporary=properties.getProperty("n");
		updatingTime=Integer.parseInt(temporary);
		start();
	}
	
	@Override
	public void run() {

		while(true){
			
			synchronized (monitor) {
			
				try {
				writer=new PrintWriter(System.getProperty("user.home")+ File.separator+"eclipse-workspace"+ File.separator+ "AirTrafficControl2019"+ File.separator+"map.txt");
				
				}
				catch(IOException ex) {
					logger.log(Level.WARNING, ex);
				}
				for(int i=0;i<Map.getNumberRows();i++) {
				for(int j=0;j<Map.getNumberColumns();j++) {
					
					if(Map.matrix[i][j].getSize()!=0) {
						
					int size=Map.matrix[i][j].getSize();
					
					for(int k=0;k<size;k++) {
						if(Map.matrix[i][j].getSize()!=0) {
						Aircraft temp= Map.matrix[i][j].getElement(k);
						String type= getAircraftType(temp);
						writer.println(type+"-"+i+"-"+j+"-"+temp.getUniqueID());
						if(temp instanceof MilitaryPlane) {
							MilitaryPlane militaryPlane=(MilitaryPlane)temp;
							String side=(militaryPlane.getCharacteristics()).get("Position");
							int height=militaryPlane.getHeight();
							if( !militaryPlane.IsHost() && militaryPlane.isInAir) {
								militaryPlane.setIsInAir(false);
							this.protectAirspace(i,j,side,height);
							
							}
							
						}
						
						
						}
				
					}
					}
				
				}
				}
				writer.close();
			}
			try {
				Thread.sleep((long)updatingTime*1000);
				}
				
				catch(InterruptedException ex) {
					logger.log(Level.WARNING, ex);
					
				}
			
		
		}
	}
	
	private void protectAirspace(int row,int column,String side,int height) {
		try {
		File folder=new File("events");
		if(!folder.exists()) {
			folder.mkdir();
		}
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		String filePath = folder.getAbsolutePath() + File.separator + dateFormat.format(date) + ".txt";
		BufferedWriter  out = new BufferedWriter(new FileWriter(new File(filePath)));
		out.write("Foreign Military plane"+" - "+row+" - "+column);
		out.close();
		Simulator.sendHunters(row, column, side, height);
		}
		catch(IOException ex) {
			logger.log(Level.WARNING, ex);
		}
	}

	
	private String getAircraftType(Aircraft object) {
		String resultString="";
		if(object instanceof Hunter)
			resultString= "Hunter";
		else if(object instanceof MilitaryPlane)
			resultString="Military plane";
		else if(object instanceof TransportPlane)
			resultString="Transport plane";
		else if(object instanceof Airliner)
			resultString="Airliner";
		else if(object instanceof Firefighting)
			resultString="Firefighting plane";
		else if(object instanceof Airplane)
			resultString="Airplane";
		else if(object instanceof Helicopter)
			resultString="Helicopter";
		else if(object instanceof UnmannedAircraft)
			resultString="Unmanned aircraft";
		
		return resultString;
		
			
			
	}
}
