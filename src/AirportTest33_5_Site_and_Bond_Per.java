import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest33_5_Site_and_Bond_Per extends Job {

	public static void main(String[] args) throws Exception {
		AirportTest33_5_Site_and_Bond_Per job = new AirportTest33_5_Site_and_Bond_Per();

		// input .ini file
		job.run("param.ini");

		// debag
//		ArrayList<Object> list = new ArrayList<>();
//		list.add(10000); list.add(3.6); list.add(0.2); list.add(100); list.add(100);
//		job.run(list);
	}

	public void job(ArrayList<Object> controlParameterList) {
		try {
			int index = 0;
			int N = Integer.parseInt(controlParameterList.get(index++).toString());
			double s_average = Double.parseDouble(controlParameterList.get(index++).toString());
			double t_average = Double.parseDouble(controlParameterList.get(index++).toString());
			int tmax = Integer.parseInt(controlParameterList.get(index++).toString());
			int imax = Integer.parseInt(controlParameterList.get(index++).toString());

			Network net;
			 do {
				 MakePoisson dist_s = new MakePoisson(N, s_average);
				 MakePoisson dist_t = new MakePoisson(N, t_average);
				 net = new ClusteringConfigrationNetwork(dist_s.degree, dist_t.degree, 100, false);
			 }while(!net.success);

			net.setNode(false);
			net.setEdge();

			double sum_N = 0.0;
			double sum_D = 0.0;
			double sum_I = 0.0;
			double sum_NI = 0.0;
			double sum_DI = 0.0;
			double sum_Edge_D = 0.0;

			String folderName = "s=" + s_average + "_t=" + t_average;
			new File(folderName).mkdirs();
			folderName = folderName + "/";

			PrintWriter pw1 = new PrintWriter(new File(folderName + "N-comp.csv"));
			PrintWriter pw2 = new PrintWriter(new File(folderName + "D-comp.csv"));
			PrintWriter pw3 = new PrintWriter(new File(folderName + "I-comp.csv"));
			PrintWriter pw4 = new PrintWriter(new File(folderName + "NI-comp.csv"));
			PrintWriter pw5 = new PrintWriter(new File(folderName + "DI-comp.csv"));
			PrintWriter pw = new PrintWriter(new File(folderName + "result_of_bondPercolation.csv"));

			for (int i = 0; i < imax; i++) {
				double f = i / (double) imax;
				sum_N = 0.0;
				sum_D = 0.0;
				sum_I = 0.0;
				sum_NI = 0.0;
				sum_DI = 0.0;
				sum_D = 0.0;
				for (int t = 0; t < tmax; t++) {
					net.SitePercolation2018(f, true);
					net.ConnectedCompornentNDI(true, false, false);
					sum_N += net.maxCC_NID;
					net.ConnectedCompornentNDI(false, true, false);
					sum_D += net.maxCC_NID;
					net.ConnectedCompornentNDI(false, false, true);
					sum_I += net.maxCC_NID;
					net.ConnectedCompornentNDI(true, false, true);
					sum_NI += net.maxCC_NID;
					net.ConnectedCompornentNDI(false, true, true);
					sum_DI += net.maxCC_NID;


					net.BondPercolation2018(1-f);
					net.ConnectedCompornent_BP2018();
					sum_Edge_D += net.maxCC;
				}
				sum_N /= (tmax * N);
				sum_D /= (tmax * N);
				sum_I /= (tmax * N);
				sum_NI /= (tmax * N);
				sum_DI /= (tmax * N);
				sum_Edge_D /= (tmax * N);

				pw1.println(f + "," + sum_N);
				pw2.println(f + "," + sum_D);
				pw3.println(f + "," + sum_I);
				pw4.println(f + "," + sum_NI);
				pw5.println(f + "," + sum_DI);
				pw.println(f + "," + sum_Edge_D);
			}
			net.printListExtention(folderName + "net.csv");

			pw1.close();
			pw2.close();
			pw3.close();
			pw4.close();
			pw5.close();
			pw.close();

		} catch (Exception e) {
			System.out.println("error happend!");
		}

	}

}
