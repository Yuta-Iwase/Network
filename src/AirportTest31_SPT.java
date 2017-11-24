import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

// 完全グラフでiterative

public class AirportTest31_SPT extends Job{

	public static void main(String[] args) throws Exception{
		AirportTest31_SPT job = new AirportTest31_SPT();
//		job.run("param.ini");

		ArrayList<Object> param = new ArrayList<Object>();
		param.add(0);
		param.add(50);
		param.add(0);
		int times = (int)Math.pow(10, 5);
		param.add(times);
		job.run(param);
	}

	private void compare(String path) throws Exception{
		Scanner scan1 = new Scanner(new File(path+"Backbone.txt"));
		Scanner scan2 = new Scanner(new File(path+"MST.txt"));
		Scanner scan3 = new Scanner(new File(path+"HSS.txt"));
		PrintWriter pw = new PrintWriter(new File(path+"result.txt"));

		ArrayList<Integer> bList = new ArrayList<>();
		ArrayList<Integer> mList = new ArrayList<>();
		ArrayList<Integer> hList = new ArrayList<>();

		while(scan1.hasNextInt()) {
			bList.add(scan1.nextInt());
		}
		while(scan2.hasNextInt()) {
			mList.add(scan2.nextInt());
		}
		while(scan3.hasNextInt()) {
			hList.add(scan3.nextInt());
		}

		int matchM = 0;
		int matchH = 0;

		int currentEdge = -1;
		for(int i=0;i<bList.size();i++) {
			currentEdge = bList.get(i);
			if(mList.contains(currentEdge)) {
				matchM++;
			}
			if(hList.contains(currentEdge)) {
				matchH++;
			}
		}

		System.out.println("MSSとの一致率:" + ((double)matchM)/mList.size());
		System.out.println("HSSとの一致率:" + ((double)matchH)/hList.size());
		pw.println("MSSとの一致率:" + ((double)matchM)/mList.size());
		pw.println("HSSとの一致率:" + ((double)matchH)/hList.size());

		scan1.close();
		scan2.close();
		scan3.close();
		pw.close();
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		try {
			int index=0;

			int jobIndex = Integer.parseInt(controlParameterList.get(index++).toString());
			int N = Integer.parseInt(controlParameterList.get(index++).toString());
			int startNode = Integer.parseInt(controlParameterList.get(index++).toString());
			int times = Integer.parseInt(controlParameterList.get(index++).toString());

			String folderName = "[" + jobIndex + "]CompN=" + N + "_start=" + startNode;
			String path = folderName + "/";
			new File(folderName).mkdir();

			RandomNetwork net = new RandomNetwork(N, 1.0);
			int M = net.M;

			int minWeight = 1;
			int maxWeight = 100;
			MakePowerLaw dist = new MakePowerLaw(M, 1.5, minWeight, maxWeight);


			net.weighted = true;
			net.weight = new double[M];
			for(int i=0;i<M;i++) {
				net.weight[i] = dist.degree[i];
			}

			net.setNode(false);
			net.setEdge();

			ArrayList<Integer> MST = net.MinimumSpanningTree(false);
			Collections.sort(MST);
			ArrayList<Integer> SPT = net.ShortestPathTree(startNode);
			Collections.sort(SPT);
			ArrayList<Integer> HSS = new ArrayList<>();
			net.LinkSalience();
			for(int i=0;i<net.M;i++) {
				if(net.edgeList.get(i).linkSalience > 0.9) {
					HSS.add(i);
				}
			}

			net.CircuitReinforcedRandomWalk2(times, 2.0, startNode, true, true);
			ArrayList<Integer> backbone = new ArrayList<>();
			for(int i=0;i<M;i++) {
				if(net.weight[i] > 1.0E-6) {
					backbone.add(i);
				}
			}
			System.out.println("backbone's size is" + backbone.size());

			int matchMST = 0;
			int matchSPT = 0;
			int matchHSS = 0;
			for(int i=0;i<backbone.size();i++) {
				if(MST.contains(backbone.get(i))) matchMST++;
				if(SPT.contains(backbone.get(i))) matchSPT++;
				if(HSS.contains(backbone.get(i))) matchHSS++;
			}

			System.out.println("MST (size=" + MST.size() + ")");
			System.out.println(MST);
			System.out.println("SPT (size=" + SPT.size() + ")");
			System.out.println(SPT);
			System.out.println("HSS (size=" + HSS.size() + ")");
			System.out.println(HSS);
			System.out.println("backbone (size=" + backbone.size() + ")");
			System.out.println(backbone);

			System.out.println();

			System.out.println("MSTとの一致率:" + (((double)matchMST)/backbone.size()));
			System.out.println("SPT(" + startNode + ")との一致率:" + (((double)matchSPT)/backbone.size()));
			System.out.println("HSSとの一致率:" + (((double)matchHSS)/backbone.size()) + "\t(分母=backbone.size())");
			System.out.println("HSSとの一致率:" + (((double)matchHSS)/HSS.size()) + "\t(分母=HSS.size())");




		}catch(Exception e) {}
	}
}
