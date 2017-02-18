
// 目的:
// 辺が有向的に定義されているものを無向的にする
// ※重みはその平均とする


public class AirportNetworkTransformer{
	public void makeUndirectedEdge(Network net){
		// 更新先の変数
		int[][] list = new int[net.M][2];
		double[] weight = new double[net.M];

		// 統合された辺を記憶する変数
		int[] used = new int[net.M*2];
		for(int i:used) used[i]=0; //(拡張for文の練習)

		// 現在の辺の記憶用変数
		int[] node = new int[2];
		// 現在の辺の番号
		int cEdge = 0;

		// 辺の統合処理
		for(int m=0;m<net.M*2;m++){
			if(used[m]==0){
				node[0] = net.list[m][0];
				node[1] = net.list[m][1];
				used[m]=1;
				for(int n=0;n<net.M*2;n++){
					if(used[n]==0){
						if((node[0]==net.list[n][0]&&node[1]==net.list[n][1]) ||
						    (node[0]==net.list[n][1]&&node[1]==net.list[n][0])){
							list[cEdge][0] = node[0];
							list[cEdge][1] = node[1];
							weight[cEdge]  = (net.weight[m]+net.weight[n])/2.0;

							cEdge++;
							used[n]=1;
							break;
						}
					}
				}
			}
		}

		// 変数を更新
		net.list   = list;
		net.weight = weight;
		net.doubleCount = false;
	}
}
