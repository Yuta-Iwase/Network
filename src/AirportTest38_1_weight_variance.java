import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AirportTest38_1_weight_variance extends Job{

	public static void main(String[] args) {
		AirportTest38_1_weight_variance job = new AirportTest38_1_weight_variance();
		job.run("param.ini");
//		job.run(100,100,2.7,4,0,0.1,10);

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
			int alpha_times_string = nextInt_from_pList();

			// 解析用パラメータ
			double hs_threshold = 0.9;
			int retry_limit = 100;

			// BigDecimal型で厳密に値を管理
			BigDecimal alpha_dec = new BigDecimal(min_alpha_string);
			BigDecimal delta_alpha_dec = new BigDecimal(delta_alpha_string);

			//// 定数を計算
			// DMS用パラメータを計算
			double a = k_min*(gamma-3.0);
			// しきい値*N
			double threshold_multi_N = hs_threshold*N;
			// Nの逆数
			double inv_N = 1.0/N;

			// PrintWriter設置
			PrintWriter pw = new PrintWriter(new File("kmin" + k_min + ".txt"));

			for(int al=0;al<alpha_times_string;al++) {
				// alpha定義
				alpha_dec = alpha_dec.add(delta_alpha_dec);
				double alpha = alpha_dec.doubleValue();

				// 取るパラメータ
				double hs_frac = 0.0;
				double variance = 0.0;

				for(int t=0;t<times;t++) {
					// 構築、重み付け
					Network net = new DMSNetwork(N, k_min, a, retry_limit);
					net.setNode(false);
					net.setEdge();
					net.SetWeight_to_Alpha(alpha);
					net.disturb();

					// salience処理
					net.LinkSalience();
					int current_hs_count = 0;
					for(int i=0;i<net.edgeList.size();i++) {
						if(threshold_multi_N<net.edgeList.get(i).linkSalience) current_hs_count++;
					}
					double current_hs_frac = current_hs_count*inv_N;
					hs_frac += current_hs_frac;

					// 重み処理
					double inv_average_w = 1.0/MyCalc.average(net.weight);
					double average_wPrime = 0.0;
					double square_average_wPrime = 0.0;
					for(int i=0;i<net.M;i++) {
						double current_wPrime = net.weight[i]*inv_average_w;
						average_wPrime += current_wPrime;
						square_average_wPrime += MyCalc.square(current_wPrime);
					}
					average_wPrime /= net.M;
					square_average_wPrime /= net.M;
					double current_variance = square_average_wPrime - MyCalc.square(average_wPrime);
					variance += current_variance;

				}
				hs_frac /= times;
				variance /= times;

				pw.println(alpha + "\t" + variance + "\t" + hs_frac);
				System.out.println(alpha + "\t" + variance + "\t" + hs_frac);
			}

			pw.close();
		} catch (Exception e) {
			System.err.println(e);
		}

	}



}
