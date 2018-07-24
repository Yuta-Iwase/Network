
public class aa {

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		
		int times = 1;
		
		for(int t=0;t<times;t++){
			Network net = new ScaleFreeNetwork(1000, 2.7, 2, 100, 100);
			net.setNode(false);
			net.setEdge();
			net.SetWeight_to_Alpha(1.0);
			net.LinkSalience();
		}
		
		System.out.println(System.currentTimeMillis()-s);
		
	}
}
