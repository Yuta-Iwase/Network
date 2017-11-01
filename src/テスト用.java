public class テスト用{
	static int[] a ;


	public static void main(String[] args) throws Exception{
		DirectedPrefentialAttachmentNetwork net = new DirectedPrefentialAttachmentNetwork(500, 10, 0.7);
		net.printInDegreeDistribution();
		net.printInDegreeDistribution("DirectedPrefentialAttachmentNetworkTest/inDegreeDistribution.txt");

		net.printList("DirectedPrefentialAttachmentNetworkTest/DirectedPrefentialAttachmentNetwork.csv");

	}
}
