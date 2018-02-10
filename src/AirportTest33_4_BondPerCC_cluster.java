import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest33_4_BondPerCC_cluster extends Job {

	public static void main(String[] args) throws Exception {
		AirportTest33_4_BondPerCC_cluster job = new AirportTest33_4_BondPerCC_cluster();

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

			double sum_D = 0.0;

			String folderName = "s=" + s_average + "_t=" + t_average;
			new File(folderName).mkdirs();
			folderName = folderName + "/";

			PrintWriter pw = new PrintWriter(new File(folderName + "D-comp.csv"));

			for (int i = 0; i < imax; i++) {
				double f = i / (double) imax;
				sum_D = 0.0;
				for (int t = 0; t < tmax; t++) {
					net.BondPercolation2018(1-f);

					net.ConnectedCompornent_BP2018();
					sum_D += net.maxCC;
				}
				sum_D /= (tmax * N);

				pw.println(f + "," + sum_D);
			}
			net.printListExtention(folderName + "net.csv");

			pw.close();

		} catch (Exception e) {
			System.out.println("error happend!");
		}

	}

}
