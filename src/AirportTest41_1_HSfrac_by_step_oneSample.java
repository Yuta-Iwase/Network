import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest41_1_HSfrac_by_step_oneSample extends Job{

	public static void main(String[] args) {
		AirportTest41_1_HSfrac_by_step_oneSample job = new AirportTest41_1_HSfrac_by_step_oneSample();
//		job.run("param.ini");

		job.run(1000, 4, 2.7, 2.0, 50, 1);
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		set_plist(controlParameterList);
		int N = nextInt_from_pList();
		int kmin = nextInt_from_pList();
		double gamma = nextDouble_from_pList();
		double alpha = nextDouble_from_pList();
		int max_scaledStep = nextInt_from_pList();
		int delta_scaledStep = nextInt_from_pList();

		try {
			String folderName = "test41_1";
			new File(folderName).mkdirs();
			String folderPath = folderName+"/";
			String fileNameFraction = "alpha" + alpha + "_gamma" + gamma + "_kmin" + kmin;
			PrintWriter pw1 = new PrintWriter(new File(folderPath + fileNameFraction + ".csv"));

			double a = (gamma-3)*kmin;
			int hs_frac;
			int hs_threshold = (int)(N*0.9);
			int currentStep = 0;
			int walkerNode;
			final double INV_N = 1.0/N;
			final int DELTA_STEP = delta_scaledStep*N;

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
			System.out.println(currentStep/N + "\t" + hs_frac*INV_N);
			pw1.println(currentStep/N + "," + hs_frac*INV_N);

			walkerNode = (int)(Math.random()*net.N);
			currentStep = DELTA_STEP;
			int beforeStep = 0;
			while(true) {
				int nextWalkerNode = net.BiasedRandomWalk_continueWeight(currentStep-beforeStep, 1.0, alpha, walkerNode, 0.0, true);
				walkerNode = nextWalkerNode;

				hs_frac = 0;
				net.LinkSalience();
				for(int i=0;i<net.M;i++) {
					if(net.linkSalience[i]>=hs_threshold) hs_frac++;
				}
				System.out.println(currentStep/N + "\t" + hs_frac*INV_N);
				pw1.println(currentStep/N + "," + hs_frac*INV_N);

				beforeStep = currentStep;
				currentStep += DELTA_STEP;
				if(currentStep>max_scaledStep*N) break;
			}
			pw1.close();

			String limitFilePath = folderPath + "limit_" + fileNameFraction + ".csv";
			PrintWriter pw2 = new PrintWriter(new File(limitFilePath));
			net.turnUniform();
			net.SetWeight_to_Alpha(alpha);
			hs_frac = 0;
			net.LinkSalience();
			for(int i=0;i<net.M;i++) {
				if(net.linkSalience[i]>=hs_threshold) hs_frac++;
			}
			double limit_HSfrac = hs_frac*INV_N;
			System.out.println("limit" + "\t" + limit_HSfrac);
			pw2.println(1 + "," + limit_HSfrac);
			pw2.close();

			py_PointPlot py = new py_PointPlot();
			py.plot(folderPath+"plot_"+fileNameFraction+".py", fileNameFraction+".csv", fileNameFraction,
					0.0, 0.0,
					0.0, 1.0,
					false, "black", false,
					true, "red", 5,
					"o", true,
					0, false, false,
					"HS frac by step", "step$/N$", "#HS$/N$",
					true, "${\\alpha}=$"+alpha+" ${\\gamma}=$"+gamma+" $k_{\\min}=$"+kmin, "lower right",
					("y=x*0+"+limit_HSfrac), "blue", true, "$\\lim_{{\\rm step} \\to \\infty}$ (HS frac)"
					);

		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
