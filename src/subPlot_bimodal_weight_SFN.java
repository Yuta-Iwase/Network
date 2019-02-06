import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class subPlot_bimodal_weight_SFN {

	public static void main(String[] args) throws FileNotFoundException {
		int kmin = Integer.parseInt(args[0]);
		double gamma = Double.parseDouble(args[1]);
		String str_init_p_L = args[2];
		String str_end_p_L = args[3];

		String folderName = "gamma" + gamma;
		File folder = new File(folderName);
		folder.mkdirs();

		BigDecimal bd_p_L = new BigDecimal(str_init_p_L);
		BigDecimal bd_end_p_L = new BigDecimal(str_end_p_L);
		BigDecimal delta_p_L = new BigDecimal("0.05");
		int dMax = (int)Math.round(bd_end_p_L.subtract(bd_p_L).doubleValue() / delta_p_L.doubleValue()) + 1;

		double p_L;
		for(int d=0;d<dMax;d++) {
			p_L = bd_p_L.doubleValue();

			int times = 100;
//			int bins = 20;

			int N = 1000;

			double w_L = 100;
			double w_S = 1;

			Network net = null;
			int[] sumSalienceFreq = new int[N+1];
			for(int t=0;t<times;t++) {
				net = new DMSNetwork(N, kmin, DMSNetwork.calc_a(gamma, kmin), 100);

				net.setNode(false);
				net.setEdge();
				net.setNeightbor();

				net.weight = new double[net.M];
				for(int i=0;i<net.M;i++){
					double r = Math.pow(10,-6)*Math.random()+1;
					if(Math.random() < p_L){
						net.weight[i] = w_L * r;
					}else{
						net.weight[i] = w_S * r;
					}
				}

				net.LinkSalience();
				for(int i=0;i<net.M;i++) {
					sumSalienceFreq[net.linkSalience[i]]++;
				}
			}

			final double INV_N = 1.0/N;
			final double INV_M = 1.0/net.M;
			final double INV_T = 1.0/times;

			File distFile = new File(folderName + "/pL" + bd_p_L.toString() + ".dat");
			PrintWriter pw1 = new PrintWriter(distFile);
			File configFile = new File(folderName + "/config_" + bd_p_L.toString() + ".txt");
			PrintWriter pw2 = new PrintWriter(configFile);

			for(int i=0;i<sumSalienceFreq.length;i++) {
				pw1.println(i*INV_N + "," + sumSalienceFreq[i]*INV_M*INV_T);
			}

			pw2.println("times=" + times);
			pw2.println("network=DMS");
			pw2.println("N=" + N);
			pw2.println("gamma=" + gamma);
			pw2.println("kmin(m0)=" + kmin);

			pw1.close();
			pw2.close();


			System.out.println("p_L=" + bd_p_L.toString() + " complete");
			bd_p_L = bd_p_L.add(delta_p_L);
		}


	}

}
