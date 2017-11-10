import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class AirportTest30_TreeAnalytics{

	public static void main(String[] args) throws Exception{
		new File("test30").mkdir();

		int M;
		int N = 50;

		RandomNetwork net = new RandomNetwork(N, 1.0);
		M = net.M;

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

		net.MinimumSpanningTree(false);
		net.LinkSalience();

		net.CircuitReinforcedRandomWalk2(1000001, 2.0, -1, true,false);

		int remEdge = 0;
		for(int i=0;i<M;i++) {
			if(net.weight[i] <= 1.0E-6) {
				remEdge++;
			}
		}
		System.out.println(M - remEdge);


		String folderName = "test30/";
		PrintWriter pw = new PrintWriter(new File(folderName + "Backbone.txt"));
		for(int i=0;i<net.M;i++) {
			if(net.weight[i] > 1.0E-6) {
				pw.println(i);
			}
		}
		pw.close();



		PrintWriter pw1 = new PrintWriter(new File(folderName + "MST.txt"));
		PrintWriter pw2 = new PrintWriter(new File(folderName + "HSS.txt"));
		PrintWriter pw3 = new PrintWriter(new File(folderName + "salience.txt"));

		for(int i=0;i<net.MST_Edges.size();i++) {
			pw1.println(net.MST_Edges.get(i));
		}
		int hss_N = 0;
		for(int i=0;i<net.M;i++) {
			if(net.edgeList.get(i).linkSalience>0.5*net.N) {
				pw2.println(net.edgeList.get(i).index);
				hss_N++;
			}
			pw3.println(net.edgeList.get(i).linkSalience);
		}
		System.out.println("HSS Edge:" + hss_N + "本");

		pw1.close();
		pw2.close();
		pw3.close();

		new AirportTest30_TreeAnalytics().a();;

	}

	private void a() throws Exception{
		String path = "test30/";

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
}
