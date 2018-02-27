import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest34_alphaNet_research extends Job{

	public static void main(String[] args) {
		AirportTest34_alphaNet_research job = new AirportTest34_alphaNet_research();
		job.run("param.ini");

//		ArrayList<Object> list = new ArrayList<Object>();
//		list.add(-2.0);
//		job.run(list);
	}

	@Override
	public void job(ArrayList<Object> controlParameterList) {
		double alpha = Double.parseDouble(controlParameterList.get(0).toString());

		int N = 1000;
		double gamma = 2.7;

		Network net;
		do{
			MakePowerLaw dist = new MakePowerLaw(N, gamma, 2, N-1);
			net = new ConfigrationNetwork(dist.degree, 100);
		}while(!net.success);

		net.setNode(false);
		net.setEdge();

		net.BiasedRandomWalk(1000*1000, 1.0, alpha, (int)(System.currentTimeMillis()&Integer.MAX_VALUE), 0.0, true);

		net.nodeBetweenness_for_WeightedNet();
		net.EdgeBetweenness();

		try{
			String folderName = "alpha="+alpha;
			new File(folderName).mkdirs();
			PrintWriter pw1 = new PrintWriter(new File(folderName + "/node_bc.txt"));
			PrintWriter pw2 = new PrintWriter(new File(folderName + "/edge_bc.txt"));
			for(int i=0;i<N;i++){
				pw1.println(net.nodeList.get(i).betweenCentrality);
			}
			for(int i=0;i<net.M;i++) {
				pw2.println(net.edgeList.get(i).betweenCentrality);
			}
			pw1.close();
			pw2.close();
		}catch(Exception e){}

	}

}
