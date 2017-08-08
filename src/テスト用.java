public class テスト用 {
	int[] a ;


	public static void main(String[] args) {
//		int times = 500;
//		double generalSum = 0.0;
//		MakePowerLaw dist = null;
//		Network net = null;
//		for(int t=0;t<times;t++) {
//			do {
//				dist = new MakePowerLaw(1000, 2.7);
//				net = new ConfigrationNetwork(dist.degree, 100);
//			}while(!net.success);
//
//			System.out.println(dist.averageDegree());
//			System.out.println();
//
////			for(int i=0;i<dist.c.length;i++) {
////				System.out.println(i + "\t" + dist.c[i]);
////			}
//
////			System.out.println();
////			System.out.println("min:" + dist.minDegree + "\tc[min]=" + dist.c[dist.minDegree]);
////			System.out.println("max;" + dist.maxDegree + "\tc[max]=" + dist.c[dist.maxDegree]);
//
//
//			double sum = 0.0;
//			for(int i=0;i<net.degree.length;i++) {
//				sum += net.degree[i];
//			}
//			double average = ((sum*1.0)/net.degree.length);
//			System.out.println("t=" + t + "\t average=" + average);
//			generalSum += average;
//		}
//
//		System.out.println();
//		System.out.println("result_ave = " + ((double)generalSum)/times);
//		System.out.println("true_ave = " + dist.averageDegree());



//		MakePowerLaw dist = new MakePowerLaw(1000, 2.7);
//		RandomNetwork net = new RandomNetwork(1000, dist);
//		double sum = 0.0;
//		for(int i=0;i<net.degree.length;i++) {
//			sum += net.degree[i];
//		}
//		System.out.println(sum/net.degree.length);
//		System.out.println(dist.averageDegree());
		
		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv",false,true,true,true);
		net.setNode();
		new AirportNetworkTransformer().makeUndirectedEdge(net);
		net.setEdge();
		
		System.out.println(net.weighted);
		
		for(int i=0;i<net.N;i++) {
			System.out.println(i + "\t" + net.weight[i]);
		}

	}
}
