package simulator;

import java.io.File;
import java.io.FileInputStream;

import java.util.Properties;

public class Map {
	
	private static  int n;
	private static  int m;
	public static Field[][] matrix;
	

	
	
	public Map() {
		Properties properties=new Properties();
		try {
			FileInputStream inputStream=new FileInputStream(System.getProperty("user.home")+ File.separator+"eclipse-workspace"+ File.separator+ "AirTrafficControl2019"+ File.separator+ "resources"+File.separator+ "config.properties");
			properties.load(inputStream);
			n=Integer.parseInt(properties.getProperty("rows"));
			m=Integer.parseInt(properties.getProperty("columns"));
			inputStream.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		matrix=new Field[n][m];
		for(int i=0;i<n;i++)
			for(int j=0;j<m;j++) {
				matrix[i][j]=new Field();
			}
	
		
	}
	




	public static int getNumberRows()  {
		return n;
		
	}
	
	public static int getNumberColumns() {
		return m;
	}

	


}
