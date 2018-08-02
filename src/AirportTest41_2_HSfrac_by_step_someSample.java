import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest41_2_HSfrac_by_step_someSample extends Job{

	public static void main(String[] args) {
		AirportTest41_2_HSfrac_by_step_someSample job = new AirportTest41_2_HSfrac_by_step_someSample();
		job.run("param.ini");

//		job.run(2, 1000, 2, 2.7, 2.0, 10, 1);
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		set_plist(controlParameterList);
		int times = nextInt_from_pList();
		int N = nextInt_from_pList();
		int kmin = nextInt_from_pList();
		double gamma = nextDouble_from_pList();
		double alpha = nextDouble_from_pList();
		int max_scaledStep = nextInt_from_pList();
		int delta_scaledStep = nextInt_from_pList();

		try {
			String folderName = "test41_2";
			new File(folderName).mkdirs();
			String folderPath = folderName+"/";
			String fileNameFraction = "alpha" + alpha + "_gamma" + gamma + "_kmin" + kmin;
			PrintWriter pw1 = new PrintWriter(new File(folderPath + fileNameFraction + ".csv"));

			String limitFilePath = folderPath + "limit_" + fileNameFraction + ".csv";
			PrintWriter pw2 = new PrintWriter(new File(limitFilePath));

			String visitFrac_FilePath = folderPath + "visit_" + fileNameFraction + ".csv";
			PrintWriter pw3 = new PrintWriter(new File(visitFrac_FilePath));

			double a = (gamma-3)*kmin;
			int hs_frac;
			int hs_threshold = (int)(N*0.9);
			int currentStep = 0;
			int walkerNode;
			final double INV_N = 1.0/N;
			final int DELTA_STEP = delta_scaledStep*N;

			int stepTimes = max_scaledStep/delta_scaledStep;

			double[] hs_frac_list = new double[stepTimes+1];
			double limit_HSfrac = 0.0;
			double[] visitedFrac_list = new double[stepTimes+1];

			for(int t=0;t<times;t++) {
				System.out.println("t=" + t);

				int current_hs_frac_index = 0;
				currentStep = 0;

				Network net = new DMSNetwork(N, kmin, a, 100);
				net.setNode(false);
				net.setEdge();
				net.setNeightbor();

				net.turnUniform();

				hs_frac = 0;
				net.LinkSalience();
				for(int i=0;i<net.M;i++) {
					if(net.linkSalience[i]>=hs_threshold) hs_frac++;
				}
//				System.out.println(currentStep/N + "\t" + hs_frac*INV_N);
//				pw1.println(currentStep/N + "," + hs_frac*INV_N);
				hs_frac_list[current_hs_frac_index] += hs_frac*INV_N;
				visitedFrac_list[current_hs_frac_index++] += 0.0;

				walkerNode = (int)(Math.random()*net.N);
				currentStep = DELTA_STEP;
//				int beforeStep = 0;
				while(true) {
					int nextWalkerNode = net.BiasedRandomWalk_continueWeight(DELTA_STEP, 1.0, alpha, walkerNode, 0.0, true);
//					System.out.println("walk: " + DELTA_STEP);
					walkerNode = nextWalkerNode;

					hs_frac = 0;
					net.LinkSalience();
					for(int i=0;i<net.M;i++) {
						if(net.linkSalience[i]>=hs_threshold) hs_frac++;
					}
					int currentVisitedNodes = 0;
					for(int i=0;i<net.N;i++) if(net.isVisited_onRW[i])currentVisitedNodes++;
					hs_frac_list[current_hs_frac_index] += hs_frac*INV_N;
					visitedFrac_list[current_hs_frac_index++] += currentVisitedNodes*INV_N;

//					beforeStep = currentStep;
					currentStep += DELTA_STEP;
					if(currentStep>max_scaledStep*N) break;
				}

				net.turnUniform();
				net.SetWeight_to_Alpha(alpha);
				hs_frac = 0;
				net.LinkSalience();
				for(int i=0;i<net.M;i++) {
					if(net.linkSalience[i]>=hs_threshold) hs_frac++;
				}
				limit_HSfrac += hs_frac*INV_N;

			}

			double INV_TIME = 1.0/times;
			for(int i=0;i<hs_frac_list.length;i++) {
				System.out.println(i*delta_scaledStep + "\t" + hs_frac_list[i]*INV_TIME + "\t" + visitedFrac_list[i]*INV_TIME);
				pw1.println(i*delta_scaledStep + "," + hs_frac_list[i]*INV_TIME);
				pw3.println(i*delta_scaledStep + "," + visitedFrac_list[i]*INV_TIME);
			}
			limit_HSfrac *= INV_TIME;
			System.out.println("limit\t" + limit_HSfrac);
			pw2.println("limit," + limit_HSfrac);


			pw1.close();
			pw2.close();
			pw3.close();

			py_PointPlot py = new py_PointPlot();
			py.plot(folderPath+"plot_"+fileNameFraction+".py", new String[]{fileNameFraction+".csv", "visit_"+fileNameFraction+".csv"}, fileNameFraction,
					0.0, 0.0,
					0.0, 1.0,
					false, new String[]{"black", "black"}, new boolean[]{false, false},
					true, new String[]{"red", "green"}, new int[]{5, 5},
					new String[]{"o", "o"}, true,
					0, false, false,
					"HS frac by step", "step$/N$", "#HS$/N$",
					true, new String[]{"${\\alpha}=$"+alpha+" ${\\gamma}=$"+gamma+" $k_{\\min}=$"+kmin, "visited nodes$/N$"}, "lower right",
					("y=x*0+"+limit_HSfrac), "blue", true, "$\\lim_{{\\rm step} \\to \\infty}$ (HS frac)"
					);
//			py.plot(folderPath+"plot_"+fileNameFraction+".py", fileNameFraction+".csv", fileNameFraction,
//					0.0, 0.0,
//					0.0, 1.0,
//					false, "black", false,
//					true, "red", 5,
//					"o", true,
//					0, false, false,
//					"HS frac by step", "step$/N$", "#HS$/N$",
//					true, "${\\alpha}=$"+alpha+" ${\\gamma}=$"+gamma+" $k_{\\min}=$"+kmin, "lower right",
//					("y=x*0+"+limit_HSfrac), "blue", true, "$\\lim_{{\\rm step} \\to \\infty}$ (HS frac)"
//					);

		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
