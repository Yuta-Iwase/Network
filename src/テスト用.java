public class テスト用{
	public static void main(String[] args) throws Exception{
		// ネットワーク構築
		int N=1000;
		double gamma = 2.7;
		Network net;
		do{
			MakePowerLaw dist = new MakePowerLaw(N, gamma, 2, N-1);
			net = new ConfigrationNetwork(dist.degree, 100,false);
		}while(!net.success);
		net.setNode(false);
		net.setEdge();
		
		net.SetWeight_to_Alpha(1.0, N*100);
		for(int i=0;i<net.edgeList.size();i++) {
			int index = net.edgeList.get(i).index;
			int node1 = net.edgeList.get(i).node[0];
			int node2 = net.edgeList.get(i).node[1];
			System.out.println(i + "\t" + net.weight[i] + "\t" + net.degree[node1] + "\t" + net.degree[node2]);
		}
		
	}
}
