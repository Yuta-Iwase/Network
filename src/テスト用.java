public class テスト用{
	public static void main(String[] args) throws Exception{
		int N = 1000;
		MakePowerLaw dist = new MakePowerLaw(N, 2.7);
//		ConfigrationNetwork net = new ConfigrationNetwork(dist.degree, 100);
//		while(!net.success){
//			dist = new MakePowerLaw(1000, 2.7);
//			net = new ConfigrationNetwork(dist.degree, 100);
//		}
		RandomNetwork net = new RandomNetwork(N, dist.averageDegree()/N);
		
		int[] hist = new int[N+1];
		for(int i=0;i<net.N;i++){
			hist[net.degree[i]]++;
		}
		
		double inv_N = 1.0/N;
		double cum = 0;
		for(int i=0;i<hist.length;i++){
			if(hist[i]>0){
//				{
//					cum += hist[i]*inv_N;
//					System.out.println(i+"\t"+(1.0-cum));
//				}
				
				{
					System.out.println(i + "\t" + hist[i]*inv_N);
				}
				
			}
		}

	}
}
