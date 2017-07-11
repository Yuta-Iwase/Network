import java.io.File;
import java.io.PrintWriter;

public class AirportTest20_TeleportTest {

	public static void main(String[] args) throws Exception{
		int N = 1000;
//		double deltaW = 100.0;
		double deltaW = Double.MAX_VALUE;

		// 生成
		MakePowerLaw dist;
		ConfigrationNetwork net;
		do{
			dist = new MakePowerLaw(N, 2.7, 2, N-1);
			net = new ConfigrationNetwork(dist.degree, 50);
		}while(!net.success);
		System.out.println("生成完了");
//		RandomNetwork net = new RandomNetwork(100, 0.05);

		// RW
		String folderPath = "AirportTest20_TeleportTest/";
		PrintWriter pw1 = new PrintWriter(new File(folderPath + "s_gamma2.7_teleport.csv"));
		PrintWriter pw2 = new PrintWriter(new File(folderPath + "s_gamma2.7.csv"));
		PrintWriter pw3 = new PrintWriter(new File(folderPath + "w_gamma2.7_teleport.csv"));
		PrintWriter pw4 = new PrintWriter(new File(folderPath + "w_gamma2.7.csv"));

		net.setNode(false);
		net.setEdge();

		// テレポート有り
		net.ReinforcedRandomWalk(1000000, deltaW, 0, 0.01);
		net.LinkSalience();
		double[] salience = new double[net.M];
		for(int i=0;i<net.M;i++){
			System.out.println(i + "\t" + net.edgeList.get(i).linkSalience);
			pw1.println(i + "," + net.edgeList.get(i).linkSalience);
			salience[i] = net.edgeList.get(i).linkSalience;
			pw3.println(i + "," + net.weight[i]);
		}
		// ヒストグラム出力
		HistogramPloter hist = new HistogramPloter();
//		folderPath = "[workspace]Histogram/";
		hist.load(net.weight);
		hist.double_plot(100, false, true, folderPath+"hist_w.txt");
		hist.load(salience);
		hist.double_plot(100, true , true, folderPath+"hist_s.txt");

		for(int i=0;i<net.M;i++){
			net.edgeList.get(i).linkSalience = 0;
		}

//		// テレポートなし
//		net.ReinforcedRandomWalk(1000000, deltaW, 0, 0);
//		net.LinkSalience();
//		for(int i=0;i<net.M;i++){
//			System.out.println(i + "\t" + net.edgeList.get(i).linkSalience);
//			pw2.println(i + "," + net.edgeList.get(i).linkSalience);
//			pw4.println(i + "," + net.weight[i]);
//		}



		pw1.close();
		pw2.close();
		pw3.close();
		pw4.close();

	}

}
