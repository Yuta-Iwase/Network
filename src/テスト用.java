public class テスト用{
	public static void main(String[] args) throws Exception{

		int N = 1000;
		double gamma = 2.7;
		int minDegree = 2;
		int maxDegree = N/10;
		int swapTime = 100_000;
		int loopLimit = 1000;
		int step = 1_000_000;
		double alpha = 1.0;
		boolean positiveAssotative = false;
		double assThreshold = 0.4;


		ScaleFreeNetwork net;
		do {
			net = new ScaleFreeNetwork(N, gamma, minDegree, maxDegree, loopLimit);
			net.setNeightbor();
			net.degreeCorrelationCoefficient();
			for(int i=0;i<swapTime;i++){
				net.degreeCorrelationCoefficient_forSwapping(positiveAssotative, loopLimit);
				if(positiveAssotative ?
						net.degreeCorrelationCoefficient >= assThreshold :
							net.degreeCorrelationCoefficient <= -assThreshold
							) break;
			}
		}while(positiveAssotative ?
				net.degreeCorrelationCoefficient >= assThreshold :
					net.degreeCorrelationCoefficient <= -assThreshold);


		System.out.println(net.degreeCorrelationCoefficient);
		net.printList("c:\\desktop\\dis.csv");

//		net.setNode(false);
//		net.setEdge();
//		net.BiasedRandomWalk(step, 1.0, alpha, 0.0, true);
//		int[][] edgeList = net.list;
//		int[] degreeList = net.degree;
//		int[] mean_w = new int[N*N];
//		int[] mean_w_elements = new int[N*N];
//		for(int i=0;i<net.M;i++) {
//			int left = edgeList[i][0];			int right = edgeList[i][0];
//			int degree0 = degreeList[left];	int degree1 = degreeList[right];
//
//			double currentWeight = net.weight[i];
//
//			mean_w[degree0*degree1] += currentWeight;
//			mean_w_elements[degree0*degree1]++;
//		}
//		for(int i=0;i<mean_w.length;i++) {
//			if(mean_w[i]>0) {
//				System.out.println(i + "\t" + mean_w[i]/(double)mean_w_elements[i]);
//			}
//		}




	}

}
