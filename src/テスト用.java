public class テスト用{
	public static void main(String[] args) throws Exception{
		Network net;




		Network net2;
		do {

			MakePoisson dist1 = new MakePoisson(1000, 3.0);
			MakePoisson dist2 = new MakePoisson(1000, 2.0);

			net = new ClusteringConfigrationNetwork(dist1.degree, dist2.degree, 100,false);
			
			dist1 = new MakePoisson(1000, 8.0);
			net2 = new ConfigrationNetwork(dist1.degree, 100);
		}while(!net.success);

//		net.printList();
		net.printList("C:\\Desktop\\csv.csv");

		net.setNode(false);
		net2.setNode(false);

		System.out.println("aa");
		int t = 100;
		for(int i=0;i<t;i++) {
			double f = i/(t*1.0);
			net.SitePercolation2018(f, true);
			net.ConnectedCompornentNDI(true, false, false);
//			System.out.println(net.maxCC_NID);
			
			net2.SitePercolation2018(f, true);
			net2.ConnectedCompornentNDI(true, false, false);
//			System.out.println(net2.maxCC_NID);
			
			System.out.println(f + "\t" + net.maxCC_NID + "\t" + net2.maxCC_NID);
		}




	}
}
