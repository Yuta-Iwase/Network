public class テスト用{
	static int[] a ;


	public static void main(String[] args) throws Exception{
		
		long s = System.currentTimeMillis();

		NetworkForCSVFile net = new NetworkForCSVFile("WorldAir_w.csv",false,true,false,false);
		net.setNode(false);
		net.setEdge();
		net.LinkSalience();
		
		long e = System.currentTimeMillis();
		System.out.println((s-e)/1000);


	}
}
