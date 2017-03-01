
public class AirportTest19_2_PropertyPloter {
	public static void main(String[] args) {
		NetworkForCSVFile net = new NetworkForCSVFile("USairport500_weighted.csv",false,true,false,false);
		net.setNode(false);
		net.setEdge();
		
		net.EdgeBetweenness();
		for(int i=0;i<net.M;i++){
//			System.out.println(i + "\t" + net.weight[i] + "\t" + net.edgeList.get(i).betweenCentrality);
			System.out.println(net.edgeList.get(i).betweenCentrality);
		}
	}
}
