import java.util.ArrayList;

// RRW、テレポ付き、disturbあり

public class AirportTestXX_RetryPoster01 extends Job{

	public static void main(String[] args) {
		AirportTestXX_RetryPoster01 job = new AirportTestXX_RetryPoster01();
		job.run("param.ini");

	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		int index=0;
		int times = Integer.parseInt(controlParameterList.get(index++).toString());
		int N = Integer.parseInt(controlParameterList.get(index++).toString());
		double deltaW = Double.parseDouble(controlParameterList.get(index++).toString());
		double teleportP = Double.parseDouble(controlParameterList.get(index++).toString());


		HistogramPloter hist = new HistogramPloter();
		Network net = null;

		// 集計用配列
		int[][] resultFrequency = new int[N+1][2];
		for(int i=0;i<resultFrequency.length;i++) {
			resultFrequency[i][0] = i;
			resultFrequency[i][1] = 0;
		}
		ArrayList<Double> resultWeight = new ArrayList<Double>();
		// その回での度数
		int[][] currentFrequency;

		// その回でのsalince
		int[] salience = null;

		for(int t=0;t<times;t++) {
			do {
				MakePowerLaw dist = new MakePowerLaw(N, 2.7, 2, N-1);
				net = new ConfigrationNetwork(dist.degree, 100);
			}while(!net.success);
			net.setNode(false);
			net.setEdge();

			int step = N*1000;
			net.ReinforcedRandomWalk(step, deltaW, teleportP, true);
			net.LinkSalience();

			for(int i=0;i<net.M;i++) {
				resultWeight.add(net.weight[i]);
			}


			salience = new int[net.M];
			for(int i=0;i<net.M;i++){
				salience[i] = net.edgeList.get(i).linkSalience;
			}

			hist.load(salience);
			currentFrequency = hist.returnIntFrequency(0, N);
			for(int i=0;i<currentFrequency.length;i++) {
				resultFrequency[i][1] += currentFrequency[i][1];
			}
		}



		// 出力
		try{
			// フォルダ設定
			String folderName = "delta_W" + String.format("%.1f", deltaW);
			folderName += ("_teleportP" + String.format("%.3f", teleportP));
			String folderPath = folderName + "/";
			makeFolder(folderName);

			// パラメータ出力
			String paramLabel_file = folderPath + "paramList.txt";
			ArrayList<String> parameterLabels = new ArrayList<String>();
			parameterLabels.add("times");
			parameterLabels.add("N");parameterLabels.add("deltaW");parameterLabels.add("teleportP");
			plotControlParameter(paramLabel_file, parameterLabels, controlParameterList);

			// ヒストグラム作成
			String histgramName = "salience.txt";
			hist.plot_BoxesStyle(resultFrequency, 50 ,folderPath+histgramName);

			// weight分布生成
			String distributionName = "weight_dist.txt";
			hist.arrayList_double_plot(resultWeight, 50, false, true, folderPath+distributionName);

			// コマンド作成
			String[] command1 ={
					gnuplot_cd_Relative(folderName),
					gnuplot_plot(("[0:"+(N+1)+"]"),histgramName, "boxes"),
					gnuplot_terminalPostscript(),
					gnuplot_outputMathod("salience.eps"),
					gnuplot_terminalPNG(),
					gnuplot_outputMathod("salience.png")
			};
			String commandName1 = "command1.gplot";
			make_gplot(folderPath+commandName1, command1);

			// コマンド作成
			String[] command2 ={
					gnuplot_cd_Relative(folderName),
					gnuplot_plot(distributionName, "lp"),
					gnuplot_terminalPostscript(),
					gnuplot_outputMathod("weight.eps"),
					gnuplot_terminalPNG(),
					gnuplot_outputMathod("weight.png")
			};
			String commandName2 = "command2.gplot";
			make_gplot(folderPath+commandName2, command2);

			// gexf出力
			GEXFStylePrinter gexf = new GEXFStylePrinter(net.N, net.list, false, folderPath+"network.gexf");
			gexf.init_1st();
			gexf.printNode_2nd(null, null, new int[0]);
			gexf.printEdge_3rd(net.weight, "Salience", salience);
			gexf.terminal_4th();

			// コマンド起動
			execution_gnuplot(folderPath+commandName1);
			execution_gnuplot(folderPath+commandName2);
		}catch(Exception e){
			System.out.println(e);
		}





	}
}
