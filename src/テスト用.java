public class テスト用{
	public static void main(String[] args) throws Exception{
		int N = 1000;
//		double gamma = 2.7;
//		int k_min = 2;
//
//		int times = 100;
//
//
//		double a = k_min*(gamma-3.0);
//		int[] degreeDist = new int[N];
//		for(int t=0;t<times;t++) {
//			DMSNetwork net = new DMSNetwork(N, k_min, a, 100);
//			for(int i=0;i<net.N;i++) {
//				degreeDist[net.degree[i]]++;
//			}
//		}
//		double inv_divider = 1.0/(N*times);
//		for(int i=0;i<degreeDist.length;i++) {
//			if(degreeDist[i]>0) {
//				System.out.println(i + "\t" + degreeDist[i]*inv_divider);
//			}
//		}

		int[] degree = new int[N];
		for(int i=0;i<N;i++) {
			degree[i] = 5;
		}
		Network net = new ConfigrationNetwork(degree, 100);
		System.out.println(net.success);

	}
}
