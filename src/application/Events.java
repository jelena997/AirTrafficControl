package application;

import fileWatching.AlertFileWatcher;
import fileWatching.EventFileWatcher;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Events {

	public static void showAllEventsInNewWindow() {
		
		Stage newStage=new Stage();
		newStage.initModality(Modality.APPLICATION_MODAL);
		
		newStage.setTitle("All events in air traffic");
		
		VBox vBox=new VBox(5);
		vBox.setAlignment(Pos.CENTER_LEFT);
		for(String x:EventFileWatcher.getAllEventData())
		{
			Label label=new Label(x);
			label.setStyle("-fx-font-size: 15;");
			vBox.getChildren().add(label);
		}
		
		ScrollPane scrollPane=new ScrollPane(vBox);
	
		BorderPane borderPane=new BorderPane();
		borderPane.setCenter(scrollPane);
		Scene scene=new Scene(borderPane,800,600);
		newStage.setScene(scene);
		newStage.initOwner(GUIController.movingStage);
		newStage.showAndWait();
	}
	
	
	

}
