import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;


public class RandomizeTest {
	public static void main(String[] args) {
		String inputFilePath  = "Listing_American_College_football.csv";
		String outputFilePath = "Randomized_American_College_football.csv";
		Scanner scan=null,cloneScan=null;
		PrintWriter pw = null;;
		String currentLine;
		int M,tabPos;
		int[][] list,randomizedList;
		Randomize project;
		
		try {
			scan = new Scanner(new File(inputFilePath));
			cloneScan = new Scanner(new File(inputFilePath));
			pw = new PrintWriter(new File(outputFilePath));
			
			M=0;
			while(cloneScan.hasNextLine()){
				cloneScan.nextLine();
				M++;
			}
			
			list = new int[M][2];
			for(int i=0;i<M;i++){
				currentLine = scan.nextLine();
				tabPos = currentLine.indexOf("\t");
				list[i][0] = Integer.parseInt(currentLine.substring(0, tabPos));
				list[i][1] = Integer.parseInt(currentLine.substring(tabPos+1, currentLine.length()));
			}
			
			project = new Randomize();
			randomizedList = project.randomize(list, 100, 5000);
			
			for(int i=0;i<M;i++){
				pw.println(randomizedList[i][0] + "\t" + randomizedList[i][1]);
				System.out.println(randomizedList[i][0] + "\t" + randomizedList[i][1]);
			}
			
			scan.close();
			cloneScan.close();
			pw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
