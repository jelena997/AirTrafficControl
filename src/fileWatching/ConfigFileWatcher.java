package fileWatching;


import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import exceptionHandler.LoggerClass;
import simulator.Simulator;

public class ConfigFileWatcher extends Thread{
	private static int oldValueForeignVariable=0;
	private static int oldValueHostVariable=0;
	private Path dir;
	private  LoggerClass logger;
	
	public ConfigFileWatcher() {
		 dir = Paths.get(System.getProperty("user.home")+ File.separator+"eclipse-workspace"+ File.separator+ "AirTrafficControl2019"+ File.separator+ "resources");
		logger=new LoggerClass(ConfigFileWatcher.class.getName());
		 start();
	
	}
	
	
	public void run()
	 {
			
	try
	{
			
	WatchService watcher = FileSystems.getDefault().newWatchService();
	dir.register(watcher,ENTRY_MODIFY);
	

	
    while (true) {
		
    	WatchKey key;
    					
    	try {	
    	key = watcher.take();
    	
    					
    	} catch (InterruptedException ex)
    	 {
    						
    	return;
    					
    	}
    	
    	
	for (WatchEvent<?> event : key.pollEvents()) 
	
	{
						
    WatchEvent.Kind<?> kind = event.kind();
						
	WatchEvent<Path> ev = (WatchEvent<Path>) event;
						
	Path fileName = ev.context();
						
	if(fileName.toString().trim().equals("config.properties") && kind.equals(ENTRY_MODIFY)) {
	
		Properties properties =new Properties();
		FileInputStream input=new FileInputStream(System.getProperty("user.home")+ File.separator+"eclipse-workspace"+ File.separator+ "AirTrafficControl2019"+ File.separator+ "resources"+File.separator+"config.properties");
		properties.load(input);
		int newValueForeignVariable=Integer.parseInt(properties.getProperty("foreignMilitaryObjects"));
	    int newValueHostVariable=Integer.parseInt(properties.getProperty("hostMilitaryObjects"));
		
		if(newValueForeignVariable>0 && newValueForeignVariable!=oldValueForeignVariable) {
			Simulator.setForeignMilitaryVariable(true);
			oldValueForeignVariable=newValueForeignVariable;
			System.out.println("Ima promjena-STRANA");
			
		}
		
		
		if(newValueHostVariable>0 && newValueHostVariable!=oldValueHostVariable) {
			Simulator.setHostMilitaryVariable(true);
			oldValueHostVariable=newValueHostVariable;
		}
		
	input.close();
	
	}
	
    boolean valid = key.reset();
    if (!valid) {
		
   	break;
    		
    	}
	}	
	
   Thread.sleep(3000);
			
    }
	 
    }
   catch (Exception ex)
	 {
				
	   logger.log(Level.WARNING, ex);
			
	}
	 
		
	
	 }
	}

	


 


