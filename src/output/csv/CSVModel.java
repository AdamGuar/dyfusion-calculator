package output.csv;

public class CSVModel {

	private double step;
	private double time;
	private double[] cellsArr;
	public double getStep() {
		return step;
	}
	public void setStep(double step) {
		this.step = step;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double[] getCellsArr() {
		return cellsArr;
	}
	public void setCellsArr(double[] cellsArr) {
		this.cellsArr = cellsArr;
	}
	public CSVModel(double step, double time, double[] cellsArr) {
		this.step = step;
		this.time = time;
		this.cellsArr = cellsArr;
	}

	

}
