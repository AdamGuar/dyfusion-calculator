import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import output.console.CellsPrinter;
import output.csv.*;

public class DyfusionCalculator {
	
	
	
	//Constants
	
	
	//Problem constants
	public static final int TEMP = 800;
	public static final int TEMP_K = 273 + TEMP;
	public static final int Q = 140000;
	public static final double R = 8.3144;
	public static final double D0 = 0.000041;
	public static final double D=D0*Math.exp(-Q /(R * TEMP_K))*Math.pow(10,10);
	public static final double deltaX = 0.1;
	public static final double deltaXPower2 = deltaX *deltaX;
	public static final double deltaTime = ((0.5) * deltaXPower2) / D;
	public static final double FRACTION = (D * deltaTime)/deltaXPower2;
	
	
	
	//Program constants
	public static final int ARRAY_SIZE = 100;
	public static final int INITIAL_HIGH_CARBON_SIZE = 6;
	public static final int INITIAL_KSI= INITIAL_HIGH_CARBON_SIZE+1;
	

	public static void main(String[] args) {
		
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	CellsPrinter cp = new CellsPrinter();	
		
	double cellsArr[] = new double [ARRAY_SIZE];
	Arrays.fill(cellsArr, 0.02);
	
	for (int i=0;i<INITIAL_HIGH_CARBON_SIZE;i++){
		cellsArr[i]=0.67;
	}
	
	System.out.println("Initial cells:");
	cp.setCellsToPrint(cellsArr);
	cp.printCells();
	
	
	double cellsArrNextStep[] = new double [ARRAY_SIZE];
	
	System.out.print("Type number of time steps:");
	int timeSteps;
  
    try {
		timeSteps = Integer.parseInt(br.readLine());
	} catch (NumberFormatException e) {
		timeSteps=0;
		e.printStackTrace();
	} catch (IOException e) {
		timeSteps=0;
		e.printStackTrace();
	}
   
	
    
/*	for(int k=0;k<timeSteps;k++){
	
		for (int j=0;j<ARRAY_SIZE;j++){
			double cellPlusOne;
			double cellMinusOne;
			if(j==0) cellMinusOne=cellsArr[j];
			else cellMinusOne=cellsArr[j-1];
			if(j==ARRAY_SIZE-1) cellPlusOne=cellsArr[j];
			else cellPlusOne=cellsArr[j+1];
			
			cellsArrNextStep[j] = ((1 -(2*FRACTION)) * cellsArr[j] ) + (FRACTION*(cellMinusOne+cellPlusOne)); 
		}
	cellsArr = cellsArrNextStep;
	System.out.println("Step: "+(k+1)+" Time: "+((k+1)*deltaTime));
	cp.setCellsToPrint(cellsArr);
	cp.printCells();
	}*/
    
    int ksi = INITIAL_KSI;
   // double cGammaAlpha = 0.35;
    List<CSVModel> csvList = new ArrayList<CSVModel>();
    
    for(int k=0;k<timeSteps;k++){
    	cellsArrNextStep=cellsArr;
		for (int j=0;j<ksi;j++){
			double cellPlusOne;
			double cellMinusOne;
			if(j==0) cellMinusOne=cellsArr[j];
			else cellMinusOne=cellsArr[j-1];
			if(j==ksi-1) cellPlusOne=cellsArr[j];
			else cellPlusOne=cellsArr[j+1];
			
			cellsArrNextStep[j] = ((1 -(2*FRACTION)) * cellsArr[j] ) + (FRACTION*(cellMinusOne+cellPlusOne));
			
			double cGammaAlpha=GSLineCalculator.calculateGAlpha(TEMP);
			if(cellsArrNextStep[ksi-1]>=cGammaAlpha){
				ksi=ksi+1;
			}
			
		}
	cellsArr = cellsArrNextStep;
	csvList.add(new CSVModel(k+1,(k+1)*deltaTime,cellsArr));
	System.out.println("Step: "+(k+1)+" Time: "+((k+1)*deltaTime));
	cp.setCellsToPrint(cellsArr);
	cp.printCells();
	}
    
    CSVGenerator csvGen = new CSVGenerator();
    csvGen.setCellsToCSV(csvList);
    csvGen.generateCSV(null);

	}

}
