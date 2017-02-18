import java.io.PrintWriter;

public class AirportTest10_BCEachNode {
	public static void main(String[] args) throws Exception{
		GephiNetwork net = new GephiNetwork("S10b-14_BetAport_GephiPlot.csv",false);
		net.setNode();
		net.betweenCentrality();
		
		String fileName = "BCEachNode";
		PrintWriter pw = new PrintWriter(fileName);
		
		System.out.println();
		
		for(int i=0;i<net.N;i++){
			pw.println(i + "\t" + net.nodeList.get(i).betweenCentrality);
			System.out.println(i + "\t" + net.nodeList.get(i).betweenCentrality);
		}
		
		pw.close();
	}
}
