import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class AirportTest37_2_Iterative_Filtering_for_DMS extends Job{

	public static void main(String[] args) throws Exception{
		AirportTest37_2_Iterative_Filtering_for_DMS job = new AirportTest37_2_Iterative_Filtering_for_DMS();
		job.run("param.ini");

	}

	public static int k_max(Network input) {
		int max = 0;
		for(int i=0;i<input.N;i++) {
			if(max<input.degree.length) max=input.degree[i];
		}
		return max;
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		try {
			int index = 0;

			int N0 = Integer.parseInt(controlParameterList.get(index++).toString());
			int N = Integer.parseInt(controlParameterList.get(index++).toString());
			int insertEdges = Integer.parseInt(controlParameterList.get(index++).toString());
			double a = Double.parseDouble(controlParameterList.get(index++).toString());

			int times = Integer.parseInt(controlParameterList.get(index++).toString());

			double inital_alpha = Double.parseDouble(controlParameterList.get(index++).toString());
			double delta_alpha = Double.parseDouble(controlParameterList.get(index++).toString());
			double alpha_t = Double.parseDouble(controlParameterList.get(index++).toString());

			double alpha = inital_alpha;
			HistogramGenerator hist = new HistogramGenerator();
			double[][] sHist = null;

			Network original_net = new DMSNetwork(N0, N, insertEdges, a, 100);

			for(int f=0;f<alpha_t;f++) {
				PrintWriter pw = new PrintWriter(new File("alpha=" + alpha + ".txt"));

				System.out.println(new Date().toString());
				System.out.println("alpha\t" + alpha);
				System.out.println();

				Network net = original_net.clone();
				net.setNode(false);
				net.setEdge();
				net.SetWeight_to_Alpha(alpha, N*100);
				net.disturb();

				double hs_frac = -1.0;
				for(int t=0;t<times;t++) {
					final double INV_N = 1.0/N;
//					final double INV_M = 1.0/net.M;

					net.LinkSalience();
					int[] sList = new int[net.edgeList.size()];
					for(int i=0;i<net.edgeList.size();i++) sList[i]=net.edgeList.get(i).linkSalience;
					sHist = hist.binPlot(sList, 20, false, 0, N);

					hs_frac = (sHist[sHist.length-1][1]+sHist[sHist.length-2][1])*INV_N;
					pw.println("t=\t" + t  + "\thigh salience frac./N=\t" + hs_frac);
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

				alpha += delta_alpha;

				pw.close();
			}
		}catch(Exception e) {
			System.out.println(e);
		}


	}

}
