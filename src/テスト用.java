public class テスト用{
	public static void main(String[] args) throws Exception{
		int times = 1;
		int mode = 0;

		int N = 3000;
		double gamma = 2.7;
		int kmin = 4;


		long stamp = System.currentTimeMillis();
		for(int t=0;t<times;t++){
			if(mode==0){
				Network net = new ScaleFreeNetwork(N, gamma, kmin, N-1, 100);
				net.SetWeight_to_Alpha(1.0);
				net.setNeightbor();

				long temp_stamp = System.currentTimeMillis();
				net.LinkSalience();
				System.out.println("section:" + t + "\t" + (System.currentTimeMillis()-temp_stamp));

			}else if(mode==1 || mode==2){
				Network net = new ScaleFreeNetwork(N, gamma, kmin, N-1, 100);
				net.SetWeight_to_Alpha(1.0);
				net.setNode(false);
				net.setEdge();

				long temp_stamp = System.currentTimeMillis();
				if(mode==1) net.LinkSalience_legacy();
				if(mode==2) net.LinkSalience_legacy2();
				System.out.println("section:" + t + "\t" + (System.currentTimeMillis()-temp_stamp));
			}
		}

		System.out.println("\t" + (System.currentTimeMillis()-stamp));

//		for(int i=0;i<21;i++) {
//			double alpha = -1.0+0.1*i;
//			Network net = new ScaleFreeNetwork(N, gamma, kmin, N-1, 100);
//			net.setNeightbor();
//			net.SetWeight_to_Alpha(alpha);
//			net.LinkSalience();
//			int hs_count = 0;
//			for(int j=0;j<net.M;j++) {
//				if(net.linkSalience[j]>=N*0.9) hs_count++;
//			}
//			System.out.println(alpha + "\t" + ((double)hs_count)/net.M);
//		}

	}

}
