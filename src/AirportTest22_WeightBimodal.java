import java.util.ArrayList;

public class AirportTest22_WeightBimodal extends Job{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		AirportTest22_WeightBimodal job = new AirportTest22_WeightBimodal();
		job.run("param.ini");
//		ArrayList<Object> param = new ArrayList<Object>();
//		param.add(1000);	param.add(0.5);	param.add(0.004);
//		job.run(param);
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		// 変数読み込み
		int N = Integer.parseInt(controlParameterList.get(0).toString());
		double a = Double.parseDouble(controlParameterList.get(1).toString());
		double p = Double.parseDouble(controlParameterList.get(2).toString());

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
		for(int i=0;i<net.M;i++){
			if(Math.random() < a){
				net.weight[i] = 0.5+Math.random();
			}else{
				net.weight[i] = 99.5+Math.random();
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
			String folderName = String.format("%.3f", a);
			String folderPath = folderName + "/";
			makeFolder(folderName);

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
