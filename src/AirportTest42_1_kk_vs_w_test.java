import java.io.File;
import java.io.PrintWriter;

public class AirportTest42_1_kk_vs_w_test{

	public static void main(String[] args) throws Exception{
		int N = 1000;
		double gamma = 2.7;
		int minDegree = 2;
		int maxDegree = N/10;
		int swapTime = 10000;
		int loopLimit = 1000;
		int step = 1_000_000;
		double alpha = -1.0;

		int[][] edgeList;
		int[] degreeList;
		int[] origin_sum_w, origin_sum_w_elements;
		int[] as_sum_w, as_sum_w_elements;
		int[] dis_sum_w, dis_sum_w_elements;
		double origin_DCC = 0.0;
		double as_DCC = 0.0;
		double dis_DCC = 0.0;

		ScaleFreeNetwork org_net = new ScaleFreeNetwork(N, gamma, minDegree, maxDegree, loopLimit);
		org_net.setNode(false);
		org_net.setEdge();
		org_net.degreeCorrelationCoefficient();
		Network as_net = org_net.clone();
		as_net.setNode(false);
		as_net.setEdge();
		as_net.degreeCorrelationCoefficient();
		Network dis_net = org_net.clone();
		dis_net.setNode(false);
		dis_net.setEdge();
		dis_net.degreeCorrelationCoefficient();

		// 無相関
		origin_DCC = org_net.degreeCorrelationCoefficient;
		System.out.println("もとの係数:\t" + origin_DCC);
		org_net.BiasedRandomWalk(step, 1.0, alpha, 0.0, true);
		edgeList = org_net.list;
		degreeList = org_net.degree;
		origin_sum_w = new int[N*N];
		origin_sum_w_elements = new int[N*N];
		for(int i=0;i<org_net.M;i++) {
			int left = edgeList[i][0];			int right = edgeList[i][1];
			int degree0 = degreeList[left];	int degree1 = degreeList[right];

			double currentWeight = org_net.weight[i];

			origin_sum_w[degree0*degree1] += currentWeight;
			origin_sum_w_elements[degree0*degree1]++;
		}

		// 正の相関
		as_net.setNeightbor();
		for(int i=0;i<swapTime;i++){
			as_net.degreeCorrelationCoefficient_forSwapping(true, loopLimit);
		}
		as_DCC = as_net.degreeCorrelationCoefficient;
		System.out.println("正の係数:\t" + as_DCC);

		as_net.setNode(false);
		as_net.setEdge();
		as_net.BiasedRandomWalk(step, 1.0, alpha, 0.0, true);
		edgeList = as_net.list;
		degreeList = as_net.degree;
		as_sum_w = new int[N*N];
		as_sum_w_elements = new int[N*N];
		for(int i=0;i<as_net.M;i++) {
			int left = edgeList[i][0];			int right = edgeList[i][1];
			int degree0 = degreeList[left];	int degree1 = degreeList[right];

			double currentWeight = as_net.weight[i];

			as_sum_w[degree0*degree1] += currentWeight;
			as_sum_w_elements[degree0*degree1]++;
		}

		// 負の相関
		dis_net.setNeightbor();
		for(int i=0;i<swapTime;i++){
			dis_net.degreeCorrelationCoefficient_forSwapping(false, loopLimit);
		}
		dis_DCC = dis_net.degreeCorrelationCoefficient;
		System.out.println("負の係数:\t" + dis_DCC);

		dis_net.setNode(false);
		dis_net.setEdge();
		dis_net.BiasedRandomWalk(step, 1.0, alpha, 0.0, true);
		edgeList = dis_net.list;
		degreeList = dis_net.degree;
		dis_sum_w = new int[N*N];
		dis_sum_w_elements = new int[N*N];
		for(int i=0;i<dis_net.M;i++) {
			int left = edgeList[i][0];			int right = edgeList[i][1];
			int degree0 = degreeList[left];	int degree1 = degreeList[right];

			double currentWeight = dis_net.weight[i];

			dis_sum_w[degree0*degree1] += currentWeight;
			dis_sum_w_elements[degree0*degree1]++;
		}


		String folderName = "test42";
		new File(folderName).mkdirs();
		PrintWriter pw1 = new PrintWriter(new File(folderName + "/" +  origin_DCC + "to" + as_DCC + ".csv"));
		pw1.println("kk,original_weight,ass_weight,dis_weight");
		for(int i=0;i<origin_sum_w.length;i++) {
			double origin_mean_w = 0;
			double as_mean_w = 0;
			double dis_mean_w = 0;
			if(origin_sum_w_elements[i]>0) origin_mean_w = origin_sum_w[i]/(double)origin_sum_w_elements[i];
			if(as_sum_w_elements[i]>0) as_mean_w = as_sum_w[i]/(double)as_sum_w_elements[i];
			if(dis_sum_w_elements[i]>0) dis_mean_w = dis_sum_w[i]/(double)dis_sum_w_elements[i];
			if(origin_mean_w>0 || as_mean_w>0 || dis_mean_w>0) {
				pw1.println(i + "," + origin_mean_w + "," + as_mean_w + "," + dis_mean_w);
			}
		}

		pw1.close();

		org_net.setNeightbor();
		as_net.setNeightbor();
		dis_net.setNeightbor();

		org_net.LinkSalience();
		as_net.LinkSalience();
		dis_net.LinkSalience();

		double org_HSfrac=0, as_HSfrac=0, dis_HSfrac=0;
		for(int i=0;i<org_net.M;i++) {
			if(org_net.linkSalience[i] > 0.9*N) org_HSfrac++;
			if(as_net.linkSalience[i] > 0.9*N) as_HSfrac++;
			if(dis_net.linkSalience[i] > 0.9*N) dis_HSfrac++;
		}
		System.out.println(org_HSfrac);
		System.out.println(as_HSfrac);
		System.out.println(dis_HSfrac);

	}

}
