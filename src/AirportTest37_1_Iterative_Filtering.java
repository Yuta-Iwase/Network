import java.io.File;
import java.io.PrintWriter;

public class AirportTest37_1_Iterative_Filtering {

	public static void main(String[] args) throws Exception{
		int times = 10;

		int N = 1000;
		double gamma = 2.7;
		int minDegree = 4;
		double alpha = 0.4;

		HistogramGenerator hist = new HistogramGenerator();
		double[][] sHist = null;

		Network net = new ScaleFreeNetwork(N, gamma, minDegree, N-1, 100);
		net.setNode(false);
		net.setEdge();
		net.SetWeight_to_Alpha(alpha, N*100);


		for(int t=0;t<times;t++) {
			final double INV_N = 1.0/N;
			final double INV_M = 1.0/net.M;

			net.LinkSalience();
			int[] sList = new int[net.edgeList.size()];
			for(int i=0;i<net.edgeList.size();i++) sList[i]=net.edgeList.get(i).linkSalience;
			sHist = hist.binPlot(sList, 20, false, 0, N);

			for(int i=0;i<sHist.length;i++) {
//				System.out.println(sHist[i][0]*INV_N + "\t" + sHist[i][1]*INV_M);
//				System.out.printf("%.5f\t%.5f\r\n", sHist[i][0]*INV_N, sHist[i][1]*INV_M);
			}

			System.out.println("t=\t" + t  + "\thigh salience frac./N=\t" + (sHist[sHist.length-1][1]+sHist[sHist.length-2][1])*INV_N);
			if(sHist[sHist.length-1][1]+sHist[sHist.length-2][1]==N) {
				System.out.println("break as HS_frac=(N-1)/N");
				break;
			}

			net = net.FilteringBySalience(0.0, 0.05*N);
			net.setNode(false);
			net.setEdge();
			net.SetWeight_to_Alpha(alpha, N*100);
		}

		PrintWriter pw = new PrintWriter(new File("c:\\users\\owner\\desktop\\result.txt"));
		for(int i=0;i<sHist.length;i++) {
			System.out.println(sHist[i][0] + "\t" + sHist[i][1]);
			pw.println(sHist[i][0] + "\t" + sHist[i][1]);
		}
		pw.close();
	}

}
