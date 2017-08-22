public class テスト用{
	int[] a ;


	public static void main(String[] args) throws Exception{
		NetworkForCSVFile net = new NetworkForCSVFile("WorldAir_w.csv",false,true,false,false);
		net.setNode(false);
//		new AirportNetworkTransformer().makeUndirectedEdge(net);
		net.setEdge();

		double[] nullAtri = new double[net.M];
		for(int i=0;i<net.M;i++) {
			nullAtri[i] = 0.0;
		}
		
		net.LinkSalience();
		double[] salience = new double[net.M];
		for(int i=0;i<net.M;i++) {
			salience[i] = net.edgeList.get(i).linkSalience;
		}

		// gexf出力(複数回行う場合は、最後のネットワークを代表にする。)
		GEXFStylePrinter gexf = new GEXFStylePrinter(net.N, net.list, false,"network.gexf");
		gexf.init_1st();
		gexf.printNode_2nd(null, null, new int[0]);
		gexf.printEdge_3rd(net.weight, "Salience", salience);
		gexf.terminal_4th();


	}
}
