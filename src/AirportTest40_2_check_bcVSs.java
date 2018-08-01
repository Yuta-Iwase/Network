import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest40_2_check_bcVSs extends Job{

	public static void main(String[] args) {
		AirportTest40_2_check_bcVSs job = new AirportTest40_2_check_bcVSs();
		job.run("param.ini");

//		job.run(1000, 4, 2.7, 2.0);

	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		set_plist(controlParameterList);
		int N = nextInt_from_pList();
		int kmin = nextInt_from_pList();
		double gamma = nextDouble_from_pList();
		double alpha = nextDouble_from_pList();
//		int step = nextInt_from_pList();

		double a = (gamma-3.0) * kmin;
		try {
			String folderName = "test40_1";
			String folderPath = folderName + "/";
			File folder = new File(folderName);
			folder.mkdirs();
			String fileNameFraction = "alpha" + alpha + "_gamma" + gamma + "_kmin" + kmin;
			String filePath = folderPath + fileNameFraction + ".csv";
			PrintWriter pw = new PrintWriter(new File(filePath));

			Network net = new DMSNetwork(N, kmin, a, 100);
			net.setNode(false);
			net.setEdge();
			net.setNeightbor();
//			net.BiasedRandomWalk(step, 1.0, alpha, 0.0, false);
			net.SetWeight_to_Alpha(alpha);

			net.LinkSalience();
			net.EdgeBetweenness();

			final double INV_N = 1.0/net.N;
			for(int i=0;i<net.M;i++) {
				double bc = net.edgeList.get(i).betweenCentrality;
				double s = net.linkSalience[i]*INV_N;
				pw.println(bc + "," + s);
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
					"$bc$ vs $s$", "edge bc$_{ij}$", "$s_{ij}$",
					true, "${\\alpha}=$"+alpha+" ${\\gamma}=$"+gamma+" $k_{\\min}=$"+kmin, "upper left",
					"", "", false, ""
					);
		}catch (Exception e) {
			// TODO: handle exception
		}

	}



}
