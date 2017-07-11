import java.io.File;

public class AirportTest21_TeleportTest {

	public static void main(String[] args) throws Exception{
		int N = 1000;
		double deltaW = 0.01;
//		double deltaW = Double.MAX_VALUE;
		int digits = 6;
		int walks = 1;
		for(int i=0;i<digits;i++) walks*=10;
		double teleportP = 0.0;

		// 生成
//		MakePowerLaw dist;
//		ConfigrationNetwork net;
//		do{
//			dist = new MakePowerLaw(N, 2.7, 2, N-1);
//			net = new ConfigrationNetwork(dist.degree, 50);
//		}while(!net.success);
//		System.out.println("生成完了");
		
		double p = 0.0038738738738738738;
		RandomNetwork net = new RandomNetwork(N, p);
		
		


		// RW
		String folderPath = "AirportTest21_TeleportTest/";
		new File("AirportTest21_TeleportTest").mkdirs();
		
		String param = folderPath + "deltaW=" + deltaW + " p=" + teleportP;
		new File(param).mkdir();

		net.setNode(false);
		net.setEdge();

		int[] cntVisit = new int[net.M];

		// テレポート有り
		net.ReinforcedRandomWalk(walks, deltaW, 0, teleportP);
		for(int i=0;i<net.M;i++){
//			System.out.println(i + "," + net.weight[i]);
			cntVisit[i] = (int)((net.weight[i]-1.0)/deltaW)+1; //1加えたのは対数スケールのため
		}
		// ヒストグラム出力
		HistogramPloter hist = new HistogramPloter();
//		folderPath = "[workspace]Histogram/";
		hist.load(cntVisit);
		hist.int_plot(false, false, folderPath+"hist_v.txt");
		hist.load(net.weight);
		hist.double_plot(100, false, false, folderPath+"hist_w.txt");
		
		GEXFStylePrinter gexf = new GEXFStylePrinter(net.N, net.list, false, folderPath+"network.gexf");
		gexf.init_1st();
		gexf.printNode_2nd(null, null, new int[0]);
		gexf.printEdge_3rd(net.weight, "Visit", cntVisit);
		gexf.terminal_4th();
		
		// salienceプロット
		net.LinkSalience();
		double[] s = new double[net.M];
		for(int i=0;i<net.M;i++){
			s[i] = net.edgeList.get(i).linkSalience;
		}
		HistogramPloter h = new HistogramPloter();
		h.load(s);
		h.double_plot(100, true, false, folderPath+"hist_s.txt");

	}

}
