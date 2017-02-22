import java.util.ArrayList;
import java.util.Scanner;

public class AirportTest17_1_5_PropertyPloter {

	public static void main(String[] args) {
		Scanner scan = new Scanner("USairport500_weighted.csv");
		
		ArrayList<Double> weightList = new ArrayList<Double>();
		
		while(scan.hasNext()){
			scan.next();
			scan.next();
			weightList.add(scan.nextDouble());
		}
		
		for(int i=0;i<weightList.size();i++){
			System.out.println(i + "\t" + weightList.get(i));
		}
		
		scan.close();

	}

}
