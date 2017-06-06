import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dyfusion.GSLineCalculator;
import output.console.CellsPrinter;
import output.csv.*;

public class DyfusionCalculator {

	// Constants
	// Problem constants
	public static final int Q = 140000;
	public static final double R = 8.3144;
	public static final double D0 = 0.000041;
	public static final double deltaX = 0.1;

	// Program constants
	public static final int ARRAY_SIZE = 100;
	public static final int INITIAL_LOW_CARBON_SIZE = 2;
	public static final int INITIAL_KSI = INITIAL_LOW_CARBON_SIZE;

	public static void main(String[] args) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		CellsPrinter cp = new CellsPrinter();

		double cellsArr[] = new double[ARRAY_SIZE];
		Arrays.fill(cellsArr, 0.1);

		for (int i = 0; i < INITIAL_LOW_CARBON_SIZE; i++) {
			cellsArr[i] = 0.02;
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

		double initialTemperature = 0;
		System.out.println("Type initial temperature of process");
		try {
			initialTemperature = Double.parseDouble(br.readLine());
		} catch (NumberFormatException e) {
			initialTemperature = 0;
			e.printStackTrace();
		} catch (IOException e) {
			initialTemperature = 0;
			e.printStackTrace();
		}

		double deltaTemperature = 0;
		System.out.println("Type heating speed (0 means const temperature): ");
		try {
			deltaTemperature = Double.parseDouble(br.readLine());
		} catch (NumberFormatException e) {
			deltaTemperature = 0;
			e.printStackTrace();
		} catch (IOException e) {
			deltaTemperature = 0;
			e.printStackTrace();
		}


		int ksi = INITIAL_KSI;
		List<CSVModel> csvList = new ArrayList<CSVModel>();
		double cellsArrCopy[] = deepArrCopy(cellsArr);
		csvList.add(new CSVModel(0, 0, cellsArrCopy));
		Map<String, String> austeniteMap = new HashMap<String, String>();
		double TEMP = initialTemperature;

		double cGammaAlpha = GSLineCalculator.calculateGAlpha(TEMP);
		cellsArrNextStep[ksi] = cGammaAlpha;

		double coalDelta = 0;
		double coalCurrent = 0;
		double coalPrevious = calculateCoalSum(cellsArr);

		for (int k = 0; k < timeSteps; k++) {
			// Problem variables
			double TEMP_K = 273 + TEMP;
			double D = D0 * Math.exp(-Q / (R * TEMP_K)) * Math.pow(10, 10);

			coalCurrent = calculateCoalSum(cellsArr);
			coalDelta = coalCurrent - coalPrevious;

			System.out.println("C increase in step: " + coalDelta);

			double deltaXPower2 = deltaX * deltaX;
			double deltaTime = ((0.5) * deltaXPower2) / D;
			double FRACTION = (D * deltaTime) / deltaXPower2;
			cellsArrNextStep = cellsArr;

			for (int j = ksi; j < cellsArr.length; j++) {
				double cellPlusOne;
				double cellMinusOne;

				if (j == ksi)
					cellMinusOne = cellsArr[j];
				else
					cellMinusOne = cellsArr[j - 1];
				if (j == cellsArr.length - 1)
					cellPlusOne = cellsArr[j];
				else
					cellPlusOne = cellsArr[j + 1];

				cellsArrNextStep[j] = ((1 - (2 * FRACTION)) * cellsArr[j]) + (FRACTION * (cellMinusOne + cellPlusOne));

			}
			cGammaAlpha = GSLineCalculator.calculateGAlpha(TEMP);
			cellsArrNextStep[ksi] = cGammaAlpha;
			cellsArr = cellsArrNextStep;
			cellsArrCopy = deepArrCopy(cellsArr);
			csvList.add(new CSVModel(k + 1, (k + 1) * deltaTime, cellsArrCopy));
			System.out.println("Step: " + (k + 1) + " Time: " + ((k + 1) * deltaTime));
			cp.setCellsToPrint(cellsArr);
			cp.printCells();
			austeniteMap.put(String.valueOf(((k + 1) * deltaTime)),
					String.valueOf(calcAustenite(deepArrCopy(cellsArr))));
			TEMP = TEMP + (deltaTemperature * deltaTime);
			coalPrevious = coalCurrent;
		}

		CSVGenerator csvGen = new CSVGenerator();
		csvGen.setCellsToCSV(csvList);
		csvGen.generateCSV(null);
		csvGen.generateCSVAusteniteFraction(null, austeniteMap);
	}

	private static double calculateCoalSum(double cells[]) {
		double sum = 0;
		for (int i = 0; i < cells.length; i++) {
			sum = sum + cells[i];
		}
		return sum;
	}

	private static double[] deepArrCopy(double arrToCopy[]) {
		double result[] = new double[arrToCopy.length];

		for (int i = 0; i < arrToCopy.length; i++) {
			result[i] = arrToCopy[i];
		}

		return result;
	}

	private static double calcAustenite(double cells[]) {
		int counter = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] > 0.02)
				counter++;

		}
		return (double) counter / (double) cells.length;

	}

}
