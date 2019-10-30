package application;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class WarningObject implements Serializable {
	
	private String description;
	private String vrijeme;
	private int row;
	private int column;
	
	public WarningObject(String description,int row,int column) {
		this.description=description;
		this.column=column;
		this.row=row;
		//System.out.println(LocalDateTime.now().toString());
		vrijeme=LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).toString().replace("T", " ").replace("Z", " ");
		vrijeme=vrijeme.substring(0,vrijeme.lastIndexOf("."));
		
	}

	public String getDescription() {
		return description;
	}

	public String getVrijeme() {
		return vrijeme;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setVrijeme(String vrijeme) {
		this.vrijeme = vrijeme;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	
	

}
