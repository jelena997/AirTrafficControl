package application;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import aircraftModels.Aircraft;
import exceptionHandler.LoggerClass;
import fileWatching.EventFileWatcher;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import radar.RadarSystem;
import simulator.Map;
import simulator.Simulator;


public class GUIController {
	
	 public Button startButton;
	 ScrollPane scroolPane=new ScrollPane();
	 GridPane matrix=new GridPane();
	 BorderPane borderPane=new BorderPane();
	 int numberOfRows=Map.getNumberRows();
	 int numberOfColumns=Map.getNumberColumns();
	 TraversableNode[][] traversableMatrix=new TraversableNode[numberOfRows][numberOfColumns];
	 private LoggerClass logger=new LoggerClass(GUIController.class.getName());
	 public static Stage movingStage;
	 NodeContent[][] objects=new NodeContent[numberOfRows][numberOfColumns];
	
	 
	 
	 

	 public void handleStartButtonClick(ActionEvent actionEvent) {
		Label bottomLabel=new Label();
		EventFileWatcher.bottomLabel=bottomLabel;
		bottomLabel.setText("");
		Label label=new Label();
		label.setText("MATRIX OF MOVEMENT");
		VBox leftSide=new VBox();
		Button stopButton=new Button();
		stopButton.setText("STOP");
		stopButton.setPrefSize(120,55);
		Button startButton=new Button("START");
		startButton.setPrefSize(120, 55);
		Button showCrashes=new Button("CRASHES");
		showCrashes.setPrefSize(120, 55);
		Button showEvents=new Button("EVENTS");
		showEvents.setPrefSize(120, 55);
		leftSide.getChildren().addAll(stopButton,startButton,showCrashes,showEvents);
		leftSide.setLayoutY(100);
		leftSide.setSpacing(10);
		
		 stopButton.setOnAction(e-> {
			 Aircraft.stopSimulation=true;
			
			 });
		 
		 startButton.setOnAction(e->{Aircraft.stopSimulation=false; });
		 
		 for(int i=0;i<numberOfRows;i++)
				for(int j=0;j<numberOfColumns;j++) {
					traversableMatrix[i][j]=new TraversableNode("",i,j);
					
				}
		 
		 for(int i=0; i<numberOfRows;i++) 
			 for(int j=0;j<numberOfColumns;j++) {
                matrix.add(traversableMatrix[i][j].textField,j,i);
                 
				 
			 }
		 
		 
		 
		matrix.setGridLinesVisible(true);
		scroolPane.setContent(matrix);
		borderPane.setTop(label);
		borderPane.setLeft(leftSide);
		borderPane.setCenter(scroolPane);
		VBox vBox=new VBox(5);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPrefHeight(50);
		bottomLabel.setStyle("-fx-font-size: 18;");
		vBox.getChildren().add(bottomLabel);
		borderPane.setBottom(vBox);
	
	
		 Scene scene=new Scene(borderPane,1366,768);
		 
		 Stage mainStage=(Stage)( (Button)(actionEvent.getSource())).getScene().getWindow();
		 mainStage.setScene(scene);
		 mainStage.setFullScreen(true);
		 movingStage=mainStage;
		
		showCrashes.setOnAction(event -> Crashes.showAllCrashesInNewWindow());
		showEvents.setOnAction(event -> Events.showAllEventsInNewWindow());
	

		Thread GUIThread=new Thread(()->{
		
			while(true) {
			try {
				
				Thread.sleep(1000);
				synchronized(RadarSystem.monitor)
				{
				 for(int i=0;i<numberOfRows;i++)
						for(int j=0;j<numberOfColumns;j++) {
							objects[i][j]=new NodeContent();
						}
				 
				
			  File file= new File(System.getProperty("user.home")+ File.separator+"eclipse-workspace"+ File.separator+ "AirTrafficControl2019"+ File.separator+"map.txt");
             
				BufferedReader br= new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                String[] parts=line.split("-");
                int row=Integer.parseInt(parts[1]);
                int column=Integer.parseInt(parts[2]);
                objects[row][column].setElement(parts[0]);
                }   
               br.close();
              
				}
				
				 
			}
			
			catch(Exception ex) {
				
				logger.log(Level.WARNING, ex);
			}
		
			
			for(int i=0;i<numberOfRows;++i) 
				for(int j=0;j<numberOfColumns;++j) {
					
							
					if(objects[i][j].getSize()==0)
					{
						
						
						traversableMatrix[i][j].setTextField("");
					}
					else {
						String string="";
						if(objects[i][j].getSize()==1) {
							string=TraversableNode.findNameAndSetText(objects[i][j].getElement(0));
						}
						else {
						for(String s: objects[i][j].content) {
							String name=TraversableNode.findNameAndSetText(s);
							string+=name+" ";
						}
					
						}
						traversableMatrix[i][j].setTextField(string);
						
					}
				
				}
			}
			
			
		});
		
			

		GUIThread.setDaemon(true);
		GUIThread.start();

}
}
	 


	
	 

	 
	 



