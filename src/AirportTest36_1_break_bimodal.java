import java.io.File;
import java.io.PrintWriter;

public class AirportTest36_1_break_bimodal{
	public static void main(String[] args) throws Exception{
		NetworkForCSVFile net = new NetworkForCSVFile("WorldAir_w.csv", false, true);
		net.setNode(false);
		net.setEdge();

		net.EdgeRewiring();
//		net.weightShuffle();

		String f = "test36";
		new File(f).mkdirs();
		PrintWriter pw = new PrintWriter(new File(f+"/s.csv"));
		HistogramGenerator hist = new HistogramGenerator();
		py_PointPlot py = new py_PointPlot();


		net.LinkSalience();

		double[] s_list = new double[net.edgeList.size()];
		for(int i=0;i<s_list.length;i++) s_list[i]=net.edgeList.get(i).linkSalience/(double)net.N;
		double[][] hist_s = hist.binPlot(s_list, 50, false, 0, 1);

		{
			GEXFStylePrinter gexf = new GEXFStylePrinter(net.N, net.list, false, f+"/network.gexf");
			gexf.init_1st();
			int[] nullList = {};
			gexf.printNode_2nd(null, "", nullList);
			gexf.printEdge_3rd(net.weight, "salience", s_list);
			gexf.terminal_4th();
		}

		for(int i=0;i<50;i++) {
			System.out.println(hist_s[i][0] + "," + hist_s[i][1]/(double)net.M);
			pw.println(hist_s[i][0] + "," + hist_s[i][1]);
		}
		pw.close();

		py.plot(f+"/plot_salience.py", "s.csv", "salience", 0, 1, 0, 1, true, "black", false, true, "blue", 4, 0, false, false, "salience dist.", "$salience$", "$p({\\rm salience})$", true, "world air", "upper center");
	}
}
