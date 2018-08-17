import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AirportTestXX_Retry_WeightDist extends Job{
	public static void main(String[] args) {
		AirportTestXX_Retry_WeightDist job = new AirportTestXX_Retry_WeightDist();
		job.run("param.ini");
//		job.run(100, 1000, 2.7, 4, 1.5, 0.1, 1);
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		set_plist(controlParameterList);

		int times = nextInt_from_pList();
		int N = nextInt_from_pList();
		double gamma = nextDouble_from_pList();
		int k_min = nextInt_from_pList();
		String min_alpha_string = nextString_from_pList();
		String delta_alpha_string = nextString_from_pList();
		int alpha_times = nextInt_from_pList();

		double a = (gamma-3)*k_min;
		final double INV_TIMES = 1.0/times;
		BigDecimal deltaAlpha_dec = new BigDecimal(delta_alpha_string);
		BigDecimal alpha_dec = new BigDecimal(min_alpha_string);
		py_PointPlot py = new py_PointPlot();

		try {
			for(int al = 0;al<alpha_times;al++) {
				double alpha = alpha_dec.doubleValue();

				double[] kk_dist = new double[N*N];
				for(int t=0;t<times;t++) {
					DMSNetwork net = new DMSNetwork(N, k_min, a, 100);
					final int M = net.M;
					final double INV_M = 1.0/M;

					int[] current_kk_count = new int[N*N];
					for(int i=0;i<M;i++) {
						int kk = net.degree[net.list[i][0]]*net.degree[net.list[i][1]];
						current_kk_count[kk]++;
					}
					for(int i=0;i<kk_dist.length;i++) {
						kk_dist[i] += current_kk_count[i]*INV_M;
					}
				}

				String fileName = "weight_gamma" + gamma + "_kmin" + k_min + "_alpha" + alpha_dec.doubleValue();
				PrintWriter pw = new PrintWriter(new File(fileName + ".csv"));
				for(int i=0;i<kk_dist.length;i++) {
					if(kk_dist[i]>0) {
						pw.println(Math.pow(i, alpha) + "," + kk_dist[i]*INV_TIMES);
					}
				}
				pw.close();

				py.plot("dist_"+fileName+".py", fileName+".csv", "dist_"+fileName,
						0.0, 0.0,
						0.0, 0.0,
						false, "black", false,
						true, "red", 5,
						0, true, true,
						"", "$w$", "$p(w)$",
						true, ("${\\alpha}=$"+alpha+" ${\\gamma}=$"+gamma+" $k_{\\min}=$"+k_min), "upper right"	);

				py.plot("distAC_"+fileName+".py", fileName+".csv", "distAC_"+fileName,
						0.0, 0.0,
						0.0, 0.0,
						false, "black", false,
						true, "red", 5,
						2, true, true,
						"", "$w$", "$p(w)$",
						true, ("${\\alpha}=$"+alpha+" ${\\gamma}=$"+gamma+" $k_{\\min}=$"+k_min), "upper right"	);

				alpha_dec = alpha_dec.add(deltaAlpha_dec);
			}



		}catch (Exception e) {
			System.err.println(e);
		}



	}
}
