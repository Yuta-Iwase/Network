public class テスト用{
	public static void main(String[] args) throws Exception{
		Network net;
		
		MakePoisson dist1 = new MakePoisson(1000, 3.0);
		MakePoisson dist2 = new MakePoisson(1000, 2.0);
		
		net = new ClusteringConfigrationNetwork(dist1.degree, dist2.degree, 100);
		
		net.printList();
		
		
	}
}
