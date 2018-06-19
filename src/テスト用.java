public class テスト用{
	public static void main(String[] args) throws Exception{
		NetworkForCSVFile net = new NetworkForCSVFile("WorldAir_w.csv", false, true);
		net.setNode(false);
		net.setEdge();
		net.EdgeRewiring();

		net.printList("re_world.csv");

	}
}
