
public class PercolationTest1 {
	public static void main(String[] args) {
		double time = System.currentTimeMillis();
		//net1はサイト・パーコレーション用
		GephiNetwork net1 = new GephiNetwork("power.csv", false);
		//net1はボンド・パーコレーション用
		GephiNetwork net2 = new GephiNetwork("power.csv", false);
		
		System.out.println("計測点1:" + (System.currentTimeMillis()-time));
		net1.SitePercolation(0.6);
		System.out.println("計測点2:" + (System.currentTimeMillis()-time));
		net2.BondPercolation(0.7);
		System.out.println("計測点3:" + (System.currentTimeMillis()-time));
		
		// csvで出力
		net1.printList("SitePercolation_power.csv");
		net1.printListExtention("SitePercolation_power.csv");
		net2.printList("BondPercolation_power.csv");
		
		// 連結成分を解析
		System.out.println();
		net1.SearchAlgorithm(true);
		System.out.println("計測点4:" + (System.currentTimeMillis()-time));
	}
}
