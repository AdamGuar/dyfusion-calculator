package output.console;

import java.text.DecimalFormat;

public class CellsPrinter {

	private double cellsToPrint[];

	public double[] getCellsToPrint() {
		return cellsToPrint;
	}

	public void setCellsToPrint(double[] cellsToPrint) {
		this.cellsToPrint = cellsToPrint;
	}
	
	
	
	public void printCells(){
		
		for (int i =0;i<cellsToPrint.length;i++)
		{
			if(cellsToPrint[i]!=0.02)
				System.out.print(new DecimalFormat("#0.000000000000").format(cellsToPrint[i])+" *| ");
			else
				System.out.print(new DecimalFormat("#0.000000000000").format(cellsToPrint[i])+" | ");
			
			
		}
		System.out.println();
		
	}
	
	
}
