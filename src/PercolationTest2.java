import java.io.File;
import java.io.PrintWriter;

public class PercolationTest2 {
	public static void main(String[] args) {
		// 試行回数
		int times = 10000;
		// 占有確率
		double f = 0.6;
		
		// 初期化
		GephiNetwork net1 = new GephiNetwork("power.csv", false);
		Network net2;
		int[] sizeN = new int[net1.N];
		for(int n=0;n<net1.N;n++) sizeN[n]=0;
		double start;
		PrintWriter pw;
		
		// 実行
		// 計測開始点
		start = System.currentTimeMillis();
		
		for(int t=0;t<times;t++){
			net2 = net1.clone();
			net2.SitePercolation(f);
			sizeN[ net2.SearchAlgorithm(false) ]++;
		}
		
		try{
			pw = new PrintWriter(new File("ComponentGraph.dat"));
			for(int s=0;s<net1.N;s++){
				pw.println(s + "\t" +(sizeN[s]/(double)times));
			}
			pw.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		// 計測終了点
		System.out.println("実行時間: " + (System.currentTimeMillis() - start) + "[ms]");
		
		
		// (参考)ｻｲﾄ･ﾊﾟｰｺﾚｰｼｮﾝ,探索アルゴリズムの時間
		net2 = net1.clone();
		
		start = System.currentTimeMillis();
		net2.SitePercolation(f);
		System.out.println("ｻｲﾄ･ﾊﾟｰｺﾚｰｼｮﾝ: " + (System.currentTimeMillis() - start) + "[ms]");
		
		start = System.currentTimeMillis();
		sizeN[ net2.SearchAlgorithm(false) ]++;
		System.out.println("探索アルゴリズム: " + (System.currentTimeMillis() - start) + "[ms]");
	}
}
