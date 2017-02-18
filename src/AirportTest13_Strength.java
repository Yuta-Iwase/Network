
public class AirportTest13_Strength {

	public static void main(String[] args) {
		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv", false, true, true, true);
		net.setNode();
		new AirportNetworkTransformer().makeUndirectedEdge(net);

		net.setLabel("空港ラベル表.csv");

//		for(int i=0;i<net.list.length;i++){
//			System.out.println(i + "," + net.label[net.list[i][0]] + "," + net.label[net.list[i][1]]);
//		}

		double maxS=0.0;
		int maxIndex = 100;
		double[] s = new double[net.N]; 
		for(int i=0;i<net.N;i++){
			s[i] = 0.0;
			for(int j=0;j<net.degree[i];j++){
				s[i] += net.weight[net.nodeList.get(i).list.get(j).index];
			}
			if(maxS<s[i]){
				maxS = s[i];
				maxIndex = i;
			}


		}
//		System.out.println(maxS);
		
		for(int i=0;i<net.N;i++){
			System.out.println(i + "," + (s[i])/1.0);
		}
		
		System.out.println(maxIndex);
	}

}
