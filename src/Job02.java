import java.io.File;
import java.io.PrintWriter;

public class Job02 {
	public static void main(String[] args) throws Exception{
		String projectDirectory = "D:\\Java/Network/";

		PrintWriter pw = new PrintWriter(new File(projectDirectory + "job/job02.txt"));

		NetworkForCSVFile net = new NetworkForCSVFile(projectDirectory + "WorldAir/WorldAir_RRW1.0.csv",false,true,false,false);
		net.setNode(false);
		net.setEdge();
		net.LinkSalience();

		for(int i=0;i<net.M;i++){
			pw.println(i + "\t" + net.edgeList.get(i).linkSalience);
		}

		pw.close();
	}
}
