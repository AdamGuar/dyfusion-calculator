import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import output.console.CellsPrinter;
import output.csv.*;

public class DyfusionCalculator {

	// Constants
	// Problem constants
	public static final int Q = 140000;
	public static final double R = 8.3144;
	public static final double D0 = 0.000041;

	// Program constants
	public static final int ARRAY_SIZE = 100;
	public static final int INITIAL_HIGH_CARBON_SIZE = 6;
	public static final int INITIAL_KSI = INITIAL_HIGH_CARBON_SIZE + 1;

	public static void main(String[] args) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		CellsPrinter cp = new CellsPrinter();
		
		
		

		double cellsArr[] = new double[ARRAY_SIZE];
		Arrays.fill(cellsArr, 0.02);

		for (int i = 0; i < INITIAL_HIGH_CARBON_SIZE; i++) {
			cellsArr[i] = 0.67;
		}

		System.out.println("Initial cells:");
		cp.setCellsToPrint(cellsArr);
		cp.printCells();

		double cellsArrNextStep[] = new double[ARRAY_SIZE];

		System.out.print("Type number of time steps:");
		int timeSteps;

		try {
			timeSteps = Integer.parseInt(br.readLine());
		} catch (NumberFormatException e) {
			timeSteps = 0;
			e.printStackTrace();
		} catch (IOException e) {
			timeSteps = 0;
			e.printStackTrace();
		}
		
		double initialTemperature = 0 ;
		System.out.println("Type initial temperature of process");
		try {
			initialTemperature = Double.parseDouble(br.readLine());
		} catch (NumberFormatException e) {
			initialTemperature  = 0;
			e.printStackTrace();
		} catch (IOException e) {
			initialTemperature  = 0;
			e.printStackTrace();
		}
		
		
		double deltaTemperature = 0 ;
		System.out.println("Type delta of temperature(incrementation of temperature for every time step, zero means constant temperature:");
		try {
			deltaTemperature = Double.parseDouble(br.readLine());
		} catch (NumberFormatException e) {
			deltaTemperature  = 0;
			e.printStackTrace();
		} catch (IOException e) {
			deltaTemperature  = 0;
			e.printStackTrace();
		}
		

		int ksi = INITIAL_KSI;
		// double cGammaAlpha = 0.35;
		List<CSVModel> csvList = new ArrayList<CSVModel>();
		
		double TEMP = initialTemperature;
		for (int k = 0; k < timeSteps; k++) {
			// Problem variables
			double TEMP_K = 273 + TEMP;
			double D = D0 * Math.exp(-Q / (R * TEMP_K)) * Math.pow(10, 10);
			double deltaX = 0.1;
			double deltaXPower2 = deltaX * deltaX;
			double deltaTime = ((0.5) * deltaXPower2) / D;
			double FRACTION = (D * deltaTime) / deltaXPower2;
			cellsArrNextStep = cellsArr;
			
			for (int j = 0; j < ksi; j++) {
				double cellPlusOne;
				double cellMinusOne;

				if (j == 0)
					cellMinusOne = cellsArr[j];
				else
					cellMinusOne = cellsArr[j - 1];
				if (j == ksi - 1)
					cellPlusOne = cellsArr[j];
				else
					cellPlusOne = cellsArr[j + 1];

				cellsArrNextStep[j] = ((1 - (2 * FRACTION)) * cellsArr[j]) + (FRACTION * (cellMinusOne + cellPlusOne));

				double cGammaAlpha = GSLineCalculator.calculateGAlpha(TEMP);
				if (cellsArrNextStep[ksi - 1] >= cGammaAlpha) {
					ksi = ksi + 1;
				}

			}
			cellsArr = cellsArrNextStep;
			double cellsArrCopy[] = deepArrCopy(cellsArr);
			csvList.add(new CSVModel(k + 1, (k + 1) * deltaTime, cellsArrCopy));
			System.out.println("Step: " + (k + 1) + " Time: " + ((k + 1) * deltaTime));
			cp.setCellsToPrint(cellsArr);
			cp.printCells();
		TEMP = TEMP + deltaTemperature;	
		}

		CSVGenerator csvGen = new CSVGenerator();
		csvGen.setCellsToCSV(csvList);
		csvGen.generateCSV(null);

	}

	private static double[] deepArrCopy(double arrToCopy[]){
		double result[] = new double[arrToCopy.length];
		
		for(int i = 0 ; i < arrToCopy.length;i++){
			result[i] = arrToCopy[i];
		}
		
		return result;
	}
	
}
