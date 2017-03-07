import java.util.ArrayList;

// 8/2修正;
// 40行目:∞の定義をint型からDouble型に変更
// 46行～:queueから取り出す頂点を先頭のものではなく
//        distが最小となるものを取り出す
// 8/5修正;
// 15行目: AirportNetworkTransformerにより辺の定義を整備
//   全体: それに伴い net.M*2 を net.M に変更

public class AirportTest7_LinkSalience_ForWeighted {
	public static void main(String[] args) {
		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv",false,true,true,true);
		net.setNode();
		new AirportNetworkTransformer().makeUndirectedEdge(net);
		net.setLabel("空港ラベル表.csv");

		ArrayList<Integer> queue = new ArrayList<Integer>();
		ArrayList<Integer> stack = new ArrayList<Integer>();

		//distance from source
		double[] dist = new double[net.N];

		//list of predecessors on shortest paths from source
		ArrayList<ArrayList<Integer>> Pred = new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<net.N;i++) Pred.add(new ArrayList<Integer>());

		ArrayList<Edge> edge = new ArrayList<Edge>();
		for(int i=0;i<net.M;i++)edge.add(new Edge());

		int v,w,m,minIndex,vwEdge;
		Double minDis;


		for(int s=0 ; s<net.N ; s++){
			//// single-source shortest-paths problem
			// initialization
			for(int i=0 ; i<net.N ; i++){
				Pred.get(i).clear();
				dist[i] = Double.MAX_VALUE; // 【修正箇所】念のため右辺をInteger.MAX_VALUEから変更
			}
			dist[s] = 0;
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

						Pred.get(w).clear();
					}
					//path counting
					if(dist[w] == dist[v]+1.0/net.weight[vwEdge]){
						Pred.get(w).add(v);
					}
				}
			}

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
					edge.get(m).linkSalience = edge.get(m).linkSalience+1;
				}
			}

		}

		// 出力①
		// 分布を出力
		int max=0;
		for(m=0;m<net.M;m++){
			max = Math.max(max, edge.get(m).linkSalience);
		}
		int[] distribution = new int[max+1];
		for(int i=0;i<=max;i++){
			distribution[i]=0;
		}
		for(m=0;m<net.M;m++){
			distribution[edge.get(m).linkSalience]++;
		}
		for(int i=0;i<=max;i++){
			if(distribution[i] != 0){
				// 規格化
//				System.out.println(((double)i)/net.N + "\t" + ((double)distribution[i]/net.M));
				// 規格化なし
//				System.out.println(((double)i) + "\t" + ((double)distribution[i]));
			}
		}
		// 出力②
		// 各辺を重み付きでプロット(Pajek用)
//		System.out.println("*Vertices\t" + net.N);
//		System.out.println("*Arcs");
//		for(m=0;m<net.M;m++){
//			if(edge.get(m).node[0]!=-1){
//				System.out.println((edge.get(m).node[0]+1) + "\t" + (edge.get(m).node[1]+1) + "\t" + edge.get(m).linkSalience);
//			}
//		}

		// 出力③
		// salienceが0以外のものを出力
//		for(int i=0;i<net.N;i++) System.out.println(i);
//		for(m=0;m<net.M;m++){
//			if(edge.get(m).node[0]!=-1){
//				if( edge.get(m).linkSalience != 0.0){
//					System.out.println((edge.get(m).node[0]) + "\t" + (edge.get(m).node[1]));
//				}
//			}
//		}

		// 出力④
		// salience>=0.5のものをラベル状態で出力
//		for(int i=0;i<net.N;i++) System.out.println(net.label[i]);
//		for(m=0;m<net.M;m++){
//			if(edge.get(m).node[0]!=-1){
//				if( edge.get(m).linkSalience >= 0.5*net.N){
////					System.out.println(net.label[edge.get(m).node[0]] + "\t" + net.label[edge.get(m).node[1]]);
//					System.out.println(edge.get(m).node[0] + "," + edge.get(m).node[1]);
//				}
//			}
//		}

		// 出力⑤
		// 強引にgephi用の重み付きネットワークを作る
//		for(m=0;m<net.M;m++){
//			if(edge.get(m).node[0]!=-1){
//				for(int i=0;i<edge.get(m).linkSalience;i++){
//					System.out.println((edge.get(m).node[0]+1) + "\t" + (edge.get(m).node[1]+1));
//				}
//			}
//		}

		// 出力⑥
		// 各辺のSalienceを出力
		for(m=0;m<net.M;m++){
			System.out.println(m + "\t" + edge.get(m).linkSalience);
		}

}

	private static class Edge{
		int linkSalience;
		int[] node = new int[2];
		Edge() {
			linkSalience=0;
			node[0] = -1;
			node[1] = -1;
		}

		void setNode(int n1,int n2){
			node[0] = Math.min(n1, n2);
			node[1] = Math.max(n1, n2);
		}
	}
}
