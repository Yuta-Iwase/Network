import java.io.File;
import java.io.PrintWriter;

public class テスト用{
	public static void main(String[] args) throws Exception{
		int N = 10000;
		Network net;

//		do {
//			MakePowerLaw dist = new MakePowerLaw(N, 2.7, 2, N/10);
//			net = new ConfigrationNetwork(dist.degree,100,false);
//		}while(net.success);

		do {
			MakePoisson dist = new MakePoisson(N, 3.84406);
			net = new ConfigrationNetwork(dist.degree,100,false);
		}while(net.success);

		PrintWriter pw = new PrintWriter(new File("C:\\desktop\\rnd.csv"));
		for(int i=0;i<net.degree.length;i++) {
			System.out.println(net.degree[i]);
			pw.println(net.degree[i]);
		}

		pw.close();


	}
}
