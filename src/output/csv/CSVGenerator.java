package output.csv;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.io.FileUtils;

public class CSVGenerator {

	private List<CSVModel> cellsToCSV;
	
	
	
	
	public List<CSVModel> getCellsToCSV() {
		return cellsToCSV;
	}




	public void setCellsToCSV(List<CSVModel> cellsToCSV) {
		this.cellsToCSV = cellsToCSV;
	}




	public void generateCSV(String filename){
		
		if(filename==null) filename = "output.csv";
		
		String output ="sep=','\n";
		
		for(CSVModel entity : cellsToCSV){
			output = output + entity.getStep()+"," + entity.getTime()+",";
			double[] cellsConcentration = entity.getCellsArr();
			for(int i=0;i<cellsConcentration.length;i++){
				output=output+cellsConcentration[i]+",";
			}
			output = output + "\n";
		}
				
		try {
			FileUtils.writeStringToFile(new File(filename), output);
		} catch (IOException e) {
			// TODO Implement exception handling here
			e.printStackTrace();
		}
	}
	
public void generateCSVAusteniteFraction(String filename,Map<String,String> ValueMap){
		
		if(filename==null) filename = "outputAustenite.csv";
		
		String output ="time,austenite[%]\n";
		
		Set<String> keys = ValueMap.keySet();
		
		for(String entity : keys){
			output = output + entity+"," + ValueMap.get(entity);
			output = output + "\n";
		}
				
		try {
			FileUtils.writeStringToFile(new File(filename), output);
		} catch (IOException e) {
			// TODO Implement exception handling here
			e.printStackTrace();
		}
	}
	
	
}
