import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class AirportTestXX_PropertyPloter {
	public static void main(String[] args) throws Exception{
		S10b14_BetAport_salience();


	}

	static void S10b14_BetAport_salience(){
//		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv",false,true,true,true);
//		net.setNode();
//		new AirportNetworkTransformer().makeUndirectedEdge(net);
//		net.setLabel("空港ラベル表.csv");
//
////		net.LinkSalience();
////		net.Lin
//
//		for(int i=0;i<net.M;i++){
//			System.out.println(i + "\t" + net.edgeList.get(i).linkSalience);
//		}
	}

	static void JPAir() throws Exception{
		// weight読み込み用
		NetworkForCSVFile net = new NetworkForCSVFile("JPAir_RRW.csv",false,true,false,false);
		// salience読み込み用
		Scanner scan = new Scanner(new File("JPAir_RRW_s.csv"));
		// 出力ファイル
		PrintWriter pw = new PrintWriter(new File("JPAir_RRW_Property.csv"));

		// salience読み込み
		double[] s = new double[net.M];
		for(int i=0;i<net.M;i++)s[i]=0.0;
		int currentIndex = 0;
		while(scan.hasNext()){
			scan.next();
			s[currentIndex] = scan.nextDouble();
			currentIndex++;
		}

		for(int i=0;i<net.M;i++){
			System.out.println(i+","+net.weight[i]+","+s[i]);
			pw.println(i+","+net.weight[i]+","+s[i]);
		}

		net.setNode(false);
		scan.close();
		pw.close();
	}
}
