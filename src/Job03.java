import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Job03 {

	public static void main(String[] args) throws Exception{
		NetworkForCSVFile netCSV= new NetworkForCSVFile("JPAirport/JPAirport_w.csv", false, true, false, false);
		ConfigrationNetwork net = new ConfigrationNetwork(netCSV.degree, 10000);
		int counter = 1;
		while(!net.success){
			net = new ConfigrationNetwork(netCSV.degree, 10000);
			System.out.println("生成" + counter + "回目");
			counter++;
		}

		ArrayList<Double> wList = new ArrayList<Double>();
		for(int i=0;i<netCSV.M;i++){
			wList.add(netCSV.weight[i]);
		}

		if(net.success){
			PrintWriter pw = new PrintWriter(new File("jp_random/jp_random_w.csv"));
			int r;
			for(int i=0;i<net.M;i++){
				r = (int)(wList.size()*Math.random());
				net.weight[i] = wList.get(r);
				wList.remove(r);
				System.out.println(net.list[i][0] + "\t" + net.list[i][1]  + "\t" + net.weight[i]);
				pw.println(net.list[i][0] + "," + net.list[i][1]  + "," + net.weight[i]);
			}

			pw.close();

		}

//		for(int i=0;i<netCSV.degree.length;i++)System.out.println(i + "\t" + netCSV.degree[i]);

	}

}
