import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest41_3_HSfrac_by_step_someSample_shuffle extends Job{

	public static void main(String[] args) {
		AirportTest41_3_HSfrac_by_step_someSample_shuffle job = new AirportTest41_3_HSfrac_by_step_someSample_shuffle();
		job.run("param.ini");

//		job.run(2, 1000, 4, 2.7, 2.0, 2, 1);
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
			String folderName = "test41_3";
			new File(folderName).mkdirs();
			String folderPath = folderName+"/";
			String fileNameFraction = "alpha" + alpha + "_gamma" + gamma + "_kmin" + kmin;
			PrintWriter pw1 = new PrintWriter(new File(folderPath + fileNameFraction + ".csv"));

			String limitFilePath = folderPath + "limit_" + fileNameFraction + ".csv";
			PrintWriter pw2 = new PrintWriter(new File(limitFilePath));

			String visitNodeFrac_FilePath = folderPath + "visitNode_" + fileNameFraction + ".csv";
			PrintWriter pw3 = new PrintWriter(new File(visitNodeFrac_FilePath));

			String visitEdgeFrac_FilePath = folderPath + "visitEdge_" + fileNameFraction + ".csv";
			PrintWriter pw4 = new PrintWriter(new File(visitEdgeFrac_FilePath));

			String shuffle_fileNameFraction = "alpha" + alpha + "_gamma" + gamma + "_kmin" + kmin + "_shuffled";
			PrintWriter s_pw1 = new PrintWriter(new File(folderPath + shuffle_fileNameFraction + ".csv"));

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
			double[] visitedNodeFrac_list = new double[stepTimes+1];
			double[] visitedEdgeFrac_list = new double[stepTimes+1];

			double[] s_hs_frac_list = new double[stepTimes+1];

			for(int t=0;t<times;t++) {
				System.out.println("t=" + t);

				int current_hs_frac_index = 0;
				currentStep = 0;

				Network net = new DMSNetwork(N, kmin, a, 100);
				final double INV_M = 1.0/net.M;
				net.setNode(false);
				net.setEdge();
				net.setNeightbor();

				net.turnUniform();
				net.disturb();

				hs_frac = 0;
				net.LinkSalience();
				for(int i=0;i<net.M;i++) {
					if(net.linkSalience[i]>=hs_threshold) hs_frac++;
				}
//				System.out.println(currentStep/N + "\t" + hs_frac*INV_N);
//				pw1.println(currentStep/N + "," + hs_frac*INV_N);
				hs_frac_list[current_hs_frac_index] += hs_frac*INV_N;
				visitedNodeFrac_list[current_hs_frac_index] = 0.0;
				visitedEdgeFrac_list[current_hs_frac_index] = 0.0;
				current_hs_frac_index++;


				walkerNode = (int)(Math.random()*net.N);
				currentStep = DELTA_STEP;
//				int beforeStep = 0;
				while(true) {
					int nextWalkerNode = net.BiasedRandomWalk_continueWeight(DELTA_STEP, 1.0, alpha, walkerNode, 0.0, true);
					net.disturb();
//					System.out.println("walk: " + DELTA_STEP);
					walkerNode = nextWalkerNode;

					hs_frac = 0;
					net.LinkSalience();
					for(int i=0;i<net.M;i++) {
						if(net.linkSalience[i]>=hs_threshold) hs_frac++;
					}
					int currentVisitedNodes = 0;
					int currentVisitedEdges = 0;
					for(int i=0;i<net.N;i++) if(net.visitedNodes_onRW[i])currentVisitedNodes++;
					for(int i=0;i<net.M;i++) if(net.visitedEdge_onRW[i])currentVisitedEdges++;
					hs_frac_list[current_hs_frac_index] += hs_frac*INV_N;
					visitedNodeFrac_list[current_hs_frac_index] += currentVisitedNodes*INV_N;
					visitedEdgeFrac_list[current_hs_frac_index] += currentVisitedEdges*INV_M;


					Network s_net = net.clone();
					s_net.setNode(false);
					s_net.setEdge();
					s_net.setNeightbor();
					s_net.weightShuffle();

					hs_frac = 0;
					s_net.LinkSalience();
					for(int i=0;i<s_net.M;i++) {
						if(s_net.linkSalience[i]>=hs_threshold) hs_frac++;
					}
					s_hs_frac_list[current_hs_frac_index] += hs_frac*INV_N;


					current_hs_frac_index++;
//					beforeStep = currentStep;
					currentStep += DELTA_STEP;
					if(currentStep>max_scaledStep*N) break;
				}

				net.turnUniform();
				net.SetWeight_to_Alpha(alpha);
				net.disturb();
				hs_frac = 0;
				net.LinkSalience();
				for(int i=0;i<net.M;i++) {
					if(net.linkSalience[i]>=hs_threshold) hs_frac++;
				}
				limit_HSfrac += hs_frac*INV_N;

			}

			double INV_TIME = 1.0/times;
			for(int i=0;i<hs_frac_list.length;i++) {
				System.out.println(i*delta_scaledStep + "\t" + hs_frac_list[i]*INV_TIME + "\t" + visitedNodeFrac_list[i]*INV_TIME + "\t" + visitedEdgeFrac_list[i]*INV_TIME);
				pw1.println(i*delta_scaledStep + "," + hs_frac_list[i]*INV_TIME);
				pw3.println(i*delta_scaledStep + "," + visitedNodeFrac_list[i]*INV_TIME);
				pw4.println(i*delta_scaledStep + "," + visitedEdgeFrac_list[i]*INV_TIME);

				s_pw1.println(i*delta_scaledStep + "," + s_hs_frac_list[i]*INV_TIME);
			}
			limit_HSfrac *= INV_TIME;
			System.out.println("limit\t" + limit_HSfrac);
			pw2.println("limit," + limit_HSfrac);


			pw1.close();
			pw2.close();
			pw3.close();
			pw4.close();

			s_pw1.close();

			py_PointPlot py = new py_PointPlot();
			py.plot(folderPath+"plot_"+fileNameFraction+".py", new String[]{fileNameFraction+".csv", shuffle_fileNameFraction+".csv", "visitNode_"+fileNameFraction+".csv", "visitEdge_"+fileNameFraction+".csv"}, fileNameFraction,
					0.0, 0.0,
					0.0, 1.05,
					false, new String[]{"black", "black", "black", "black"}, new boolean[]{false, false, false, false},
					true, new String[]{"red", "salmon", "blue", "green"}, new int[]{5, 5, 5, 5},
					new String[]{"o", "o", "s", "^"}, true,
					0, false, false,
					"", "step$/N$", "$f_{\\, {\\rm HS}}$, #visited nodes$/N$, #visited edges$/M$",
					true, new String[]{"${\\alpha}=$"+alpha+" ${\\gamma}=$"+gamma+" $\\langle k \\rangle=$"+(kmin*2), "weight shuffled network" ,"#visited nodes$/N$", "#visited edges$/M$"}, "lower right",
					("y=x*0+"+limit_HSfrac), "red", true, "$\\lim_{{\\rm step} \\to \\infty}$ (HS frac)"
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
