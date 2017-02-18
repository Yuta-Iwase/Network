
public class ScaleFreeNetworkTest {
	public static void main(String[] args) {
		int N=1000;
		int[] degree = new int[N];
		double gamma=2.8;

		double r;
		int sum=0;
		for(int i=0;i<N;i++){
			r = Math.random();
			degree[i] = (int)(Math.pow(1-r, 1/(1-gamma)));
			sum += degree[i];
		}
		if(sum%2==1)degree[0]++;

		ConfigurationModel2 net = new ConfigurationModel2(degree,10*N);
		String fileName = "ScaleFree_" + gamma + ".csv";
		net.printList(fileName);
	}
}
