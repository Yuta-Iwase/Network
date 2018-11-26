public class テスト用{
	static int ii = 0;
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
//		double before_DCC = net.degreeCorrelationCoefficient;
//
//		net.degreeCorrelationCoefficient_forSwapping(true);
//		net.setNeightbor();
//		net.degreeCorrelationCoefficient();
//		double true_DCC = net.degreeCorrelationCoefficient;
//
//		System.out.println("true_dif=" + (true_DCC-before_DCC));
		for(int i=0;i<10000;i++){
			ii = i;
			net.degreeCorrelationCoefficient_forSwapping(true, 1000);
		}
		System.out.println(net.degreeCorrelationCoefficient);

		net.printList("C:\\Users\\Owner\\Desktop\\新しいフォルダー\\net.csv");
	}

}
