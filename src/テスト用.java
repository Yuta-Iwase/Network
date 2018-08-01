public class テスト用{
	public static void main(String[] args) throws Exception{
		int kmin = 4;
		double gamma = 2.7;
		double a = (gamma-3)*kmin;
		Network net = new DMSNetwork(10, 2, a, 100);
		
		int sumDegree =0 ;
		for(int i=0;i<net.N;i++) {
			sumDegree += net.degree[i];
		}
		
		System.out.println(sumDegree/2);
		System.out.println(net.M);
		
	}

}
