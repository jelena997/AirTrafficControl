package fileWatching;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import application.GUIController;
import application.WarningObject;
import exceptionHandler.LoggerClass;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import simulator.Simulator;

public class AlertFileWatcher extends Thread {

	private static Path dir;
	private static LoggerClass logger;

	public AlertFileWatcher() {

		dir = Paths.get(System.getProperty("user.home") + File.separator + "eclipse-workspace" + File.separator
				+ "AirTrafficControl2019" + File.separator + "alert");
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
					File lastFile = lastFileModified(dir.toFile().getAbsolutePath());
					WarningObject object = deserializeObject(lastFile);
					if (object != null) {
						javafx.application.Platform.runLater(() -> {
							Alert alert = new Alert(Alert.AlertType.INFORMATION,
									object.getDescription() + " na poziciji ["+object.getRow()+"] ["+ object.getColumn()+"] u "  + object.getVrijeme() + " !", ButtonType.OK);
							alert.initOwner(GUIController.movingStage);
							alert.showAndWait();
						});
					}
				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		} catch (Exception ex) {

			logger.log(Level.WARNING, ex);

		}

	}

	public static File lastFileModified(String dir) {
		File fl = new File(dir);
		File[] files = fl.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				
				return file.isFile();
			}
		});

		long lastMod = Long.MIN_VALUE;
		File choice = null;
		for (File file : files) {
			if (file.lastModified() > lastMod) {
				choice = file;
				lastMod = file.lastModified();
			}
		}
		return choice;
	}

	public static WarningObject deserializeObject(File file)

	{

		try (FileInputStream fileIn = new FileInputStream(file.getAbsolutePath());

				ObjectInputStream in = new ObjectInputStream(fileIn)) {

			WarningObject object = (WarningObject) in.readObject();

			return object;

		} catch (Exception ex) {
			logger.log(Level.WARNING, ex);
		}
		return null;

	}
	
	public static ArrayList<WarningObject> getAllObjectsDeserialized()
	{
		ArrayList<WarningObject> arrayList=new ArrayList<WarningObject>();
		
		File fl = new File(dir.toFile().getAbsolutePath());
		File[] files = fl.listFiles(f-> f.isFile());
		
		for(File file:files)
			arrayList.add(deserializeObject(file));
		
		return  arrayList;
	}

}
