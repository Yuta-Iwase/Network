public class AirportTest37_1_Iterative_Filtering {

	public static void main(String[] args) throws Exception{
		int times = 10;

		int N = 1000;
		double gamma = 3.2;
		int minDegree = 8;
		double min_alpha = 0.5;
		double delta_alpha = 0.1;
		double alpha_t = 1;


		double alpha = min_alpha;
		HistogramGenerator hist = new HistogramGenerator();
		double[][] sHist = null;

		Network original_net = new ScaleFreeNetwork(N, gamma, minDegree, N-1, 100);

		for(int f=0;f<alpha_t;f++) {
			System.out.println("alpha\t" + alpha);

			Network net = original_net.clone();
			net.setNode(false);
			net.setEdge();
			net.SetWeight_to_Alpha(alpha, N*100);
			net.disturb();

			double hs_frac = -1.0;
			for(int t=0;t<times;t++) {
				final double INV_N = 1.0/N;
//				final double INV_M = 1.0/net.M;

				net.LinkSalience();
				int[] sList = new int[net.edgeList.size()];
				for(int i=0;i<net.edgeList.size();i++) sList[i]=net.edgeList.get(i).linkSalience;
				sHist = hist.binPlot(sList, 20, false, 0, N);

				hs_frac = (sHist[sHist.length-1][1]+sHist[sHist.length-2][1])*INV_N;
				System.out.println("t=\t" + t  + "\thigh salience frac./N=\t" + hs_frac);
				if(sHist[sHist.length-1][1]+sHist[sHist.length-2][1]==N) {
					System.out.println("break as HS_frac=(N-1)/N");
					break;
				}

				net = net.FilteringBySalience(0.0, 0.0);
				net.setNode(false);
				net.setEdge();
				net.SetWeight_to_Alpha(alpha, N*100);
				net.disturb();
			}
			net.printList("net2.csv");
			System.out.println("hs_frac\t" + hs_frac);
			System.out.println();

			alpha += delta_alpha;
		}
	}

	public static int k_max(Network input) {
		int max = 0;
		for(int i=0;i<input.N;i++) {
			if(max<input.degree.length) max=input.degree[i];
		}
		return max;
	}

}
