import java.util.ArrayList;

public class AirportTest23_2_WeightTrimodal_OnRandom extends Job{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		AirportTest23_1_WeightTrimodal_OnConfig job = new AirportTest23_1_WeightTrimodal_OnConfig();

		// エクスポート用
		job.run("param.ini");

		// 直接実行用
//		ArrayList<Object> param = new ArrayList<Object>();
//		int N = 1000;				param.add(N);
//		double w_s = 1.1;			param.add(w_s);
//		double w_l = 100.0;			param.add(w_l);
//		double p_w_s = 0.80;			param.add(p_w_s);
//		double p_w_l = 0.19;			param.add(p_w_l);
//		job.run(param);
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		// 変数読み込み(パラメータ5つ)
		int N = Integer.parseInt(controlParameterList.get(0).toString());
		double w_s = Double.parseDouble(controlParameterList.get(1).toString());
		double w_l = Double.parseDouble(controlParameterList.get(2).toString());
		double p_w_s = Double.parseDouble(controlParameterList.get(3).toString());
		double p_w_l = Double.parseDouble(controlParameterList.get(4).toString());
		double gamma_w = -Math.log(p_w_l/p_w_s)/Math.log(w_l/w_s);
		controlParameterList.add(5,gamma_w);
		double p = Double.parseDouble(controlParameterList.get(6).toString());

		double p_w_ll = 1.0 - (p_w_s+p_w_l);
		double w_ll = Math.pow(p_w_ll*Math.pow(w_s, -gamma_w)/p_w_s, 1/(-gamma_w));
		controlParameterList.add(3,w_ll);
		controlParameterList.add(6,p_w_ll);


		// Network生成
//		MakePowerLaw dist;
//		ConfigrationNetwork net;
//		do{
//			dist = new MakePowerLaw(N, 2.7, 2, N-1);
//			net = new ConfigrationNetwork(dist.degree, 50);
//		}while(!net.success);
//		System.out.println("生成完了");
		RandomNetwork net = new RandomNetwork(N, p);

		// weight割り振り
		net.weight = new double[net.M];
		double r;
		for(int i=0;i<net.M;i++){
			r = Math.random();
			if(r < p_w_s){
				net.weight[i] = w_s-0.5+Math.random();
			}else if(r<p_w_s+p_w_l){
				net.weight[i] = w_l-0.5+Math.random();
			}else{
				net.weight[i] = w_ll-0.5+Math.random();
			}
		}

		// salience計算
		net.setNode(false);
		net.setEdge();
		net.LinkSalience();
		double[] salience = new double[net.M];
		for(int i=0;i<net.M;i++){
			salience[i] = net.edgeList.get(i).linkSalience;
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
			parameterLabels.add("N");parameterLabels.add("w_s");parameterLabels.add("w_l");parameterLabels.add("(w_ll)");
			parameterLabels.add("p_w_s");parameterLabels.add("p_w_l");parameterLabels.add("(p_w_ll)");
			parameterLabels.add("(gamma_w)");
			parameterLabels.add("p");
			plotControlParameter(paramLabel_file, parameterLabels, controlParameterList);

			// ヒストグラム作成
			String histgramName = "salience.txt";
			HistogramPloter hist = new HistogramPloter();
			hist.load(salience);
			hist.double_plot(50, true, false, folderPath+histgramName);

			// コマンド作成
			String[] command ={
					gnuplot_cd_Relative(folderName),
					gnuplot_plot(("[0:"+N+"]"),histgramName, "boxes"),
					gnuplot_terminalPostscript(),
					gnuplot_outputMathod("salience.eps"),
					gnuplot_terminalPNG(),
					gnuplot_outputMathod("salience.png")
			};
			String commandName = "command.gplot";
			make_gplot(folderPath+commandName, command);

			// gexf出力
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
