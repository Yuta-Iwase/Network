import java.io.File;
import java.io.PrintWriter;

public class AirportTest33_NDIComp_Caida {

	public static void main(String[] args) throws Exception{
		int tmax = 100;
		int imax = 100;

		NetworkForCSVFile net = new NetworkForCSVFile("caida.csv", false, false, false, false);
		net.setNode(false);

		int N = net.N;

		double sum_N = 0.0;
		double sum_D = 0.0;
		double sum_I = 0.0;
		double sum_NI = 0.0;
		double sum_DI = 0.0;

		String folderName = "caida";
		new File(folderName).mkdirs();
		folderName = folderName + "/";

		PrintWriter pw1 = new PrintWriter(new File(folderName + "N-comp.csv"));
		PrintWriter pw2 = new PrintWriter(new File(folderName + "D-comp.csv"));
		PrintWriter pw3 = new PrintWriter(new File(folderName + "I-comp.csv"));
		PrintWriter pw4 = new PrintWriter(new File(folderName + "NI-comp.csv"));
		PrintWriter pw5 = new PrintWriter(new File(folderName + "DI-comp.csv"));

		for(int i=0;i<imax;i++) {
			double f = i / (double)imax;
			sum_N = 0.0;
			sum_D = 0.0;
			sum_I = 0.0;
			sum_NI = 0.0;
			sum_DI = 0.0;
			for(int t=0;t<tmax;t++) {
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
			}
			sum_N /= (tmax*N);
			sum_D /= (tmax*N);
			sum_I /= (tmax*N);
			sum_NI /= (tmax*N);
			sum_DI /= (tmax*N);

			pw1.println(f + "," + sum_N);
			pw2.println(f + "," + sum_D);
			pw3.println(f + "," + sum_I);
			pw4.println(f + "," + sum_NI);
			pw5.println(f + "," + sum_DI);
		}

		pw1.close();
		pw2.close();
		pw3.close();
		pw4.close();
		pw5.close();
	}

}
