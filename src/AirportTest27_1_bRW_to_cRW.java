import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

// RRW、テレポ付き、disturbあり

public class AirportTest27_1_bRW_to_cRW extends Job{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		AirportTest27_1_bRW_to_cRW job = new AirportTest27_1_bRW_to_cRW();
//		job.run("param.ini");

		ArrayList<Object> list = new ArrayList<Object>();
		list.add(86*1000);	list.add(1.0);	list.add(0);	list.add(2.001);
		job.run(list);

	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		int index=0;
		int step = Integer.parseInt(controlParameterList.get(index++).toString());
		double alpha = Double.parseDouble(controlParameterList.get(index++).toString());
		int tryNum = Integer.parseInt(controlParameterList.get(index++).toString());
		double divider = Double.parseDouble(controlParameterList.get(index++).toString());
		boolean uniform = Boolean.parseBoolean(controlParameterList.get(index++).toString());
		

//		NetworkForCSVFile net = new NetworkForCSVFile("WorldAir_w.csv",false,true,false,false);
//		net.setNode(false);
//		net.setEdge();

		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv",false,true,true,true);
		net.setNode();
		new AirportNetworkTransformer().makeUndirectedEdge(net);
		net.setEdge();

		HistogramPloter hist = new HistogramPloter();

		double N = net.N;
		double averageDegree = net.averageDegree();
		controlParameterList.add(0,N);
		controlParameterList.add(1,averageDegree);

		// 集計用配列
		int[][] resultFrequency = new int[net.N+1][2];
		for(int i=0;i<resultFrequency.length;i++) {
			resultFrequency[i][0] = i;
			resultFrequency[i][1] = 0;
		}
		ArrayList<Double> resultWeight = new ArrayList<Double>();
		// その回での度数
		int[][] currentFrequency;

		// その回でのsalience
		int[] salience = null;

//		int step = net.N*1000;

		// 計算領域
		net.turnUniform();
//		net.BiasedRandomWalk(step, 1.0, alpha, 0.0, true);
		int start = (int)(Math.random()*net.N);
		System.out.println("cRW mae");
//		net.CircuitReinforcedRandomWalk(tryNum, deltaW, start, false, true);
		net.CircuitReinforcedRandomWalk2(tryNum, divider, start, true);
		System.out.println("salience keusann mae");
		net.LinkSalience();
		System.out.println("salience keusann ato");

		for(int i=0;i<net.M;i++) {
			resultWeight.add(net.weight[i]);
		}


		salience = new int[net.M];
		for(int i=0;i<net.M;i++){
			salience[i] = net.edgeList.get(i).linkSalience;
		}

		hist.load(salience);
		currentFrequency = hist.returnIntFrequency(0, net.N);
		for(int i=0;i<currentFrequency.length;i++) {
			resultFrequency[i][1] += currentFrequency[i][1];
		}




		// 出力
		try{
			// フォルダ設定
			String folderName = null;
			folderName = "StandOut_alpha=" + alpha + "_divider=" + divider;
			String folderPath = folderName + "/";
			makeFolder(folderName);

			// パラメータ出力
			String paramLabel_file = folderPath + "paramList.txt";
			ArrayList<String> parameterLabels = new ArrayList<String>();
			parameterLabels.add("N");parameterLabels.add("<k>");
			parameterLabels.add("step");parameterLabels.add("alpha");
			parameterLabels.add("tryNum");parameterLabels.add("deltaW");
			plotControlParameter(paramLabel_file, parameterLabels, controlParameterList);

			// 辺の各パラメータを出力
			String property_file = folderPath + "property.txt";
			PrintWriter pw = new PrintWriter(new File(property_file));
			net.EdgeBetweenness();
			for(int i=0;i<net.M;i++) {
//				pw.println(i + "\t" + net.edgeList.get(i).linkSalience + "\t" + Math.round(net.weight[i]));
				pw.println(i + "\t" + net.edgeList.get(i).linkSalience + "\t" + Math.round(net.weight[i]) + "\t" + (int)net.edgeList.get(i).betweenCentrality);
			}
			pw.close();

			// ヒストグラム作成
			String histgramName = "salience.txt";
			hist.plot_BoxesStyle(resultFrequency, 50 ,folderPath+histgramName);

			// weight分布生成
			String distributionName = "weight_dist.txt";
			hist.arrayList_double_plot(resultWeight, 50, false, true, folderPath+distributionName);

			// weight-salience相関図
			double maxWeight = -1.0;
			for(int i=0;i<net.M;i++){
				if(net.weight[i]>maxWeight){
					maxWeight = net.weight[i];
				}
			}
			String cor = "correlation.txt";
			PrintWriter pw2 = new PrintWriter(new File(folderPath + cor));
			for(int i=0;i<net.M;i++){
				pw2.println(net.edgeList.get(i).linkSalience/(double)net.N + "\t" + net.weight[i]/maxWeight);
			}
			pw2.close();

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

			// コマンド生成
			String[] command3 ={
					gnuplot_cd_Relative(folderName),
					gnuplot_plot(cor, "p"),
					gnuplot_terminalPostscript(),
					gnuplot_outputMathod("correlation.eps"),
					gnuplot_terminalPNG(),
					gnuplot_outputMathod("correlation.png")
			};
			String commandName3 = "command3.gplot";
			make_gplot(folderPath+commandName3, command3);


			// gexf出力
			GEXFStylePrinter gexf = new GEXFStylePrinter(net.N, net.list, false, folderPath+"network.gexf");
			gexf.init_1st();
			gexf.printNode_2nd(null, null, new int[0]);
			gexf.printEdge_3rd(net.weight, "Salience", salience);
			gexf.terminal_4th();

			// コマンド起動
			execution_gnuplot(folderPath+commandName1);
			execution_gnuplot(folderPath+commandName2);
			execution_gnuplot(folderPath+commandName3);
		}catch(Exception e){
			System.out.println(e);
		}





	}
}
