import java.io.File;
import java.io.PrintWriter;

public class Job02 {
	public static void main(String[] args) throws Exception{
//		AirportTestXX_PropertyPloter.property(6, 1);
//		AirportTestXX_PropertyPloter.rRW("conf2_7min4/conf2.7min4_nw.csv","conf2_7min4/conf2.7min4_rRW20.0.csv",20.0,false);

//		AirportTestXX_PropertyPloter.property20();

		PrintWriter pw = new PrintWriter(new File("random_conf2_7/random_conf2.7_nw.csv"));
		NetworkForCSVFile netC = new NetworkForCSVFile("conf2_7/conf2.7_nw.csv", false, false, false, false);
		double p = 2 * netC.M * (1.0/netC.N) * (1.0/(netC.N-1));
//		RandomNetwork net = new RandomNetwork(1000, p);
//		for(int i=0;i<net.M;i++){
//			System.out.println(net.list[i][0] + "," + net.list[i][1]);
//			pw.println(net.list[i][0] + "," + net.list[i][1]);
//		}
		System.out.println(netC.N);

		pw.close();
	}
}
