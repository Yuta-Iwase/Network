import java.io.File;
import java.io.PrintWriter;

public class PercolationTest3 {
	public static void main(String[] args) throws Exception{
		int N = 1000;

		// 生成
		MakePowerLaw dist;
		ConfigrationNetwork net;
		do{
			dist = new MakePowerLaw(N, 2.7, 2, N-1);
			net = new ConfigrationNetwork(dist.degree, 50);
		}while(!net.success);
		System.out.println("生成完了");


		String filePath = "PercolationTest3/";
		PrintWriter pw1 = new PrintWriter(new File(filePath + "before.txt"));
		PrintWriter pw2 = new PrintWriter(new File(filePath + "after.txt"));

		for(int i=0;i<net.N;i++){
			pw1.println(i + "\t" + net.degree[i]);
			System.out.println(i + "\t" + net.degree[i]);
		}
		System.out.println();

		net.BondPercolation(0.6);

		for(int i=0;i<net.N;i++){
			pw2.println(i + "\t" + net.degree[i]);
			System.out.println(i + "\t" + net.degree[i]);
		}

		pw1.close();
		pw2.close();
	}
}
