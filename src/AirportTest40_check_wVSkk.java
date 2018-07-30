import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest40_check_wVSkk extends Job{

	public static void main(String[] args) {
		AirportTest40_check_wVSkk job = new AirportTest40_check_wVSkk();
//		job.run("param.ini");

		job.run(1000,4,2.7,2.0,100000);

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
//			py.plot(fileNameFraction+".py", fileNameFraction, str_outputName, dou_plotRangeX_start, dou_plotRangeX_end, dou_plotRangeY_start, dou_plotRangeY_end, bool_withLine, str_lineColor, bool_dottedLine, bool_withPoint, str_pointColor, int_pointSize, int_accumulationMode, bool_logscaleX, bool_logscaleY, str_title, str_xLabel, str_yLabel, bool_withLegend, str_legendLabel, str_legendPosition);
		}catch (Exception e) {
			// TODO: handle exception
		}

	}



}
