import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Job {
	final String gnuplotPath = "D:/Program Files (x86)/gnuplot/bin/wgnuplot.exe";

	// 直接打ち込む手法
	final void run(ArrayList<Object> controlParameterList){
		job(controlParameterList);
	}

	// iniファイルを読み込む手法
	final void run(String ini_FilePath){
		Scanner scan;
		Scanner currentLine;
		try{
			// ファイルをload
			scan = new Scanner(new File(ini_FilePath));
			scan.nextLine();

			// 変数をiniファイルから読み込み
			ArrayList<ArrayList<Object>> controlParrameterMatrix = new ArrayList<ArrayList<Object>>();
			while(scan.hasNextLine()){
				currentLine = new Scanner(scan.nextLine());
				currentLine.useDelimiter(" " + "|" + "\t" + "|" + ",");// |は「または」の意味

				ArrayList<Object> currentControlParameterList = new ArrayList<Object>();
				while(currentLine.hasNext()){
					currentControlParameterList.add(currentLine.next());
				}
				controlParrameterMatrix.add(currentControlParameterList);
			}

			for(int i=0;i<controlParrameterMatrix.size();i++){
				job(controlParrameterMatrix.get(i));
			}
		}catch(FileNotFoundException e){
			System.out.println(e);
		}
	}

	public abstract void job(ArrayList<Object> controlParameterList);

	/*
	 * gnuplotコマンドセット
	 */
	public final String gnuplot_cd_Absorute(String absotutePath){return ("cd \"" + absotutePath + "\"");}
	public final String gnuplot_cd_Relative(String folderName){return ("cd \"" + workDirectory() + "/" + folderName + "\"");}
	public final String gnuplot_plot(String file, String with){
		return ("plot \"" + file + "\" w " + with);
		}
	public final String gnuplot_plot(String xRange, String file, String with){
		return ("plot " + xRange + " \"" + file + "\" w " + with);
		}
	public final String gnuplot_terminalPostscript(){return "set terminal postscript eps enhanced color";}
	public final String gnuplot_terminalPNG(){return "set terminal png";}
	public final String gnuplot_outputMathod(String output){
		String line1 = "set output\"" + output + "\"";
		String line2 = "replot";
		String line3 = "set output";
		String br = "\r\n";
		return (line1 + br + line2 + br + line3);
	}
	/*
	 * .gplotファイルをcommandListに従い作成します。
	 */
	public final void make_gplot(String filePath_relative, ArrayList<String> commandList){
		try{
			PrintWriter pw = new PrintWriter(new File(filePath_relative));
			for(int i=0;i<commandList.size();i++){
				pw.println(commandList.get(i));
			}
			pw.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	/*
	 * .gplotファイルをcommandListに従い作成します。
	 */
	public final void make_gplot(String filePath_relative, String[] commandList){
		ArrayList<String> commandList_alt = new ArrayList<String>();
		for(int i=0;i<commandList.length;i++){
			commandList_alt.add(commandList[i]);
		}
		make_gplot(filePath_relative, commandList_alt);
	}

	/*
	 * その他コマンドセット
	 */
	public final String workDirectory(){
		return System.getProperty("user.dir").replaceAll("\\\\", "/");
	}

	/*
	 * 与えられた.gplotファイルのパスをgnuplotで実行する
	 */
	public final void execution_gnuplot(String commandFilePath_relative){
		try{
			Runtime.getRuntime().exec(gnuplotPath + " " + commandFilePath_relative);
		}catch(IOException e){
			System.out.println(e);
		}
	}

	public final void makeFolder(String folderName){
		new File(folderName).mkdirs();
	}


}
