package fileWatching;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.logging.Level;

import application.WarningObject;
import exceptionHandler.LoggerClass;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class EventFileWatcher extends Thread {

	private static Path dir;
	private static LoggerClass logger;
	public static Label bottomLabel;
	private static String resultString="";

	public EventFileWatcher() {

		dir = Paths.get(System.getProperty("user.home") + File.separator + "eclipse-workspace" + File.separator+ "AirTrafficControl2019" + File.separator + "events");
		logger = new LoggerClass(AlertFileWatcher.class.getName());
		start();

	}

	public void run() {

		try {

			WatchService watcher = FileSystems.getDefault().newWatchService();
			dir.register(watcher, ENTRY_MODIFY);

			while (true) {

				WatchKey key;

				try {
					key = watcher.take();

				} catch (InterruptedException ex) {

					return;

				}

				for (WatchEvent<?> event : key.pollEvents()) {
				
					File lastFile = AlertFileWatcher.lastFileModified(dir.toFile().getAbsolutePath());

					resultString = "";
					try {
						BufferedReader reader = new BufferedReader(new FileReader(lastFile));
						String lineString = "";
						while ((lineString = reader.readLine()) != null)
							resultString += lineString;
					} catch (Exception ex) {
						logger.log(Level.WARNING, ex);
					}
					javafx.application.Platform.runLater(() -> {
						bottomLabel.setText("Dogadjaj: "+resultString);
					});
				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		} catch (

		Exception ex) {
			logger.log(Level.WARNING, ex);
		}

	}
	
	public static ArrayList<String> getAllEventData()
	{
		ArrayList<String> arrayList=new ArrayList<String>();
		
		File fl = new File(dir.toFile().getAbsolutePath());
		File[] files = fl.listFiles(f-> f.isFile());
		
		for(File file:files)
		{
			try(BufferedReader reader = new BufferedReader(new FileReader(file)))
			{
				String lineString = "";
				while ((lineString = reader.readLine()) != null && !lineString.isEmpty())
					resultString += lineString;
				arrayList.add(resultString);
				resultString="";
			}catch (Exception e) {
				logger.log(Level.WARNING, e);
			}
		}
		return  arrayList;
		
	}

}
