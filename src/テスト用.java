public class テスト用{
	public static void main(String[] args) throws Exception{
		GephiNetwork net = new GephiNetwork("C:\\Users\\Owner\\Desktop\\Untitled.csv", false, true);
		for(int i=0;i<net.M;i++) {
			System.out.println(net.list[i][0] + "," + net.list[i][1] + "," + net.weight[i]);
		}
		System.out.println();
		System.out.println(net.N);
		System.out.println(net.M);

	}
}
