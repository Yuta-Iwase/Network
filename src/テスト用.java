public class テスト用{
	public static void main(String[] args) throws Exception{
		int N = 100000;
		MakePowerLaw dist = null;
		int[] a = new int[N];

		int count = 0;
		long timestamp =System.currentTimeMillis();
		Network net = null;
		do {
			dist = new MakePowerLaw(N, 2.5, 1, 1000);
			net = new ClusteringConfigrationNetwork(a, dist.degree, 100);
			System.out.println(count++);
		}while(!net.success);
		System.out.println((System.currentTimeMillis()-timestamp)/1000 + "[s]");

		count = 0;
		timestamp = System.currentTimeMillis();
		int[] twice = new int[N];
		for(int i=0;i<N;i++) twice[i]=dist.degree[i]*2;
		do {
			net = new ConfigrationNetwork(twice, 100, false);
			System.out.println(count++);
		}while(!net.success);
		System.out.println((System.currentTimeMillis()-timestamp)/1000 + "[s]");

	}
}
