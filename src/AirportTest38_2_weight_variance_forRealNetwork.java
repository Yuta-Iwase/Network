
public class AirportTest38_2_weight_variance_forRealNetwork {

	public static void main(String[] args) {
		double hs_threshold = 0.9;


		double hs_frac = 0.0;
		double variance = 0.0;

		// 構築、重み付け
//		Network net = new NetworkForCSVFile("JPAirport/JPAirport_w.csv", false, true, false, false);
//		Network net = new NetworkForCSVFile("WorldAir/WorldAir_w.csv",false,true,false,false);
//		Network net = new NetworkForCSVFile("CaenorhabditisElegans.csv", false, true, false, false);
		Network net = new NetworkForCSVFile("C:\\Users\\Owner\\Desktop\\job0723_4\\florida.csv", false, true, false, false);
		
		net.setNode(false);
		net.setEdge();
		double threshold_multi_N = hs_threshold*net.N;
		double inv_N = 1.0/net.N;

		// 重みシャッフル
		net.weightShuffle();


		// salience処理
		net.LinkSalience();
		int current_hs_count = 0;
		for(int i=0;i<net.edgeList.size();i++) {
			if(threshold_multi_N<net.edgeList.get(i).linkSalience) current_hs_count++;
		}
		double current_hs_frac = current_hs_count*inv_N;
		hs_frac += current_hs_frac;

		// 重み処理
		double inv_average_w = 1.0/MyCalc.average(net.weight);
		double average_wPrime = 0.0;
		double square_average_wPrime = 0.0;
		for(int i=0;i<net.M;i++) {
			double current_wPrime = net.weight[i]*inv_average_w;
			average_wPrime += current_wPrime;
			square_average_wPrime += MyCalc.square(current_wPrime);
		}
		average_wPrime /= net.M;
		square_average_wPrime /= net.M;
		double current_variance = square_average_wPrime - MyCalc.square(average_wPrime);
		variance += current_variance;


		System.out.println(variance + "," + hs_frac + "," + "FloridaEcosystem");

		System.out.println();
		for(int i=0;i<net.M;i++) {
//			System.out.println(net.weight[i] + "\t" + net.edgeList.get(i).linkSalience);
		}
//		net.printList();

	}

}
