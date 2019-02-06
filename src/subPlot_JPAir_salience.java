import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class subPlot_JPAir_salience {

	public static void main(String[] args) throws Exception{
		double initAlpha = Double.parseDouble(args[0]);
		double deltaAlpha = Double.parseDouble(args[1]);
		int alphaN = Integer.parseInt(args[2]);

		int times = 100;
		int bins = 20;

		Network net = null;
		if(new File("JPAirport/JPAirport_w.csv").exists()) {
			net = new NetworkForCSVFile("JPAirport/JPAirport_w.csv", false, true, false, false);
		}else if(new File("JPAirport_w.csv").exists()) {
			net = new NetworkForCSVFile("JPAirport_w.csv", false, true, false, false);
		}
		int N = net.N;
		int M = net.M;
		final double INV_N = 1.0/N;
		final double INV_M = 1.0/net.M;
		int step = N*1000;

		net.setNode(false);
		net.setEdge();
		net.setNeightbor();

		File csvFile = new File("JPAir_HSfrac.csv");
		if(!csvFile.exists()) {
			PrintWriter pw = new PrintWriter(csvFile);
			pw.close();
		}

		File s_csvFile = new File("JPAir_HSfrac_shuffle.csv");
		if(!s_csvFile.exists()) {
			PrintWriter pw2 = new PrintWriter(s_csvFile);
			pw2.close();
		}

//		double[] sumFreq = new double[N+1];


		ArrayList<String> writeLineList = new ArrayList<>();
		ArrayList<String> s_writeLineList = new ArrayList<>();
		double alpha;
		for(int a=0;a<alphaN;a++) {
			alpha = initAlpha + a*deltaAlpha;
			double sumHSfrac = 0.0;
			double s_sumHSfrac = 0.0;

			for(int t=0;t<times ;t++) {
				net.turnUniform();
				net.BiasedRandomWalk(step, 1.0, alpha, 0.0, true);

				net.LinkSalience();

				int currentHSEdge = 0;
				for(int i=0;i<net.M;i++) {
					if(net.linkSalience[i] >= 0.9*N) {
						currentHSEdge++;
					}
				}
				sumHSfrac += currentHSEdge * INV_N;


				net.weightShuffle();
				net.LinkSalience();
				int s_currentHSEdge = 0;
				for(int i=0;i<net.M;i++) {
					if(net.linkSalience[i] >= 0.9*N) {
						s_currentHSEdge++;
					}
				}
				s_sumHSfrac += s_currentHSEdge * INV_N;
			}

			writeLineList.add(alpha + "," + sumHSfrac/times);
			s_writeLineList.add(alpha + "," + s_sumHSfrac/times);
		}


		Scanner scan = new Scanner(csvFile);
		ArrayList<String> line = new ArrayList<>();
		while(scan.hasNextLine()) {
			line.add(scan.nextLine());
		}
		scan.close();

		PrintWriter pw1 = new PrintWriter(csvFile);
		for(int i=0;i<line.size();i++) {
			pw1.println(line.get(i));
		}
		for(int i=0;i<writeLineList.size();i++) {
			pw1.println(writeLineList.get(i));
			System.out.println(writeLineList.get(i));
		}
		pw1.close();


		scan = new Scanner(s_csvFile);
		line = new ArrayList<>();
		while(scan.hasNextLine()) {
			line.add(scan.nextLine());
		}
		scan.close();

		PrintWriter pw2 = new PrintWriter(s_csvFile);
		for(int i=0;i<line.size();i++) {
			pw2.println(line.get(i));
		}
		for(int i=0;i<s_writeLineList.size();i++) {
			pw2.println(s_writeLineList.get(i));
			System.out.println(s_writeLineList.get(i));
		}
		pw2.close();



	}

}
