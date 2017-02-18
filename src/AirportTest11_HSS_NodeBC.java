
public class AirportTest11_HSS_NodeBC {

	public static void main(String[] args) {
		NetworkForCSVFile net = new NetworkForCSVFile("salienceNetwork.csv", false, false, false, true);
		net.setNode();
		net.betweenCentrality();
	}

}
