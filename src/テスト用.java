public class テスト用{
	public static void main(String[] args) throws Exception{
//		int times = 1000;
//		double dcc_mean = 0.0;
//		
//		for(int i=0;i<times;i++) {
//			int N = 1000;
//			double gamma = 2.7;
//			int minDegree = 2;
//			int maxDegree = N/10;
//			int loopLimit = 100; 
//			
//			ScaleFreeNetwork net = new ScaleFreeNetwork(N, gamma, minDegree, maxDegree, loopLimit);
//			net.degreeCorrelationCoefficient();
//			
//			dcc_mean += net.degreeCorrelationCoefficient;
//		}
//
//		dcc_mean /= times;
//		System.out.println(dcc_mean);
		
		
		
		
		
		int N = 1000;
		double gamma = 2.7;
		int minDegree = 2;
		int maxDegree = N/10;
		int loopLimit = 100; 
		
		ScaleFreeNetwork net = new ScaleFreeNetwork(N, gamma, minDegree, maxDegree, loopLimit);
		net.setNeightbor();
		net.degreeCorrelationCoefficient();
		
		System.out.println("start");
		for(int i=0;i<100000;i++) {
			System.out.println(i);
			net.degreeCorrelationCoefficient_forSwapping(false);
		}
		System.out.println(net.degreeCorrelationCoefficient);
	}

}
