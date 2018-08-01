import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest41_HSfrac_by_step extends Job{

	public static void main(String[] args) {
		AirportTest41_HSfrac_by_step job = new AirportTest41_HSfrac_by_step();
//		job.run("param.ini");

		job.run(1000, 4, 2.7, 0.5, (int)(1E5));
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		set_plist(controlParameterList);
		int N = nextInt_from_pList();
		int kmin = nextInt_from_pList();
		double gamma = nextDouble_from_pList();
		double alpha = nextDouble_from_pList();
		int maxStep = nextInt_from_pList();

		try {
			String folderName = "test41";
			new File(folderName).mkdirs();
			String folderPath = folderName+"/";
			String fileNameFraction = "alpha" + alpha + "_gamma" + gamma + "_kmin" + kmin;
			PrintWriter pw = new PrintWriter(new File(folderPath + fileNameFraction + ".csv"));

			int divided_max = maxStep;
			double a = (gamma-3)*kmin;
			int hs_frac;
			int hs_threshold = (int)(N*0.9);
			int currentStep = 0;
			int walkerNode;
			final double INV_N = 1.0/N;

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
			System.out.println(currentStep + "\t" + hs_frac*INV_N);
			pw.println(currentStep + "\t" + hs_frac*INV_N);

			walkerNode = (int)(Math.random()*net.N);
			currentStep = 1;
			while(true) {
				int nextWalkerNode = net.BiasedRandomWalk_continueWeight(currentStep, 1.0, alpha, walkerNode, 0.0, true);
				walkerNode = nextWalkerNode;

				hs_frac = 0;
				net.LinkSalience();
				for(int i=0;i<net.M;i++) {
					if(net.linkSalience[i]>=hs_threshold) hs_frac++;
				}
				System.out.println(currentStep + "\t" + hs_frac*INV_N);
				pw.println(currentStep + "\t" + hs_frac*INV_N);

				currentStep *= 10;
				divided_max *= 0.1;
				if(1.0>divided_max) break;
			}

		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
