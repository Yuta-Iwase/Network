public class テスト用{
	public static void main(String[] args) throws Exception{
		Network net = new ScaleFreeNetwork(1000, 2.7, 2, 500, 100);
		net.printList();
	}
}
