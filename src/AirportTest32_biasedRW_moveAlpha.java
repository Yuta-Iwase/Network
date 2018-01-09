import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest32_biasedRW_moveAlpha extends Job{

	public static void main(String[] args) {
		AirportTest32_biasedRW_moveAlpha job = new AirportTest32_biasedRW_moveAlpha();
		job.run("param.ini");

//		ArrayList<Object> list = new ArrayList<Object>();
//		list.add(1);	list.add(1.0);
//		job.run(list);
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		int index = 0;
		int times = Integer.parseInt(controlParameterList.get(index++).toString());
		double alpha = Double.parseDouble(controlParameterList.get(index++).toString());
		int step = Integer.parseInt(controlParameterList.get(index++).toString());

		// ネットワーク生成用パラメータ
		int N = 1000;
		double gamma = 2.7;

		// パラメータリスト追加
		double averageDegree = new MakePowerLaw(N,gamma).averageDegree();
		controlParameterList.add(0,N);
		controlParameterList.add(1,averageDegree);
		controlParameterList.add(2,gamma);

		// 集計用
		double sumHS = 0.0;
		double averageHS;
		double currentHS_num;
		int[] currentSalience = null;
		HistogramPloter hist = new HistogramPloter();
		int[][] currentFrequency;
		int[][] resultFrequency = new int[N+1][2];
		for(int i=0;i<resultFrequency.length;i++) {
			resultFrequency[i][0] = i;
			resultFrequency[i][1] = 0;
		}
		ArrayList<Double> resultWeight = new ArrayList<Double>();


		// 処理
		MakePowerLaw dist;
		Network net = null;
		for(int t=0;t<times;t++){
			// 生成
			do{
				dist = new MakePowerLaw(N, 2.7);
				net = new ConfigrationNetwork(dist.degree, 100);
			}while(!net.success);
			net.setNode(false);
			net.setEdge();
			// 計算
			net.BiasedRandomWalk(step, 1.0, alpha, 0.0, true);
			net.LinkSalience();
			// 集計
			currentSalience = new int[net.M];
			currentHS_num = 0.0;
			for(int i=0;i<net.M;i++){
				if(net.edgeList.get(i).linkSalience>=N*0.9){
					currentHS_num++;
				}
				currentSalience[i] = net.edgeList.get(i).linkSalience;
				resultWeight.add(net.weight[i]);
			}
			currentHS_num /= (double)net.M;
			sumHS += currentHS_num;

			hist.load(currentSalience);
			currentFrequency = hist.returnIntFrequency(0, net.N);
			for(int i=0;i<currentFrequency.length;i++) {
				resultFrequency[i][1] += currentFrequency[i][1];
			}
		}
		averageHS = sumHS/(double)times;


		// 出力
		try{
			// フォルダ設定
			String folderName = null;
			folderName = "biasedRW_alpha=" + alpha;
			String folderPath = folderName + "/";
			makeFolder(folderName);

			// パラメータ出力
			String paramLabel_file = folderPath + "paramList.txt";
			ArrayList<String> parameterLabels = new ArrayList<String>();
			parameterLabels.add("N");parameterLabels.add("<k>");
			parameterLabels.add("gamma");
			parameterLabels.add("times");parameterLabels.add("alpha");
			parameterLabels.add("step");
			plotControlParameter(paramLabel_file, parameterLabels, controlParameterList);

			// 辺の各パラメータを出力
			String property_file = folderPath + "property.txt";
			PrintWriter pw = new PrintWriter(new File(property_file));
			pw.println("※最後の1回が代表してプロットされています。");
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

			// HSの割合を出力
			PrintWriter pw3 = new PrintWriter(new File(folderPath + "averageHS_alpha" + alpha + ".txt"));
			pw3.println(alpha + "\t" + averageHS);
			pw3.close();

			// w,bc,s,degreeVSstrength のリストデータ出力
			PrintWriter prW  = new PrintWriter(new File(folderPath + "list_weight" + ".txt"));
			PrintWriter prBC = new PrintWriter(new File(folderPath + "list_BC" + ".txt"));
			PrintWriter prS  = new PrintWriter(new File(folderPath + "list_salience" + ".txt"));
			PrintWriter prVS  = new PrintWriter(new File(folderPath + "list_degreeVSstrength" + ".txt"));
			double[] strengthList = new double[net.N];
			for(int i=0;i<net.N;i++) {
				double currentStrength = 0.0;
				for(int j=0;j<net.nodeList.get(i).eList.size();j++) {
					currentStrength += Math.round(net.weight[net.nodeList.get(i).eList.get(j).index]);
				}
				strengthList[i] = currentStrength;
				prVS.println(net.degree[i] + "," + strengthList[i]);
			}
			for(int i=0;i<net.M;i++) {
				prW.println(Math.round(net.weight[i]));
				prBC.println(net.edgeList.get(i).betweenCentrality);
				prS.println(net.edgeList.get(i).linkSalience);
			}
			prW.close(); prBC.close(); prS.close(); prVS.close();

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
			gexf.printEdge_3rd(net.weight, "Salience", currentSalience);
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
