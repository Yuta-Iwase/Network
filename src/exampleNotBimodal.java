
public class exampleNotBimodal {

	public static void main(String[] args) {
		int times = 100;
		int bins = 20;
		
		int N = 1000;
		int kmin = 2;
		double gamma = 2.7;
		double a = (gamma-3)*kmin;

		final double INV_N = 1.0/N;
		final double INV_TIMES = 1.0/times;

		double[] freqSalience = new double[N+1];
		for(int t=0;t<times;t++) {
//			Network net = new DMSNetwork(N, kmin, a, 100);
			Network net = new RandomNetwork(N, 4.0/N);

			final double INV_M = 1.0/net.M;
			net.setNode(false);
			net.setEdge();
			net.setNeightbor();

			net.turnUniform();
			net.disturb();

			int[] currentFreqSalience = new int[N+1];
			net.LinkSalience();
			for(int i=0;i<net.M;i++) {
				currentFreqSalience[net.linkSalience[i]]++;
			}

			for(int i=0;i<freqSalience.length;i++) {
				freqSalience[i] += currentFreqSalience[i]*INV_M;
			}
		}

		double binWidth = (double)freqSalience.length / bins;
		int currentPoint = 0;
		double binRight = 0.0 + binWidth;
		for(int i=0;i<bins;i++) {
			double currentFreq = 0.0;
			do {
				currentFreq += freqSalience[currentPoint]*INV_TIMES;
				currentPoint += 1;
			}while(currentPoint < binRight);
			System.out.println((i*binWidth+0.5*binWidth)*INV_N + "\t" + currentFreq);
			binRight += binWidth;
		}





	}

}
