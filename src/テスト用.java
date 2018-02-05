public class テスト用{
	public static void main(String[] args) throws Exception{
		int N = 10000;
		Network net;

//		do {
//			MakePowerLaw dist = new MakePowerLaw(N, 2.7, 2, N/10);
//			net = new ConfigrationNetwork(dist.degree,100,false);
//		}while(!net.success);

//		do {
//			MakePoisson dist = new MakePoisson(N, 3.84406);
//			net = new ConfigrationNetwork(dist.degree,100,false);
//		}while(!net.success);
//
//		PrintWriter pw = new PrintWriter(new File("C:\\desktop\\rnd.csv"));
//		for(int i=0;i<net.degree.length;i++) {
//			System.out.println(net.degree[i]);
//			pw.println(net.degree[i]);
//		}
//		pw.close();
		
		
		MakePoisson dist1,dist2;
		
		do {
			dist1 = new MakePoisson(N, 3.6);
			dist2 = new MakePoisson(N, 0.2);
			net = new ClusteringConfigrationNetwork(dist1.degree, dist2.degree, 100,false);
		}while(!net.success);
		net.printList();
		net.printList("C:\\desktop\\clu.csv");
		System.out.println();
		System.out.println("edges:" + net.M);
		System.out.println("average_degree=" + (2.0*net.M)/net.N);
		System.out.println("s_average=" + ave(dist1.degree));
		System.out.println("t_average=" + ave(dist2.degree));
//		System.out.println();


	}
	
	static double ave(int[] list) {
		int r = 0;
		for(int i=0;i<list.length;i++) {
			r += list[i];
		}
		return (double)r / list.length;
	}
}
