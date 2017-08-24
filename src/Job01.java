import java.io.File;
import java.io.PrintWriter;

public class Job01 {
	public static void main(String[] args) throws Exception{
		NetworkForCSVFile net = new NetworkForCSVFile("WorldAir/WorldAir_w.csv",false,true,false,false);
		net.setNode(false);
//		new AirportNetworkTransformer().makeUndirectedEdge(net);
		net.setEdge();
		
		net.EdgeBetweenness();
		double[] bcList = new double[net.M];
		for(int i=0;i<net.M;i++) bcList[i]=net.edgeList.get(i).betweenCentrality;
		HistogramPloter hist = new HistogramPloter();
		
		double[][] weight_frq = hist.logclasePlot(net.weight, 50);
		double[][] bc_frq = hist.logclasePlot(bcList, 50);
		
		new File("WorldAir/a/").mkdir();
		PrintWriter pw1 = new PrintWriter(new File("WorldAir/a/worldair_weightFrequency")); 
		PrintWriter pw2 = new PrintWriter(new File("WorldAir/a/worldair_BCFrequency"));
		
		for(int i=0;i<weight_frq.length;i++) {
			pw1.println(weight_frq[i][0] + "\t" + weight_frq[i][1]);
			pw2.println(bc_frq[i][0] + "\t" + bc_frq[i][1]);
		}
		
		pw1.close();
		pw2.close();
	}
}
