package dyfusion;

public class GSLineCalculator {

	
	public static double calculateGAlpha(double temp){
		double a = (double)-77/(double)18500;
		double b = (double) 17556/(double)4625;
		double y = (a * temp) + b;
		return y;	
	}
	
}
