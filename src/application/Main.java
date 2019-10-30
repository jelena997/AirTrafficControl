package application;
	
import backup.BackupSystem;
import fileWatching.AlertFileWatcher;
import fileWatching.ConfigFileWatcher;
import fileWatching.EventFileWatcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import radar.RadarSystem;
import simulator.Map;
import simulator.Simulator;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("GUI.fxml"));
			Scene scene = new Scene(root,800,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("AIR TRAFFIC CONTROL 2019");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
	    
		  Map map=new Map();
		   Simulator simulator=new Simulator(map);
		   ConfigFileWatcher watcher=new ConfigFileWatcher();
		   AlertFileWatcher alertWatcher=new AlertFileWatcher();
		   EventFileWatcher eventWatcher=new EventFileWatcher();
		   BackupSystem backupSystem=new BackupSystem();
		   RadarSystem radar=new RadarSystem(map);
		launch(args);
	}
}
