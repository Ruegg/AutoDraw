package bugpatch.master;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class MarkupSort {

	public HashMap<Integer, String> titleOrder = new HashMap<Integer,String>();
	
	public HashMap<String, String> titleAndData = new HashMap<String,String>();
	
	public String shape = "";
	
	public String largeTitle = "";
	public void addData(String data, String title){
		String currentData = titleAndData.get(title);
		if(!currentData.equals("")){
			currentData = currentData + "!" + data;
		}else{
			currentData = currentData+data;
		}
		titleAndData.put(title, currentData);
	}
	
	public void setLargeTitle(String title){
		largeTitle = title;
	}
	
	public void addTitle(String title){
		int entryNumber = titleOrder.size()+1;
		titleOrder.put(entryNumber, title);
		titleAndData.put(title, "");
	}
	
	public void setShape(String shape){
		this.shape = shape;
	}
	
	public String exportMarkup(){
		String markup = "";
		for(int x =1;x != titleOrder.size()+1;x++){
			String curTitle = titleOrder.get(x);
			markup = markup + "<br><b>" + curTitle + "</b>";
			String data = titleAndData.get(curTitle);
			if(data.contains("!")){
				String[] splitData = data.split("\\!");
				for(String curData : Arrays.asList(splitData)){
					markup = markup + "<br>" + curData;
				}
			}else{
				markup = markup + "<br>" + data;
			}
		}
		if(!largeTitle.equals("")){
			markup = "<center><b>" + largeTitle + "</b></center>" + markup;
		}
		markup = "<html>" + markup + "</html>";
		if(!shape.equals("")){
			markup = shape + "!" + markup;
		}
		return markup;
	}
}
