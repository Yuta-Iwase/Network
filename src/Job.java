import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class Job extends JFrame{
	private static final long serialVersionUID = 1L;
	final String gnuplotPath = "D:/Program Files (x86)/gnuplot/bin/wgnuplot.exe";
	int progress;

	// 直接打ち込む手法
	final void run(ArrayList<Object> controlParameterList){
//		job(controlParameterList);

		init();

		// ジョブを実行
		progress=0;
		works=1;
		progressLabel.setText("Job: " + progress + "/" + works);
		repaint();
		job(controlParameterList);
		System.exit(0);
	}

	// iniファイルを読み込む手法
	final void run(String ini_FilePath){
		init();

		Scanner scan;
		Scanner currentLine;
		try{
			// ファイルをload
			scan = new Scanner(new File(ini_FilePath));
			scan.nextLine();

			// 変数をiniファイルから読み込み
			ArrayList<ArrayList<Object>> controlParameterMatrix = new ArrayList<ArrayList<Object>>();
			while(scan.hasNextLine()){
				currentLine = new Scanner(scan.nextLine());
				currentLine.useDelimiter(" " + "|" + "\t" + "|" + ",");// |は「または」の意味

				ArrayList<Object> currentControlParameterList = new ArrayList<Object>();
				while(currentLine.hasNext()){
					currentControlParameterList.add(currentLine.next());
				}
				controlParameterMatrix.add(currentControlParameterList);
			}

			// ジョブを実行
			progress=0;
			works=controlParameterMatrix.size();
			for(int i=0;i<controlParameterMatrix.size();i++){
				progressLabel.setText("Job: " + progress + "/" + works);
				repaint();
				job(controlParameterMatrix.get(i));
				progress++;
			}
		}catch(FileNotFoundException e){
			System.out.println(e);
		}
		System.exit(0);
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
	 * ワークディレクトリを返すコマンドセット
	 */
	public final String workDirectory(){
		return System.getProperty("user.dir").replaceAll("\\\\", "/");
	}

	/*
	 *
	 */
	public final void plotControlParameter(String filePath_Relative, ArrayList<String> parameterLabels, ArrayList<Object> controlParameterList){
		try{
			PrintWriter pw = new PrintWriter(new File(filePath_Relative));
			for(int i=0;i<parameterLabels.size();i++){
				pw.print(parameterLabels.get(i) + "\t");
			}
			pw.println();
			for(int i=0;i<parameterLabels.size();i++){
				pw.print(controlParameterList.get(i) + "\t");
			}
			pw.close();
		}catch(Exception e){}
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

	/*
	 * フォルダ作成メソッド
	 */
	public final void makeFolder(String folderName){
		new File(folderName).mkdirs();
	}

	/*
	 * gui定義用<br>
	 * gifは「http://nlst1.blog.fc2.com/blog-entry-219.html」でマジメなやつが取れます。<br>
	 */
	private JLabel progressLabel;
	private int works;
	private void init(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(getClass().getSimpleName());
		setResizable(false);
		setSize(165, 190);
		setLayout(null);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = screenSize.width;
		setLocation((int)(w*0.7), 100);


		JLabel img = new JLabel();
		if(new File("pola.gif").exists()){
			img.setIcon(new ImageIcon("pola.gif"));
		}else if(new File("src/pola.gif").exists()){
			img.setIcon(new ImageIcon("src/pola.gif"));
		}else if(new File("D://java/Network/src/pola.gif").exists()){
			img.setIcon(new ImageIcon("D://java/Network/src/pola.gif"));
		}else if(new File("/Users/Owner/eclipse/Network/src/pola.gif").exists()){
			img.setIcon(new ImageIcon("/Users/Owner/eclipse/Network/src/pola.gif"));
		}else if(new File("/home/yuta/workspace/Network/src/pola.gif").exists()){
			img.setIcon(new ImageIcon("/home/yuta/workspace/Network/src/pola.gif"));
		}
		img.setLayout(null);
		img.setBounds(0, 10, 155, 117);
		add(img);

		progress=0;
		works=0;
		progressLabel = new JLabel();
		progressLabel.setText("Job: " + progress + "/" + works);
		progressLabel.setBounds(50, 130, 100, 30);
		add(progressLabel);

		JPanel bg = new JPanel();
		bg.setBackground(Color.WHITE);
		bg.setBounds(0, 0, 1500, 1500);
		add(bg);

		setVisible(true);
	}


}
