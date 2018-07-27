public class テスト用{
	public static void main(String[] args) throws Exception{
		int times = 1;
		int mode = 1;

		int N = 1000;
		double gamma = 2.7;
		int kmin = 4;


//		long stamp = System.currentTimeMillis();
//		for(int t=0;t<times;t++){
//			if(mode==0){
//				Network net = new ScaleFreeNetwork(N, gamma, kmin, N-1, 100);
//				net.SetWeight_to_Alpha(1.0);
//				net.setNeightbor();
//
//				long temp_stamp = System.currentTimeMillis();
//				net.LinkSalience();
//				System.out.println("section:" + t + "\t" + (System.currentTimeMillis()-temp_stamp));
//
//			}else if(mode==1 || mode==2){
//				Network net = new ScaleFreeNetwork(N, gamma, kmin, N-1, 100);
//				net.SetWeight_to_Alpha(1.0);
//				net.setNode(false);
//				net.setEdge();
//
//				long temp_stamp = System.currentTimeMillis();
//				if(mode==1) net.LinkSalience_legacy();
//				if(mode==2) net.LinkSalience_legacy2();
//				System.out.println("section:" + t + "\t" + (System.currentTimeMillis()-temp_stamp));
//			}
//		}
//		System.out.println(System.currentTimeMillis()-stamp);

		Network net = new ScaleFreeNetwork(N, gamma, kmin, N-1, 100);
		net.SetWeight_to_Alpha(1.0);
		net.setNode(false);
		net.setEdge();
		net.setNeightbor();
		net.LinkSalience();
		net.LinkSalience_legacy2();
		boolean error = false;
		for(int i=0;i<net.M;i++){
			System.out.println(net.linkSalience[i] + "\t" + net.edgeList.get(i).linkSalience);
			if(net.linkSalience[i]!=net.edgeList.get(i).linkSalience){
				error=true;
//				break;
			}
		}
		System.out.println(error?"error!!":"");

	}

}
