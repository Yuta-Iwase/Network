import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest25_1_trimodal_RandomGraph extends Job {

	public static void main(String[] args) {
		AirportTest25_1_trimodal_RandomGraph job = new AirportTest25_1_trimodal_RandomGraph();
		job.run("param.ini");

	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		// 変数読み込み(パラメータ8つ)
		int inputIndex = 0;
		int times = Integer.parseInt(controlParameterList.get(inputIndex++).toString());
		int N = Integer.parseInt(controlParameterList.get(inputIndex++).toString());
		double w_S = Double.parseDouble(controlParameterList.get(inputIndex++).toString());
		double w_M = Double.parseDouble(controlParameterList.get(inputIndex++).toString());
		double w_L = Double.parseDouble(controlParameterList.get(inputIndex++).toString());
		double p_w_S = Double.parseDouble(controlParameterList.get(inputIndex++).toString());
		double p_w_M;
		double p_w_L = Double.parseDouble(controlParameterList.get(inputIndex++).toString());
		p_w_M = 1.0 -(p_w_S+p_w_L);
		double p = Double.parseDouble(controlParameterList.get(inputIndex++).toString());

		controlParameterList.add(6,p_w_M);

		// オブジェクト定義
		HistogramPloter hist = new HistogramPloter();
		Network net = null;

		// 集計用配列
		int[][] resultFrequency_s = new int[N+1][2];
		for(int i=0;i<resultFrequency_s.length;i++) {
			resultFrequency_s[i][0] = i;
			resultFrequency_s[i][1] = 0;
		}
		// その回での度数
		int[][] currentFrequency;

		// その回でのsalince
		int[] salience = null;

		// 回数処理
		for(int t=0;t<times;t++) {
			// Network生成
//			MakePowerLaw dist;
//			do{
//				dist = new MakePowerLaw(N, 2.7, 2, N-1);
//				net = new ConfigrationNetwork(dist.degree, 50);
//			}while(!net.success);
//			System.out.println("生成完了");
			net = new RandomNetwork(N, p);

			// weight割り振り
			net.weight = new double[net.M];
			double r;
			double width = Math.pow(10, -6);
			for(int i=0;i<net.M;i++){
				r = Math.random();
				if(r < p_w_S){
					net.weight[i] = w_S+width*(Math.random()-0.5);
				}else if(r<p_w_S+p_w_M){
					net.weight[i] = w_M+width*(Math.random()-0.5);
				}else{
					net.weight[i] = w_L+width*(Math.random()-0.5);
				}
			}

			// salience計算
			net.setNode(false);
			net.setEdge();
			net.LinkSalience();
			salience = new int[net.M];
			for(int i=0;i<net.M;i++){
				salience[i] = net.edgeList.get(i).linkSalience;
			}

			hist.load(salience);
			currentFrequency = hist.returnIntFrequency(0, N);
			for(int i=0;i<currentFrequency.length;i++) {
				resultFrequency_s[i][1] += currentFrequency[i][1];
			}
		}

		// 出力
		try{
			// フォルダ設定
			String folderName = String.format("%.3f", p_w_S);
			folderName += ("_" + String.format("%.3f", p_w_M));
			folderName += ("_" + String.format("%.3f", p_w_L));
			String folderPath = folderName + "/";
			makeFolder(folderName);

			// パラメータ出力
			String paramLabel_file = folderPath + "paramList.txt";
			ArrayList<String> parameterLabels = new ArrayList<String>();
			parameterLabels.add("times");
			parameterLabels.add("N");parameterLabels.add("w_S");parameterLabels.add("w_M");parameterLabels.add("w_L");
			parameterLabels.add("p_w_S");parameterLabels.add("(p_w_M)");parameterLabels.add("p_w_L");
			parameterLabels.add("p");
			plotControlParameter(paramLabel_file, parameterLabels, controlParameterList);

			// salienceの度数をプロット
			String frequencyName = "salience_frequency.txt";
			PrintWriter frequencyFile = new PrintWriter(new File(folderPath+frequencyName));
			for(int i=0;i<resultFrequency_s.length;i++) {
				if(resultFrequency_s[i][1]>0) {
					frequencyFile.println(resultFrequency_s[i][0] + "\t" + resultFrequency_s[i][1]);
				}
			}
			frequencyFile.close();

			// ヒストグラム作成
			String histgramName = "salience_gnuplot.txt";
			hist.plot_BoxesStyle(resultFrequency_s, 50 ,folderPath+histgramName);

			// コマンド作成
			String[] command ={
					gnuplot_cd_Relative(folderName),
					gnuplot_plot(("[0:"+(N+1)+"]"),histgramName, "boxes"),
					gnuplot_terminalPostscript(),
					gnuplot_outputMathod("salience.eps"),
					gnuplot_terminalPNG(),
					gnuplot_outputMathod("salience.png")
			};
			String commandName = "command.gplot";
			make_gplot(folderPath+commandName, command);

			// gexf出力(複数回行う場合は、最後のネットワークを代表にする。)
			GEXFStylePrinter gexf = new GEXFStylePrinter(net.N, net.list, false, folderPath+"network.gexf");
			gexf.init_1st();
			gexf.printNode_2nd(null, null, new int[0]);
			gexf.printEdge_3rd(net.weight, "Salience", salience);
			gexf.terminal_4th();

			// コマンド起動
			execution_gnuplot(folderPath+commandName);
		}catch(Exception e){
			System.out.println(e);
		}

	}

}
