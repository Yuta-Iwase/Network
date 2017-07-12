import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class Job {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

	}
	
	final void run(String ini_FilePath){
		Scanner scan;
		Scanner currentLine;
		try{
			scan = new Scanner(new File(ini_FilePath));
			scan.nextLine();
			
			while(scan.hasNextLine()){
				currentLine = new Scanner(scan.nextLine());
				currentLine.useDelimiter(" " + "|" + "¥t" + "|" + ",");// |は「または」の意味
				while(currentLine.hasNext()){
					// add
				}
			}
		}catch(FileNotFoundException e){
			
		}
		
	}
	
	
	public abstract void job();

}
