package application;


import java.util.ArrayList;

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

public class Crashes {
	
	public static void showAllCrashesInNewWindow() {
		Stage newStage=new Stage();
		newStage.setTitle("All crashes in air traffic");
		newStage.initModality(Modality.APPLICATION_MODAL);
		VBox vBox=new VBox(5);
		vBox.setAlignment(Pos.CENTER_LEFT);
		for(WarningObject x:AlertFileWatcher.getAllObjectsDeserialized())
		{
			Label label=new Label(x.getDescription() + " na poziciji ["+x.getRow()+"] ["+ x.getColumn()+"] u "  + x.getVrijeme());
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
