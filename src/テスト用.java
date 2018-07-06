import java.io.File;
import java.io.PrintWriter;

public class テスト用{
	public static void main(String[] args) throws Exception{
		DMSNetwork DMS = new DMSNetwork(5, 1000, 2, 100);
		BarabasiAlbertNetwork BA = new BarabasiAlbertNetwork(5, 1000, 2, 1L);
//		net.printList("C:\\users\\owner\\desktop\\netDNS.csv");
//		System.out.println(net.generateCount);
		String desktop = "../../desktop/";
		PrintWriter pw1 = new PrintWriter(new File(desktop + "DMS.csv"));
		PrintWriter pw2 = new PrintWriter(new File(desktop + "BA.csv"));
		for(int i=0;i<DMS.M;i++) {
			int min = Math.min(DMS.degree[DMS.list[i][0]], DMS.degree[DMS.list[i][1]]);
			int max = Math.max(DMS.degree[DMS.list[i][0]], DMS.degree[DMS.list[i][1]]);
			pw1.println(min + "," + max);
		}
		for(int i=0;i<BA.M;i++) {
			int min = Math.min(BA.degree[BA.list[i][0]], BA.degree[BA.list[i][1]]);
			int max = Math.max(BA.degree[BA.list[i][0]], BA.degree[BA.list[i][1]]);
			pw2.println(min + "," + max);
		}

		pw1.close();
		pw2.close();
	}
}
