public class テスト用{
	public static void main(String[] args) throws Exception{
		int N = 1000;
		int d = 5;
		int[] degree = new int[N];
		for(int i=0;i<N;i++) degree[i]=d;
		
		ConfigrationNetwork net = new ConfigrationNetwork(degree, 100);
		
		net.sort();
		net.printList();
		net.printList("RegularRandom_N1000degree5.csv");

	}
}
