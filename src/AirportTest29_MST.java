import java.io.File;
import java.io.PrintWriter;

public class AirportTest29_MST {

	public static void main(String[] args) {
		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv",false,true,true,true);
		net.setNode();
		new AirportNetworkTransformer().makeUndirectedEdge(net);
		net.setEdge();

		net.MinimumSpanningTree(false);
		net.LinkSalience();

		String folderName = "test29";
		new File(folderName).mkdirs();
		folderName = folderName + "/";
		try {
			PrintWriter pw1 = new PrintWriter(new File(folderName + "MST_Edges.txt"));
			PrintWriter pw2 = new PrintWriter(new File(folderName + "HSS.txt"));
			PrintWriter pw3 = new PrintWriter(new File(folderName + "salience.txt"));

			for(int i=0;i<net.MST_Edges.size();i++) {
				pw1.println(net.MST_Edges.get(i));
			}
			int hss_N = 0;
			for(int i=0;i<net.M;i++) {
				if(net.edgeList.get(i).linkSalience>0.5*net.N) {
					pw2.println(net.edgeList.get(i).index);
					hss_N++;
				}
				pw3.println(net.edgeList.get(i).linkSalience);
			}

			System.out.println(net.MST_Edges.size());
			System.out.println(hss_N);

			pw1.close();
			pw2.close();
			pw3.close();
		}catch(Exception e) {}

	}

}
