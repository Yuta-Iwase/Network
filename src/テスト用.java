public class テスト用{
	public static void main(String[] args) throws Exception{
		int N = 130;
//		int M = 215;
		MakePowerLaw dist;
		int sumDegree;
		int cc = 0;
		Network net;


		do {
			do {
				dist = new MakePowerLaw(N, 2.7, 2, N-1);
				sumDegree = 0;
				for(int i=0;i<N;i++) {
					sumDegree += dist.degree[i];
				}
				sumDegree /= 2;
			}while(sumDegree != 258);


			int limit = 0;
			do {
				net = new ConfigrationNetwork(dist.degree, 100,false);
			}while(!net.success && (limit++)<100);

			if(net.success) {
				net.setNode(false);
				net.setEdge();
				net.ConnectedCompornent();
				cc = net.count_cc;
			}else {
				cc = 0;
			}

			System.out.println(net.count_cc);

		}while(net.count_cc != 1);

//		do {
//			net = new RandomNetwork(N,0.03);
//
//			net.setNode(false);
//			net.setEdge();
//			net.ConnectedCompornent();
//			System.out.println(net.count_cc);
//
////			System.out.println(net.M);
////			if(net.M==215) {
////				System.out.println(net.count_cc);
////			}
////
////		}while(net.M!=215 || net.count_cc!=1);
//		}while(net.count_cc!=1);
////		}while(false);


		String fileName = "sfn.csv";
//		net.printList("C:\\Users\\Owner\\Desktop\\" + fileName);
		net.printList("" + fileName);
		System.out.println(net.count_cc);

	}
}
