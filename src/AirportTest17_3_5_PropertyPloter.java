import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class AirportTest17_3_5_PropertyPloter {

	public static void main(String[] args) throws Exception{
		Scanner scan = new Scanner(new File("Florida_weighted.csv"));
		
		ArrayList<Double> weightList = new ArrayList<Double>();
		
		while(scan.hasNext()){
			scan.next();
			scan.next();
			weightList.add(scan.nextDouble());
		}
		
		for(int i=0;i<weightList.size();i++){
			System.out.println(weightList.get(i));
		}
		
		scan.close();
		

	}

}
