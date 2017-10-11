public class テスト用{
	static int[] a ;


	public static void main(String[] args) throws Exception{
		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv",false,true,true,true);
		net.setNode();
		new AirportNetworkTransformer().makeUndirectedEdge(net);
		net.setEdge();

		net.MinimumSpanningTree(false);
		for(int i=0;i<net.MST_Nodes.size();i++) {
			System.out.println(net.MST_Nodes.get(i));
		}

		System.out.println();
		System.out.println(net.MST_Nodes.size());

	}
}
