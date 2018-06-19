
public class min16test {

	public static void main(String[] args) {
		// ネットワーク構築
		double gamma =2.7;
		int N =10000;
		int minDegree = 16;
		Network net;
		
		int time = 0;
		do{
			System.out.println("time:" + (time++));
			MakePowerLaw dist = new MakePowerLaw(N, gamma, minDegree, N/10);
			net = new ConfigrationNetwork(dist.degree, 100,false);
		}while(!net.success);
		System.out.println("network generating comp");
//		net.setNode(false);
//		net.setEdge();
		
		
		System.out.println(N);
		
		System.out.println();
		
		net.printList();

	}

}
