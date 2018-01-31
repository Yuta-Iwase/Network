
public class AirportTest33_NDIComp_Caida {

	public static void main(String[] args) {
		NetworkForCSVFile net = new NetworkForCSVFile("caida.csv", false, false, false, false);
		net.setNode(false);

		int tmax = 1;
		int imax = 100;



		for(int i=0;i<imax;i++) {
			double f = i / (double)imax;
			net.SitePercolation2018(f, true);




			net.ConnectedCompornentNDI(false, true, false);
			System.out.println(net.maxCC_NID);
		}








	}

}
