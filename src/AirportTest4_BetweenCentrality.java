
public class AirportTest4_BetweenCentrality {
	public static void main(String[] args) {
		GephiNetwork net = new GephiNetwork("S10b-14_BetAport_GephiPlot.csv",false);
		net.setNode();
		net.betweenCentrality();
	}
}
