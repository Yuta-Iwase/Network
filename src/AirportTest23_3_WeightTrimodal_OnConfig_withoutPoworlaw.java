import java.util.ArrayList;

public class AirportTest23_3_WeightTrimodal_OnConfig_withoutPoworlaw extends Job{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		AirportTest23_3_WeightTrimodal_OnConfig_withoutPoworlaw job = new AirportTest23_3_WeightTrimodal_OnConfig_withoutPoworlaw();

		// エクスポート用
		job.run("param.ini");

		// 直接実行用
//		ArrayList<Object> param = new ArrayList<Object>();
//		int times = 1;					param.add(times);
//		int N = 1000;					param.add(N);
//		double w_s = 1.0;				param.add(w_s);
//		double w_l = 100.0;			param.add(w_l);
//		double w_ll = 10000.0;			param.add(w_ll);
//		double p_w_s = 0.80;			param.add(p_w_s);
//		double p_w_l = 0.19;			param.add(p_w_l);
//		job.run(param);
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		// 変数読み込み(パラメータ7つ)
		int inputIndex = 0;
		int times = Integer.parseInt(controlParameterList.get(inputIndex++).toString());
		int N = Integer.parseInt(controlParameterList.get(inputIndex++).toString());
		double w_s = Double.parseDouble(controlParameterList.get(inputIndex++).toString());
		double w_l = Double.parseDouble(controlParameterList.get(inputIndex++).toString());
		double w_ll = Double.parseDouble(controlParameterList.get(inputIndex++).toString());
		double p_w_s = Double.parseDouble(controlParameterList.get(inputIndex++).toString());
		double p_w_l = Double.parseDouble(controlParameterList.get(inputIndex++).toString());
		double p_w_ll = 1.0 -(p_w_s+p_w_l);
//		double p = Double.parseDouble(controlParameterList.get(inputIndex++).toString());

		controlParameterList.add(7,p_w_ll);

		// オブジェクト定義
		HistogramPloter hist = new HistogramPloter();
		Network net = null;

		// 集計用配列
		int[][] resultFrequency = new int[N+1][2];
		for(int i=0;i<resultFrequency.length;i++) {
			resultFrequency[i][0] = i;
			resultFrequency[i][1] = 0;
		}
		// その回での度数
		int[][] currentFrequency;

		// その回でのsalince
		int[] salience = null;

		// 回数処理
		for(int t=0;t<times;t++) {
			// Network生成
			MakePowerLaw dist;
			do{
				dist = new MakePowerLaw(N, 2.7, 2, N-1);
				net = new ConfigrationNetwork(dist.degree, 50);
			}while(!net.success);
			System.out.println("生成完了");
//			net = new RandomNetwork(N, p);

			// weight割り振り
			net.weight = new double[net.M];
			double r;
			double width = Math.pow(10, -6);
			for(int i=0;i<net.M;i++){
				r = Math.random();
				if(r < p_w_s){
					net.weight[i] = w_s+width*(Math.random()-0.5);
				}else if(r<p_w_s+p_w_l){
					net.weight[i] = w_l+width*(Math.random()-0.5);
				}else{
					net.weight[i] = w_ll+width*(Math.random()-0.5);
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
				resultFrequency[i][1] += currentFrequency[i][1];
			}
		}

		// 出力
		try{
			// フォルダ設定
			String folderName = String.format("%.3f", p_w_s);
			folderName += ("_" + String.format("%.3f", p_w_l));
			folderName += ("_" + String.format("%.3f", p_w_ll));
			String folderPath = folderName + "/";
			makeFolder(folderName);

			// パラメータ出力
			String paramLabel_file = folderPath + "paramList.txt";
			ArrayList<String> parameterLabels = new ArrayList<String>();
			parameterLabels.add("times");
			parameterLabels.add("N");parameterLabels.add("w_s");parameterLabels.add("w_l");parameterLabels.add("w_ll");
			parameterLabels.add("p_w_s");parameterLabels.add("p_w_l");parameterLabels.add("(p_w_ll)");
//			parameterLabels.add("p");
			plotControlParameter(paramLabel_file, parameterLabels, controlParameterList);

			// ヒストグラム作成
			String histgramName = "salience.txt";
			hist.plot_BoxesStyle(resultFrequency, 50 ,folderPath+histgramName);

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

			// gexf出力
			if(times==1) {
				GEXFStylePrinter gexf = new GEXFStylePrinter(net.N, net.list, false, folderPath+"network.gexf");
				gexf.init_1st();
				gexf.printNode_2nd(null, null, new int[0]);
				gexf.printEdge_3rd(net.weight, "Salience", salience);
				gexf.terminal_4th();
			}

			// コマンド起動
			execution_gnuplot(folderPath+commandName);
		}catch(Exception e){
			System.out.println(e);
		}

	}

}
