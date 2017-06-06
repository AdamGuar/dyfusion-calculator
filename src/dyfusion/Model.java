package dyfusion;

import console.CellsPrinter;
import output.csv.CSVModel;

public class Model {
	private double cellsArr[];
	private double coalSum;
	
	
	public double[] getCellsArr() {
		return cellsArr;
	}
	public void setCellsArr(double[] cellsArr) {
		this.cellsArr = cellsArr;
	}
	public double getCoalSum() {
		return coalSum;
	}
	public void setCoalSum(double coalSum) {
		this.coalSum = coalSum;
	}
	public Model(double[] cellsArr, double coalSum) {
		super();
		this.cellsArr = cellsArr;
		this.coalSum = coalSum;
	}
	

	public void calculateCellsArr(double temperature,double deltaTemperature,int timeSteps){
		int ksi = Utils.INITIAL_KSI;
		double TEMP = temperature;
		double cellsArrNextStep[] = new double[Utils.ARRAY_SIZE];
		double cGammaAlpha = GSLineCalculator.calculateGAlpha(TEMP);
		cellsArrNextStep[ksi] = cGammaAlpha;
		double cellsArrCopy[] = deepArrCopy(cellsArr);
		double coalDelta = 0;
		double coalCurrent= 0 ;
		double coalPrevious = calculateCoalSum(cellsArr);
		double q = Utils.Q;
		double r = Utils.R;
		CellsPrinter cp = new CellsPrinter();
		for (int k = 0; k < timeSteps; k++) {
			// Problem variables
			double TEMP_K = 273 + TEMP;
			double D = Utils.D0 * Math.exp(-q / (r * TEMP_K)) * Math.pow(10, 10);

			coalCurrent = calculateCoalSum(cellsArr);
			coalDelta = coalCurrent - coalPrevious;
			
			System.out.println("C increase in step: " + coalDelta);
			
			double deltaXPower2 = Utils.deltaX * Utils.deltaX;
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
			//csvList.add(new CSVModel(k + 1, (k + 1) * deltaTime, cellsArrCopy));
			System.out.println("Step: " + (k + 1) + " Time: " + ((k + 1) * deltaTime));
			cp.setCellsToPrint(cellsArr);
			/*cp.printCells();
			austeniteMap.put(String.valueOf(((k + 1) * deltaTime)),
					String.valueOf(calcAustenite(deepArrCopy(cellsArr))));*/
			TEMP = TEMP + (deltaTemperature * deltaTime);
			coalPrevious = coalCurrent;
		}
		
		
		
		
	}
	
	private static double calculateCoalSum(double cells[]){
		double sum=0;
		for(int i = 0; i < cells.length;i++){
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
