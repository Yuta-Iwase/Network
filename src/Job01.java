import java.io.File;
import java.io.PrintWriter;

public class Job01 {
	public static void main(String[] args) throws Exception{
		String projectDirectory = "D:\\Java/Network/";

		PrintWriter pw = new PrintWriter(new File(projectDirectory + "job/job01.txt"));

		NetworkForCSVFile net = new NetworkForCSVFile(projectDirectory + "WorldAir/WorldAir_RRW1.0.csv",false,true,false,false);
		net.setNode(false);
		net.setEdge();
		net.EdgeBetweenness();

		for(int i=0;i<net.M;i++){
			pw.println(i + "\t" + net.edgeList.get(i).betweenCentrality);
		}

		pw.close();
	}
}
