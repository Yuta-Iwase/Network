import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Graph_HSS_DegreeDist {

	public static void main(String[] args) throws FileNotFoundException {
		int times = 1;
		int N = 1000;
		double gamma = 2.7;
		int m0 = 2;
		double alpha = 1.0;

		String workDirectory = "C:/desktop/";
		PrintWriter pw1 = new PrintWriter(new File(workDirectory + "raw_dist.csv"));
		PrintWriter pw2 = new PrintWriter(new File(workDirectory + "sBefore_hss_dist.csv"));
		PrintWriter pw3 = new PrintWriter(new File(workDirectory + "sAfter_hss_dist.csv"));
		
		NetworkForCSVFile net = new NetworkForCSVFile("USairports.csv", false, true);
		N = net.N;
		
		double[] raw_degree_dist = new double[N+1];
		double[] hss_degree_dist_before = new double[N+1];
		double[] hss_degree_dist_after = new double[N+1];
		double threshold = N*0.9;
		double inv_t = 1.0/times;
		double inv_N = 1.0/N;
		
		for(int t=0;t<times;t++) {
			double a = DMSNetwork.calc_a(gamma, m0);
//			DMSNetwork net = new DMSNetwork(N, m0, a, 100);
			
			inv_N = 1.0/net.N;
			int M = net.M;
			net.setNode(false);
			net.setEdge();
			net.setNeightbor();
			
			int[] hss_degree_before = new int[N+1];
			int[] hss_degree_after = new int[N+1];
			double[] current_raw_degree_dist = new double[N+1];
			double[] current_hss_degree_dist_before = new double[N+1];
			double[] current_hss_degree_dist_after = new double[N+1];

			for(int i=0;i<N;i++) {
				current_raw_degree_dist[net.degree[i]]++;
			}

//			net.SetWeight_to_Alpha(alpha);
			net.LinkSalience();
			for(int i=0;i<M;i++) {
				if(net.linkSalience[i]>=threshold) {
					hss_degree_before[net.list[i][0]]++;
					hss_degree_before[net.list[i][1]]++;
				}
			}
			for(int i=0;i<N;i++) {
				current_hss_degree_dist_before[hss_degree_before[i]]++;
			}

			net.weightShuffle();;
			net.LinkSalience();
			for(int i=0;i<M;i++) {
				if(net.linkSalience[i]>=threshold) {
					hss_degree_after[net.list[i][0]]++;
					hss_degree_after[net.list[i][1]]++;
				}
			}
			for(int i=0;i<N;i++) {
				current_hss_degree_dist_after[hss_degree_after[i]]++;
			}
			
			for(int i=0;i<hss_degree_dist_before.length;i++) {
				raw_degree_dist[i] += current_raw_degree_dist[i]*inv_N*inv_t;
				hss_degree_dist_before[i] += current_hss_degree_dist_before[i]*inv_N*inv_t;
				hss_degree_dist_after[i] += current_hss_degree_dist_after[i]*inv_N*inv_t;
			}
		}



		for(int i=0;i<hss_degree_dist_before.length;i++) {
			if(raw_degree_dist[i]>0) {
				pw1.println(i + "," + raw_degree_dist[i]);
			}
			if(hss_degree_dist_before[i]>0) {
				pw2.println(i + "," + hss_degree_dist_before[i]);
			}
			if(hss_degree_dist_after[i]>0) {
				pw3.println(i + "," + hss_degree_dist_after[i]);
			}
		}
		
		pw1.close();
		pw2.close();
		pw3.close();

	}

}
