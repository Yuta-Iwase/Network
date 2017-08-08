import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

// RRW、テレポ付き、disturbあり

public class AirportTestXX_RetryPoster02 extends Job{

	public static void main(String[] args) {
		AirportTestXX_RetryPoster02 job = new AirportTestXX_RetryPoster02();
		job.run("param.ini");

//		ArrayList<Object> pList = new ArrayList<Object>();
//		pList.add(1);	pList.add(1000);	pList.add(2);
//		pList.add(2.7);	pList.add(2);		pList.add(0.01);
//		job.run(pList);

	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		int index=0;
		/*
		 * mode
		 * 0:uniform weight
		 * 1:simple RW
		 * 2:biased RW
		 * 3:reinforced RW
		 * 4:original weight
		 */
		int mode = Integer.parseInt(controlParameterList.get(index++).toString());
		int times = Integer.parseInt(controlParameterList.get(index++).toString());
		int N = Integer.parseInt(controlParameterList.get(index++).toString());
		int minDegree = Integer.parseInt(controlParameterList.get(index++).toString());
		double gamma = Double.parseDouble(controlParameterList.get(index++).toString());
		double deltaW = Double.parseDouble(controlParameterList.get(index++).toString());
		double teleportP = Double.parseDouble(controlParameterList.get(index++).toString());


		HistogramPloter hist = new HistogramPloter();
		Network net = null;
		MakePowerLaw dist = new MakePowerLaw(N, gamma, minDegree, N-1);

		double averageDegree = dist.averageDegree();
		controlParameterList.add(4,averageDegree);

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
			net = new RandomNetwork(N, dist);
			net.setNode(false);
			net.setEdge();

			int step = N*1000;
			switch(mode) {
			case 0:
				net.weight = new double[net.M];
				for(int i=0;i<net.M;i++) {
					net.weight[i] = 1.0;
				}
				net.disturb();
				break;
			case 1:
				net.SimpleRandomWalk(step, deltaW, teleportP, true);
				break;
			case 2:
				net.BiasedRandomWalk(step, deltaW, teleportP, true);
				break;
			case 3:
				net.ReinforcedRandomWalk(step, deltaW, teleportP, true);
				break;
			case 4:
				if(!net.weighted) errorReport("mode4:original weight は、重みなしネットワークには使えません。", true);
				break;
			}
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
			String folderName = null;
			switch(mode) {
			case 0:
				folderName = "uniform";
				break;
			case 1:
				folderName = "simple_RW";
				break;
			case 2:
				folderName = "biased_RW_teleportP" + String.format("%.3f", teleportP);
				break;
			case 3:
				folderName = "reinforced_RW_" + "delta_W" + String.format("%.1f", deltaW);
				folderName += ("_teleportP" + String.format("%.3f", teleportP));
				break;
			case 4:
				folderName = "original_weight";
				break;
			}
			String folderPath = folderName + "/";
			makeFolder(folderName);

			// パラメータ出力
			String paramLabel_file = folderPath + "paramList.txt";
			ArrayList<String> parameterLabels = new ArrayList<String>();
			parameterLabels.add("mode");
			parameterLabels.add("times");
			parameterLabels.add("N");parameterLabels.add("minDegree");parameterLabels.add("gamma");
			parameterLabels.add("<k>");
			parameterLabels.add("deltaW");parameterLabels.add("teleportP");
			plotControlParameter(paramLabel_file, parameterLabels, controlParameterList);

			// 辺の各パラメータを出力
			String property_file = folderPath + "property.txt";
			PrintWriter pw = new PrintWriter(new File(property_file));
			net.EdgeBetweenness();
			for(int i=0;i<net.M;i++) {
				pw.println(i + "\t" + net.edgeList.get(i).linkSalience + "\t" + Math.round(net.weight[i]) + "\t" + (int)net.edgeList.get(i).betweenCentrality);
			}
			pw.close();

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
					"set logscale",
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
