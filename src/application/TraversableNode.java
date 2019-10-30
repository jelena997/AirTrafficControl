package application;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TraversableNode {
	
	int i,j;
	TextField textField;
	
	 public TraversableNode(String type,int i,int j)
	    {
		  this.i=i;
	        this.j=j;
	        textField=new TextField(" "+type);
	        textField.setPrefHeight(20);
			textField.setPrefWidth(60);
	  
	    }
	
	 
	 
	  public void setTextField(String type)
	  {
		  if("MP".equals(type)) {
			  javafx.application.Platform.runLater(()-> {
				  
				  textField.setText(type);
				  textField.setStyle("-fx-text-inner-color: blue;");}); 
		  }
		 
		  else if("Hu".equals(type)) {
                 javafx.application.Platform.runLater(()-> {
				  
				  textField.setText(type);
				  textField.setStyle("-fx-text-inner-color: green;");}); 
			  
		  }
		  else {
		  javafx.application.Platform.runLater(()-> {
			  
		  textField.setText(type);
		  textField.setStyle("-fx-text-inner-color: red;");});
		 
	  }
	  }
	  
	  public static String findNameAndSetText(String type) {

		  String name;
		  if("Transport plane".equals(type)) {
			  name="TP";
		  }
		  else if("Airliner".equals(type)) {
			  name="A";
		  }
		  else if("Firefighting plane".equals(type)) {
			  name="FP";
		  }
		  else if("Unmanned Aircraft".equals(type)) {
			  name="UA";
		  }
		  else if("Helicopter".equals(type)) {
			  name="H";
		  }
		  else if("Transport helicopter".equals(type)) {
			  name="TH";
		  }
		  else if("Helicopter airliner".equals(type)) {
			  name="HA";
		  }
		  else if("Firefighting helicopter".equals(type)) {
			  name="FH";
		  }
		  else if("Military plane".equals(type)) {
			  name="MP";
		  }
		  else if("Hunter".equals(type)) {
			  name="Hu";
		  }
		  
		  else {
			  name="";
		  }
		  
		 return name;
	  }
}
