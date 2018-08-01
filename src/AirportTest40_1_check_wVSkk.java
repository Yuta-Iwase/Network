import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest40_1_check_wVSkk extends Job{

	public static void main(String[] args) {
		AirportTest40_1_check_wVSkk job = new AirportTest40_1_check_wVSkk();
		job.run("param.ini");

//		job.run(1000, 4, 2.7, 2.0, 100000);

	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		set_plist(controlParameterList);
		int N = nextInt_from_pList();
		int kmin = nextInt_from_pList();
		double gamma = nextDouble_from_pList();
		double alpha = nextDouble_from_pList();
		int step = nextInt_from_pList();

		double a = (gamma-3.0) * kmin;
		try {
			String folderName = "test40";
			String folderPath = folderName + "/";
			File folder = new File(folderName);
			folder.mkdirs();
			String fileNameFraction = "alpha" + alpha + "_gamma" + gamma;
			String filePath = folderPath + fileNameFraction + ".csv";
			PrintWriter pw = new PrintWriter(new File(filePath));

			Network net = new DMSNetwork(N, kmin, a, 100);
			net.setNode(false);
			net.setEdge();
			net.BiasedRandomWalk(step, 1.0, alpha, 0.0, false);

			for(int i=0;i<net.M;i++) {
				int kk = net.degree[net.list[i][0]]*net.degree[net.list[i][1]];
				pw.println(kk + "," + net.weight[i]);
			}

			pw.close();

			py_PointPlot py = new py_PointPlot();
			py.plot(folderPath+"plot_"+fileNameFraction+".py", fileNameFraction+".csv", fileNameFraction,
					0.0, 0.0,
					0.0, 0.0,
					false, "black", false,
					true, "blue", 5,
					"o", true,
					0, true, true,
					"$w$ vs $kk$", "$k_i k_j$", "$w$",
					true, "${\\alpha}=$"+alpha+" ${\\gamma}=$"+gamma, "upper left",
					"y=(x**" +alpha+")", "red", false, "$y=x^{"+alpha+"}$"
					);
		}catch (Exception e) {
			// TODO: handle exception
		}

	}



}
