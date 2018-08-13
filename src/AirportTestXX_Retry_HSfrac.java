import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AirportTestXX_Retry_HSfrac extends Job{

	public static void main(String[] args) {
		AirportTestXX_Retry_HSfrac job = new AirportTestXX_Retry_HSfrac();
		job.run("param.ini");
//		job.run(100, 200, 2.7, 4, -1, 0.1, 31, false);

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
			double hs_threshold = 0.9;
			int retry_limit = 100;
			int bins = 20; //Nの約数であること
			BigDecimal bin_width = BigDecimal.ONE.divide(new BigDecimal(bins));

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
			PrintWriter pw1 = new PrintWriter(new File("weightVariance_" + "ganna_" + gamma + "_kmin" + k_min + (weightShuffle?"_shuffle":"") + ".csv"));
			PrintWriter pw2 = new PrintWriter(new File("hs_" + "ganna_" + gamma + "_kmin" + k_min + (weightShuffle?"_shuffle":"") + ".csv"));

			for(int al=0;al<alpha_times;al++) {
				// alpha定義
				double alpha = alpha_dec.doubleValue();

				// 取るパラメータ
				double hs_frac = 0.0;
				double variance = 0.0;
				double[] edgeBC = new double[N*N];
				double[] salience = new double[N+1];

				for(int t=0;t<times;t++) {
					// 構築、重み付け
					Network net = new DMSNetwork(N, k_min, a, retry_limit);
					final double inv_M = 1.0/net.M;
					net.setNode(false);
					net.setEdge();
					net.setNeightbor();
					net.SetWeight_to_Alpha(alpha);
					net.disturb();
					if(weightShuffle) net.weightShuffle();

					// salience処理
					net.LinkSalience();
					//// salience分布
					int[] currentSalience = new int[N*N];
					for(int i=0;i<net.M;i++) {
						currentSalience[net.linkSalience[i]]++;
					}
					for(int i=0;i<salience.length;i++) {
						salience[i] += currentSalience[i]*inv_M;
					}
					//// hs frac
					int current_hs_count = 0;
					for(int i=0;i<net.edgeList.size();i++) {
						if(threshold_multi_N<net.edgeList.get(i).linkSalience) current_hs_count++;
					}
					double current_hs_frac = current_hs_count*inv_N;
					hs_frac += current_hs_frac;

					// edgeBC処理
					net.EdgeBetweenness();
					int[] currentEdgeBC = new int[N*N];
					for(int i=0;i<net.M;i++) {
						currentEdgeBC[(int)Math.round(net.edgeList.get(i).betweenCentrality)]++;
					}
					for(int i=0;i<edgeBC.length;i++) {
						edgeBC[i] += currentEdgeBC[i]*inv_M;
					}

					// 重み処理
					double inv_average_w = 1.0/MyTool.average(net.weight);
					double average_wPrime = 0.0;
					double square_average_wPrime = 0.0;
					for(int i=0;i<net.M;i++) {
						double current_wPrime = net.weight[i]*inv_average_w;
						average_wPrime += current_wPrime;
						square_average_wPrime += MyTool.square(current_wPrime);
					}
					average_wPrime /= net.M;
					square_average_wPrime /= net.M;
					double current_variance = square_average_wPrime - MyTool.square(average_wPrime);
					variance += current_variance;

				}
				hs_frac /= times;
				variance /= times;
				for(int i=0;i<edgeBC.length;i++) edgeBC[i] /= times;
				for(int i=0;i<salience.length;i++) salience[i] /= times;

				// alpha加算
				alpha_dec = alpha_dec.add(delta_alpha_dec);

				pw1.println(variance + "," + hs_frac + ","  + alpha);
				pw2.println(alpha + "," + hs_frac);
				System.out.println(alpha + "\t" + variance + "\t" + hs_frac);

				String folderName_edgeBC = "edgeBC";
				File folder_edgeBC = new File(folderName_edgeBC);
				folder_edgeBC.mkdirs();

				String s3 = (weightShuffle?"[shuffle]":"") + "edgeBC" + "_alpha" + alpha +"_kmin" + k_min + "_gamma" + gamma +".csv";
				PrintWriter pw3 = new PrintWriter(folderName_edgeBC + "/" + s3);
				for(int i=0;i<edgeBC.length;i++) {
					if(edgeBC[i]>0) pw3.println(i + "," + edgeBC[i]);
				}
				pw3.close();

				String folderName_salience = "salience";
				File folder_salience = new File(folderName_salience);
				folder_salience.mkdirs();
				String s41 = (weightShuffle?"[shuffle]":"") + "salience" + "_alpha" + alpha +"_kmin" + k_min + "_gamma" + gamma;
				PrintWriter pw41 = new PrintWriter(folderName_salience + "/" + s41 + ".csv");
				for(int i=0;i<salience.length;i++) {
					if(salience[i]>0) pw41.println(i + "," + salience[i]);
				}
				pw41.close();

				String s42 = (weightShuffle?"[shuffle]":"") + "salience("+ bins +"BIN)" + "_alpha" + alpha +"_kmin" + k_min + "_gamma" + gamma;
				PrintWriter pw42 = new PrintWriter(folderName_salience + "/" + s42 + ".csv");
				BigDecimal current_s = BigDecimal.ZERO;
				int bin_points = N/bins;
				for(int i=0;i<bins-1;i++) {
					double currentFrequency = 0.0;
					for(int j=0;j<bin_points;j++) {
						currentFrequency += salience[i*bin_points + j];
					}
					pw42.println(current_s.doubleValue() + "," + currentFrequency);
					current_s = current_s.add(bin_width);
				}
				double currentFrequency = 0.0;
				for(int j=0;j<bin_points+1;j++) {
					currentFrequency += salience[(bins-1)*bin_points + j];
				}
				pw42.println(current_s.doubleValue() + "," + currentFrequency);
				current_s = current_s.add(bin_width);
				pw42.close();

				py_PointPlot py = new py_PointPlot();

				py.plot(folderName_salience+"/"+"plot_"+s42+".py", s42+".csv", s42,
				0.0, 0.0,
				0.0, 1.0,
				false, "black", false,
				true, "red", 5,
				0, false, false,
				"", "salience $s$", "$p(s)$",
				true, "${\\alpha}=$"+alpha+" ${\\gamma}=$"+gamma+" $\\langle k \\rangle="+(k_min*2)+"$", "lower right");

			}

			pw1.close();
			pw2.close();
		} catch (Exception e) {
			System.err.println(e);
		}

	}



}
