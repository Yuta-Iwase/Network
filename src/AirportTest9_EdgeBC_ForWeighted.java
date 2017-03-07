import java.util.ArrayList;

public class AirportTest9_EdgeBC_ForWeighted {
	public static void main(String[] args) {
		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv",false , true, true,true);
		net.setNode();
		new AirportNetworkTransformer().makeUndirectedEdge(net);

		net.setLabel("空港ラベル表.csv");

		ArrayList<Integer> queue = new ArrayList<Integer>();
		ArrayList<Integer> stack = new ArrayList<Integer>();

		double[] sigma = new double[net.N];
		double[] delta = new double[net.N];
		for(int i=0;i<net.N;i++)sigma[i]=0;

		//distance from source
		double[] dist = new double[net.N];

		//list of predecessors on shortest paths from source
		ArrayList<ArrayList<Integer>> Pred = new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<net.N;i++) Pred.add(new ArrayList<Integer>());

		ArrayList<Edge> edge = new ArrayList<Edge>();
		for(int i=0;i<net.M;i++)edge.add(new Edge());

		int v,w,m,minIndex,vwEdge;
		double c;
		double[] node_bc = new double[net.N];
		for(int i=0;i<net.N;i++) node_bc[i]=0;
		Double minDis;


		for(int s=0 ; s<net.N ; s++){
			//// single-source shortest-paths problem
			// initialization
			for(int i=0 ; i<net.N ; i++){
				Pred.get(i).clear();
				dist[i] = Double.MAX_VALUE; // 【修正箇所】念のため右辺をInteger.MAX_VALUEから変更
				sigma[i] = 0;
			}
			dist[s] = 0;
			sigma[s] = 1;
			queue.add(s);

			while(!queue.isEmpty()){
				// 【修正箇所】
				// queueからdist[v]が最小となるものを取り出す
				minDis = Double.MAX_VALUE - 1.0;
				v = -1;
				minIndex = -1;
				for(int i=0;i<queue.size();i++){
					if(minDis > dist[queue.get(i)]){
						minDis = dist[queue.get(i)];
						v = queue.get(i);
						minIndex = i;
					}
				}
				queue.remove(minIndex);
				stack.add(v);

				for(int neighbor=0 ; neighbor<net.nodeList.get(v).list.size() ; neighbor++){
					w = net.nodeList.get(v).list.get(neighbor).index;
					// path discovery
					vwEdge = net.searchEdge(v,w);
					if(dist[w] > dist[v] + 1.0/net.weight[vwEdge]){
						dist[w] = dist[v] + 1.0/net.weight[vwEdge];

						// insert/update w
						queue.add(w);
						for(int i=0;i<queue.size()-1;i++){
							if(queue.get(i) == w){
								queue.remove(i);
								break;
							}
						}

						sigma[w] = 0;

						Pred.get(w).clear();
					}
					//path counting
					if(dist[w] == dist[v]+1.0/net.weight[vwEdge]){
						sigma[w] = sigma[w] + sigma[v];
						Pred.get(w).add(v);
					}
				}
			}

			for(int i=0;i<delta.length;i++)delta[i]=0.0;
			int[] node = new int[2];
			int[] listNode = new int[2];
			//// accumulation
			while(!stack.isEmpty()){
				w = stack.get(stack.size()-1);
				stack.remove(stack.size()-1);

				for(int i=0 ; i<Pred.get(w).size() ; i++){
					v = Pred.get(w).get(i);
					node[0] = Math.min(v,w);
					node[1] = Math.max(v,w);
					for(m=0;m<net.M;m++){
						listNode[0] = Math.min(net.list[m][0],net.list[m][1]);
						listNode[1] = Math.max(net.list[m][0],net.list[m][1]);
						if(listNode[0]==node[0]&&listNode[1]==node[1])break;
					}
					edge.get(m).setNode(node[0], node[1]);

					c = (sigma[v]/sigma[w]) * (1.0+delta[w]);
					edge.get(m).bc = edge.get(m).bc + c;
					delta[v] = delta[v] + c;
				}

				if(w!=s){
					node_bc[w] = node_bc[w] + delta[w];
				}
			}

		}

		// 出力①
		// 分布を出力
		int max=0;
		for(m=0;m<net.M;m++){
			max = Math.max(max, (int)edge.get(m).bc);
		}
		int[] distribution = new int[max+1];
		for(int i=0;i<=max;i++){
			distribution[i]=0;
		}
		for(m=0;m<net.M;m++){
			distribution[(int)edge.get(m).bc]++;
		}
		for(int i=0;i<=max;i++){
			if(((double)distribution[i]/net.M) !=0.0){
//				System.out.println(((double)i)/max + "\t" + ((double)distribution[i]/net.M));
				// ↓y軸規格化解除(2016/11/30使用)
//				System.out.println(((double)i) + "\t" + ((double)distribution[i]));
			}
		}

		// 出力②
		// 各辺に対するBCを出力
		for(int i=0;i<net.M;i++){
			System.out.println(i + "," + edge.get(i).bc);
		}

}

	private static class Edge{
		double bc;
		int[] node = new int[2];
		Edge() {
			bc=0;
			node[0] = -1;
			node[1] = -1;
		}

		void setNode(int n1,int n2){
			node[0] = Math.min(n1, n2);
			node[1] = Math.max(n1, n2);
		}
	}
}
