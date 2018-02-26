import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AirportTest34_alphaNet_research extends Job{

	public static void main(String[] args) {
		AirportTest34_alphaNet_research job = new AirportTest34_alphaNet_research();
		
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(-2.0);
		job.run(list);
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

		try{
			String folderName = "alpha="+alpha;
			new File(folderName).mkdirs();
			PrintWriter pw = new PrintWriter(new File(folderName + "/bc.txt"));
			for(int i=0;i<N;i++){
				pw.println(net.nodeList.get(i).betweenCentrality);
			}
			pw.close();
		}catch(Exception e){}

	}

}
