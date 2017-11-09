public class テスト用{
	static int[] a ;


	public static void main(String[] args) throws Exception{
		int M;
		int N = 50;

		RandomNetwork net = new RandomNetwork(N, 1.0);
		M = net.M;

		int minWeight = 1;
		int maxWeight = 100;
		MakePowerLaw dist = new MakePowerLaw(M, 1.5, minWeight, maxWeight);


		net.weighted = true;
		net.weight = new double[M];
		for(int i=0;i<M;i++) {
			net.weight[i] = dist.degree[i];
		}

		net.setNode(false);
		net.setEdge();
		net.CircuitReinforcedRandomWalk2(1000001, 2.0, -1, true,false);

		int remEdge = 0;
		for(int i=0;i<M;i++) {
			if(net.weight[i] <= 1.0E-6) {
				remEdge++;
			}
		}
		System.out.println(M - remEdge);
	}
}
