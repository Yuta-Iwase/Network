import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AirportTest39_1_weightDist extends Job{

	public static void main(String[] args) {
		AirportTest39_1_weightDist job = new AirportTest39_1_weightDist();
		job.run("param.ini");
//		job.run(2, 100, 2.7, 4, 1.0, 0.1, 1, false);

	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		try {
			set_plist(controlParameterList);

			int times = nextInt_from_pList();

			int N = nextInt_from_pList();
			double gamma = nextDouble_from_pList();
			int k_min = nextInt_from_pList();

			String min_alpha_string = nextString_from_pList();
			String delta_alpha_string = nextString_from_pList();
			int alpha_times = nextInt_from_pList();

			boolean weightShuffle = nextBoolean_from_pList();

			// 解析用パラメータ
//			double hs_threshold = 0.9;
			int retry_limit = 100;

			// BigDecimal型で厳密に値を管理
			BigDecimal alpha_dec = new BigDecimal(min_alpha_string);
			BigDecimal delta_alpha_dec = new BigDecimal(delta_alpha_string);

			//// 定数を計算
			// DMS用パラメータを計算
			double a = k_min*(gamma-3.0);
			// しきい値*N
//			double threshold_multi_N = hs_threshold*N;
			// Nの逆数
			double inv_N = 1.0/N;

			// PrintWriter設置
//			PrintWriter pw = new PrintWriter(new File("kmin" + k_min + (weightShuffle?"_shuffle":"") + ".txt"));

			for(int al=0;al<alpha_times;al++) {
				// alpha定義
				double alpha = alpha_dec.doubleValue();

				// 取るパラメータ
//				double hs_frac = 0.0;
//				double variance = 0.0;
				double[] kk_dist = new double[N*N];
				int[] total_kk_count = new int[N*N];
				double[] k_dist = new double[N];


				// PrintWriter設置
				PrintWriter pw2 = new PrintWriter(new File("alpha" + alpha + "_weightDist" + ".csv"));
				PrintWriter pw3 = new PrintWriter(new File("alpha" + alpha + "_degreeDist" + ".csv"));
				for(int t=0;t<times;t++) {
					// 構築、重み付け
					Network net = new DMSNetwork(N, k_min, a, retry_limit);
					net.setNode(false);
					net.setEdge();
					net.SetWeight_to_Alpha(alpha);
					net.disturb();
					if(weightShuffle) net.weightShuffle();

					// salience処理
//					net.LinkSalience();
//					int current_hs_count = 0;
//					for(int i=0;i<net.edgeList.size();i++) {
//						if(threshold_multi_N<net.edgeList.get(i).linkSalience) current_hs_count++;
//					}
//					double current_hs_frac = current_hs_count*inv_N;
//					hs_frac += current_hs_frac;

					// 重み分散処理
//					double inv_average_w = 1.0/MyTool.average(net.weight);
//					double average_wPrime = 0.0;
//					double square_average_wPrime = 0.0;
//					for(int i=0;i<net.M;i++) {
//						double current_wPrime = net.weight[i]*inv_average_w;
//						average_wPrime += current_wPrime;
//						square_average_wPrime += MyTool.square(current_wPrime);
//					}
//					average_wPrime /= net.M;
//					square_average_wPrime /= net.M;
//					double current_variance = square_average_wPrime - MyTool.square(average_wPrime);
//					variance += current_variance;

					// kk計算
					final double INV_M = 1.0/net.M;
					int[] current_kk_count = new int[N*N];
					for(int i=0;i<net.M;i++) {
						int k1 = net.degree[net.list[i][0]];
						int k2 = net.degree[net.list[i][1]];
						current_kk_count[k1*k2]++;
					}
					for(int i=0;i<N*N;i++) {
						total_kk_count[i] += current_kk_count[i];
						kk_dist[i] += current_kk_count[i]*INV_M;
					}
					// k計算
					for(int i=0;i<N;i++) {
						k_dist[net.degree[i]]++;
					}

				}
//				hs_frac /= times;
//				variance /= times;
				final double INV_T = 1.0/times;
				for(int i=0;i<N*N;i++) {
					kk_dist[i] *= INV_T;
					if(total_kk_count[i]>0) {
						pw2.println(i + "," + kk_dist[i]);
//						System.out.println(i + "\t" + kk_dist[i]);
					}
				}
				final double INV_N_MULTI_TIME = 1.0/(N*times);
				for(int i=0;i<N;i++) {
					if(k_dist[i]>0) {
						pw3.println(i + "," + k_dist[i]*INV_N_MULTI_TIME);
					}
				}

				// alpha加算
				alpha_dec = alpha_dec.add(delta_alpha_dec);

//				pw.println(variance + "," + hs_frac + ","  + alpha);
//				System.out.println(alpha + "\t" + variance + "\t" + hs_frac);

				pw2.close();
				pw3.close();
			}


		} catch (Exception e) {
			System.err.println(e);
		}

	}



}
