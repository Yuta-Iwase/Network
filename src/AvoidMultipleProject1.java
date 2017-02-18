
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;

public class AvoidMultipleProject1 {

	public static void main(String[] args) {
		Scanner scan = null;
		PrintWriter pw = null;
		ArrayList<String> line = new ArrayList<String>();
		String cS;
		Boolean previous;
		try {
			scan = new Scanner(new File("C:\\Users\\Owner\\Desktop\\x04\\S10b-14_BetAport_Roman.csv"));
			pw = new PrintWriter(new File("C:\\Users\\Owner\\Desktop\\x04\\S10b-14_BetAport_Roman_AvoidMultiple.csv"));
			
			scan.nextLine();
			
			while(scan.hasNextLine()){
				cS = scan.nextLine();
				previous = false;
				
				loop2:for(int i=0;i<line.size();i++){
					if(cS.equals(line.get(i))){
						previous = true;
						break loop2;
					}
				}
				
				if(!previous){
					line.add(cS);
				}
			}
			
			for(int i=0;i<line.size();i++){
				pw.println(line.get(i));
			}
			
			
		}catch(Exception e){System.out.println(e);}
		scan.close();
		pw.close();
		
		
	}
}
