import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

// 読み込んだデータからsalienceを計算

public class AirportTest18_2_2_RWSalience {
	public static void main(String[] args) throws Exception{
		double addWeight = 69.9;
		String folderName = "USAirport/";

		PrintWriter pw = new PrintWriter(new File(folderName + "USAirport_RWSalience_" + addWeight + ".csv"));

		NetworkForCSVFile net = new NetworkForCSVFile(folderName + "USAirport_reinforcedRW_" + addWeight + ".csv",false,true,false,false);
		net.setNode(false);

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

		long timeStamp = System.currentTimeMillis();
		for(int s=0 ; s<net.N ; s++){
			if(s%100==0){
				System.out.println("区間通過:s=" + s + "\t区間記録=" + (System.currentTimeMillis()-timeStamp)/1000.0 + "秒");
				timeStamp = System.currentTimeMillis();
			}
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
//					System.out.println(v + "\t" + w + "\t" + vwEdge);
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

		// 出力⑥
		// 各辺のSalienceを出力
		System.out.println();
		for(m=0;m<net.M;m++){
			pw.println(m + "," + edge.get(m).linkSalience);
			System.out.println(m + "\t" + edge.get(m).linkSalience);
		}

		pw.close();

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
