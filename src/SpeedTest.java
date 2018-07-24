
public class SpeedTest {

	public static void main(String[] args) {
		int times = 100;
		long s;

		System.out.println("start\r\n");

		s = System.currentTimeMillis();
		for(int t=0;t<times;t++){
			Network net = new ScaleFreeNetwork(1000, 2.7, 4, 100, 100);
			net.setNode(false);
			net.setEdge();
			net.SetWeight_to_Alpha(1.0);
			net.LinkSalience();
		}
		System.out.println(System.currentTimeMillis()-s);

		s = System.currentTimeMillis();
		for(int t=0;t<times;t++){
			Network net = new ScaleFreeNetwork(1000, 2.7, 4, 100, 100);
			net.setNode(false);
			net.setEdge();
			net.SetWeight_to_Alpha(1.0);
			net.LinkSalience_legacy();
		}
		System.out.println(System.currentTimeMillis()-s);

	}
}
