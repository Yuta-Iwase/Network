import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import Tools.SendMail;

public class AirportTest33_3_3_NDIComp_cluster_EX2 extends Job {
	static SendMail mailer = null;

	public static void main(String[] args) throws Exception {
		mailer = new SendMail(args[0], args[1]);

		AirportTest33_3_3_NDIComp_cluster_EX2 job = new AirportTest33_3_3_NDIComp_cluster_EX2();

		// input .ini file
		job.run("param.ini");

		// debag
//		ArrayList<Object> list = new ArrayList<>();
//		list.add(100000); list.add(3.5); list.add(1); list.add(1000); list.add(2); list.add(0.0); list.add(0.05); list.add(20);
//		job.run(list);
	}

	public void job(ArrayList<Object> controlParameterList) {
		try {
			int index = 0;
			int N = Integer.parseInt(controlParameterList.get(index++).toString());
			double gamma = Double.parseDouble(controlParameterList.get(index++).toString());
			int tmin = Integer.parseInt(controlParameterList.get(index++).toString());
			int tmax = Integer.parseInt(controlParameterList.get(index++).toString());
			int time = Integer.parseInt(controlParameterList.get(index++).toString());
			BigDecimal dec_f0 = new BigDecimal(controlParameterList.get(index++).toString());
			BigDecimal dec_delta_f = new BigDecimal(controlParameterList.get(index++).toString());
			int imax = Integer.parseInt(controlParameterList.get(index++).toString());

			long timestamp = System.currentTimeMillis();
			mailer.sendMyself("job start", new Date().toString() + "<br>f=" + dec_f0.doubleValue());

			Network cluster_net = null;
			Network randomize_net = null;

			double cluster_sum_N = 0.0;
			double cluster_sum_D = 0.0;
			double cluster_sum_I = 0.0;
			double cluster_sum_NI = 0.0;
			double cluster_sum_DI = 0.0;
			double randomize_sum_N = 0.0;
			double randomize_sum_D = 0.0;
			double randomize_sum_I = 0.0;
			double randomize_sum_NI = 0.0;
			double randomize_sum_DI = 0.0;

			String folderName = "tmin=" + tmin + "_gamma=" + gamma;
			new File(folderName).mkdirs();
			new File(folderName+"/cluster").mkdirs();
			new File(folderName+"/randomize").mkdirs();
			folderName = folderName + "/";

			PrintWriter pw01 = new PrintWriter(new File(folderName + "cluster/N-comp.csv"));
			PrintWriter pw02 = new PrintWriter(new File(folderName + "cluster/D-comp.csv"));
			PrintWriter pw03 = new PrintWriter(new File(folderName + "cluster/I-comp.csv"));
			PrintWriter pw04 = new PrintWriter(new File(folderName + "cluster/NI-comp.csv"));
			PrintWriter pw05 = new PrintWriter(new File(folderName + "cluster/DI-comp.csv"));
			PrintWriter pw11 = new PrintWriter(new File(folderName + "randomize/N-comp.csv"));
			PrintWriter pw12 = new PrintWriter(new File(folderName + "randomize/D-comp.csv"));
			PrintWriter pw13 = new PrintWriter(new File(folderName + "randomize/I-comp.csv"));
			PrintWriter pw14 = new PrintWriter(new File(folderName + "randomize/NI-comp.csv"));
			PrintWriter pw15 = new PrintWriter(new File(folderName + "randomize/DI-comp.csv"));


			int[] nullDist = new int[N];
			for(int z=0;z<N;z++) nullDist[z]=0;

			for (int i = 0; i < imax; i++) {
				// f_i = f_0+delta_f*i
				double f = dec_f0.add(dec_delta_f.multiply(new BigDecimal(i))).doubleValue();

				cluster_sum_N = 0.0;
				cluster_sum_D = 0.0;
				cluster_sum_I = 0.0;
				cluster_sum_NI = 0.0;
				cluster_sum_DI = 0.0;
				randomize_sum_N = 0.0;
				randomize_sum_D = 0.0;
				randomize_sum_I = 0.0;
				randomize_sum_NI = 0.0;
				randomize_sum_DI = 0.0;
				for (int t = 0; t < time; t++) {
					MakePowerLaw dist_t = null;
					int[] twice_dist_t = new int[N];
					 do {
						 dist_t = new MakePowerLaw(N, gamma, tmin, tmax);
						 cluster_net = new ClusteringConfigrationNetwork(nullDist, dist_t.degree, 100, false);
					 }while(!cluster_net.success);

					 for(int z=0;z<N;z++) twice_dist_t[z] = dist_t.degree[z]*2;
					 do {
						 randomize_net = new ConfigrationNetwork(twice_dist_t, 100, false);
					 }while(!randomize_net.success);

					cluster_net.setNode(false);
					randomize_net.setNode(false);

					cluster_net.SitePercolation2018(f, true);

					cluster_net.ConnectedCompornentNDI(true, false, false);
					cluster_sum_N += cluster_net.maxCC_NID;

					cluster_net.ConnectedCompornentNDI(false, true, false);
					cluster_sum_D += cluster_net.maxCC_NID;

					cluster_net.ConnectedCompornentNDI(false, false, true);
					cluster_sum_I += cluster_net.maxCC_NID;

					cluster_net.ConnectedCompornentNDI(true, false, true);
					cluster_sum_NI += cluster_net.maxCC_NID;

					cluster_net.ConnectedCompornentNDI(false, true, true);
					cluster_sum_DI += cluster_net.maxCC_NID;


					randomize_net.SitePercolation2018(f, true);

					randomize_net.ConnectedCompornentNDI(true, false, false);
					randomize_sum_N += randomize_net.maxCC_NID;

					randomize_net.ConnectedCompornentNDI(false, true, false);
					randomize_sum_D += randomize_net.maxCC_NID;

					randomize_net.ConnectedCompornentNDI(false, false, true);
					randomize_sum_I += randomize_net.maxCC_NID;

					randomize_net.ConnectedCompornentNDI(true, false, true);
					randomize_sum_NI += randomize_net.maxCC_NID;

					randomize_net.ConnectedCompornentNDI(false, true, true);
					randomize_sum_DI += randomize_net.maxCC_NID;

				}
				cluster_sum_N /= (time * N);
				cluster_sum_D /= (time * N);
				cluster_sum_I /= (time * N);
				cluster_sum_NI /= (time * N);
				cluster_sum_DI /= (time * N);
				randomize_sum_N /= (time * N);
				randomize_sum_D /= (time * N);
				randomize_sum_I /= (time * N);
				randomize_sum_NI /= (time * N);
				randomize_sum_DI /= (time * N);

				pw01.println(f + "," + cluster_sum_N);
				pw02.println(f + "," + cluster_sum_D);
				pw03.println(f + "," + cluster_sum_I);
				pw04.println(f + "," + cluster_sum_NI);
				pw05.println(f + "," + cluster_sum_DI);
				pw11.println(f + "," + randomize_sum_N);
				pw12.println(f + "," + randomize_sum_D);
				pw13.println(f + "," + randomize_sum_I);
				pw14.println(f + "," + randomize_sum_NI);
				pw15.println(f + "," + randomize_sum_DI);
			}
			cluster_net.printListExtention(folderName + "cluster.csv");
			randomize_net.printListExtention(folderName + "randomize.csv");

			pw01.close();
			pw02.close();
			pw03.close();
			pw04.close();
			pw05.close();
			pw11.close();
			pw12.close();
			pw13.close();
			pw14.close();
			pw15.close();

			mailer.sendMyself("job finish", new Date().toString() + "<br>f=" + dec_f0.doubleValue() + "<br>rap:" + ((System.currentTimeMillis()-timestamp)/1000) + "seconds");

		} catch (Exception e) {
			System.out.println("error happend!");
			System.out.println(e);
		}

	}

}
