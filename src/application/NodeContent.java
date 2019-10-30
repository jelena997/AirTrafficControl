package application;

import java.util.ArrayList;

import aircraftModels.Aircraft;

public class NodeContent {
	
	public ArrayList<String> content;
	
	public NodeContent() {
		content=new ArrayList<String>();
	}

	
	public String getElement(int index) {
		return content.get(index);
	}
	
	public void  setElement(String element) {
		
		//if(!content.contains(element))
		content.add(element);
	}
	
	public int getSize() {
		return content.size();
	}
	
	public void deleteElement(String a) {
		content.remove(a);
	}
}
