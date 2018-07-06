public class テスト用{
	public static void main(String[] args) throws Exception{
		DMSNetwork net = new DMSNetwork(5, 1000, 2, 100);
//		BarabasiAlbertNetwork net = new BarabasiAlbertNetwork(5, 1000, 2, 1L);
		net.printList("C:\\users\\owner\\desktop\\netDNS.csv");
//		System.out.println(net.generateCount);
		for(int i=0;i<net.M;i++) {
			System.out.println(net.degree[net.list[i][0]] + "\t" + net.degree[net.list[i][1]]);
		}
		
//		net.printList();
	}
}
