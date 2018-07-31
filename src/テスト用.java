import java.io.File;
import java.io.PrintWriter;

public class テスト用{
	public static void main(String[] args) throws Exception{
		int times = 1;
		int step = (int)1E5;

		int N = 1000;
		double gamma = 2.7;
		int kmin = 4;

		double alpha = 1.5;

		final double a = kmin*(gamma-3.0);
		PrintWriter pw = new PrintWriter(new File("C:/desktop/kk_vs_w.txt"));
		for(int t=0;t<times;t++){
			Network net = new DMSNetwork(N, kmin, a, 100);
			net.setNode(false);
			net.setEdge();
			net.BiasedRandomWalk(step, 1.0, alpha, 0.0, false);
			final int[] degree = net.degree;
			final int[][] edgeList = net.list;
			final double[] weight = net.weight;
			for(int i=0;i<net.M;i++){
				int kk = degree[edgeList[i][0]]*degree[edgeList[i][1]];
				double w = weight[i];
				System.out.println(kk + "\t" + w);
				pw.println(kk + "\t" + w);
			}
		}

	}

}
