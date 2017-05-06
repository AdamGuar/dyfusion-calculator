package output.csv;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
	
}
