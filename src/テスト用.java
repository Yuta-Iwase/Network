public class テスト用{
	public static void main(String[] args) throws Exception{
		int times = 10;
		double gamma = 2.7;
		int m0 = 2;
		int N = 1000;
		double alpha = -2.0;
//		double[] alphaList = new double[] {-3.0,-4.0,-6.0,-10.0,-20.0,-40.0};
//		double alpha = -1234;

		double a = m0 * (gamma-3.0);
		double hs_thres = N*0.9;

//		double hs_frac = 0.0;
//
//
//		for(int al=0;al<alphaList.length;al++) {
//			alpha = alphaList[al];
//			long start = System.currentTimeMillis();
//			for(int t=0;t<times;t++) {
//				Network net = new DMSNetwork(N, m0, a, 100);
//				net.setNode(false);
//				net.setEdge();
//				net.setNeightbor();
//				net.SetWeight_to_Alpha(alpha);
//				net.disturb();
//
//				net.LinkSalience();
//				double current_hs_frac = 0.0;
//				for(int i=0;i<net.M;i++) {
//					if(net.linkSalience[i]>=hs_thres) {
//						current_hs_frac++;
//					}
//				}
//				current_hs_frac /= net.N;
////				System.out.println(current_hs_frac);
//
//				hs_frac += current_hs_frac;
//			}
//
//			hs_frac /= times;
//			System.out.println("finish: " + (System.currentTimeMillis()-start)*0.001 + "[s]");
//			System.out.println(alpha + "\t" + hs_frac);
//		}

		Network net = new DMSNetwork(N, m0, a, 100);
		net.setNode(false);
		net.setEdge();
		net.setNeightbor();
		net.SetWeight_to_Alpha(alpha);
		net.disturb();

		net.LinkSalience();

		GEXFStylePrinter gexf = new GEXFStylePrinter(N, net.list, false, "c:/desktop/network_alpha" + alpha + ".gexf");
		gexf.init_1st();
		gexf.printNode_2nd();
		gexf.printEdge_3rd(null, "link_salience", net.linkSalience);
		gexf.terminal_4th();



	}

}
