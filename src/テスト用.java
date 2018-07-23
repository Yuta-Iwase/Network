public class テスト用{
	public static void main(String[] args) throws Exception{
		Network net = new NetworkForCSVFile("FloridaEcosystem.csv", false, true, false, false);
		net.setNode(false);
		net.setEdge();
//
//		double[] realWeight = new double[net.M];
//		for(int i=0;i<net.M;i++) {
//			realWeight[i] = net.weight[i];
//		}
//
//		net.turnUniform();
//		net.EdgeBetweenness();
//		double[] b = new double[net.M];
//		for(int i=0;i<net.M;i++) {
//			b[i] = net.edgeList.get(i).betweenCentrality;
//		}
//
//		net.SetWeight_to_Alpha(0.5);
//		double[] weight_05 = new double[net.M];
//		for(int i=0;i<net.M;i++) {
//			weight_05[i] = net.weight[i];
//		}
//		net.turnUniform();
//
//		net.SetWeight_to_Alpha(1.0);
//		double[] weight_10 = new double[net.M];
//		for(int i=0;i<net.M;i++) {
//			weight_10[i] = net.weight[i];
//		}
//		net.turnUniform();
//
//		net.SetWeight_to_Alpha(1.5);
//		double[] weight_15 = new double[net.M];
//		for(int i=0;i<net.M;i++) {
//			weight_15[i] = net.weight[i];
//		}
//		net.turnUniform();
//
//
//		for(int i=0;i<net.M;i++) {
//			System.out.println(realWeight[i] + "\t" + b[i] + "\t" + weight_05[i] + "\t" + weight_10[i] + "\t" + weight_15[i]);
//		
//		
//		}
		
		net.printList();

	}
}
