
public class AirportTest37_1_Iterative_Filtering {

	public static void main(String[] args) {
		int N = 1000;
		double gamma = 2.7;
		int minDegree = 2;

		ScaleFreeNetwork net = new ScaleFreeNetwork(N, gamma, minDegree, N-1, 100);
		net.setNode(false);
		net.setEdge();
		net.SetWeight_to_Alpha(0.2, N*100);

		net.LinkSalience();
	}

}
