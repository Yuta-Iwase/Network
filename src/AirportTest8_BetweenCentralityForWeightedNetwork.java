import java.util.ArrayList;

public class AirportTest8_BetweenCentralityForWeightedNetwork extends Network {
	public static void main(String[] args) {
		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv", false, true, true, true);
		net.setNode();
		new AirportNetworkTransformer().makeUndirectedEdge(net);

		// 変数定義
		Node currentNode;
		ArrayList<Node> stack = new ArrayList<Node>();
		ArrayList<Node> queue = new ArrayList<Node>();
		double[] distance = new double[net.N];
		double[] sigma = new double[net.N];
		double[] delta = new double[net.N];
		int v, w,vwEdge, x, y;
		// P:pの集合(lenght=N)
		// └p[i]:頂点iのリスト(要素:Node)
		// └Node
		//
		// ↑この構造を作る
		ArrayList<ArrayList<Node>> P = new ArrayList<ArrayList<Node>>();
		for (int n = 0; n < net.N; n++) {
			P.add(new ArrayList<Node>());
		}

		for (int s = 0; s < net.N; s++) {
			// 初期化
			stack.clear();
			queue.clear();
			for (int i = 0; i < net.N; i++) {
				distance[i] = -1;
				sigma[i] = 0;
				P.get(i).clear();
			}

			// 頂点sに対する処理
			distance[s] = 0;
			sigma[s] = 1;
			queue.add(net.nodeList.get(s));

			// 主となる処理
			while (!queue.isEmpty()) {
				currentNode = queue.get(0);
				v = currentNode.index;
				stack.add(currentNode);
				queue.remove(0);
				// 現頂点の隣接頂点についてループ
				for (int neightbor = 0; neightbor < currentNode.list.size(); neightbor++) {
					// 現ループの隣接頂点のindexをwとおく
					w = currentNode.list.get(neightbor).index;
					// 【追加部分】リンク(v,w)を検索
					vwEdge = net.searchEdge(v,w);
					// wが未訪問のとき
					if (distance[w] < 0) {
						queue.add(net.nodeList.get(w));
						distance[w] = distance[v] + 1.0/net.weight[vwEdge];
					}
					// sからwへの最短経路にvが含まれるとき
					if (distance[w] == distance[v] + 1.0/net.weight[vwEdge]) {
						sigma[w] += sigma[v];
						P.get(w).add(currentNode);
					}
				}
			}
			// 初期化
			for (int n = 0; n < net.N; n++)
				delta[n] = 0;
			// stackは頂点sからの距離が遠い順で返す
			while (!stack.isEmpty()) {
				x = stack.get(stack.size() - 1).index;
				stack.remove(stack.size() - 1);
				for (int i = 0; i < P.get(x).size(); i++) {
					y = P.get(x).get(i).index;
					delta[y] += (sigma[y] / sigma[x]) * (1 + delta[x]);
				}
				if (x != s) {
					net.nodeList.get(x).betweenCentrality += delta[x];
				}
			}
		}

		// 結果表示
		for (int n = 0; n < net.N; n++) {
			System.out.println(n + "," + net.nodeList.get(n).betweenCentrality);
		}

		ArrayList<Double> bc = new ArrayList<Double>();
		ArrayList<Double> sorted_bc = new ArrayList<Double>();
		ArrayList<Integer> distribution = new ArrayList<Integer>();
		ArrayList<Integer> removeIndex = new ArrayList<Integer>();
		double min_bc;
		int currentDist,r;

		for(int n=0;n<net.N;n++){
			bc.add(net.nodeList.get(n).betweenCentrality/2.0);
		}
		while(!bc.isEmpty()){
			min_bc = Double.MAX_VALUE;
			for(int i=0;i<bc.size();i++){
				min_bc = Math.min(min_bc, bc.get(i));
			}
			sorted_bc.add(min_bc);
			currentDist=0;
			removeIndex.clear();
			for(int i=0;i<bc.size();i++){
				if(min_bc == bc.get(i)){
					currentDist++;
					removeIndex.add(i);
				}
			}
			distribution.add(currentDist);
			for(int i=removeIndex.size()-1 ; i>=0 ; i--){
				r=removeIndex.get(i);
				bc.remove(r);
			}
		}
		for(int i=0;i<sorted_bc.size();i++){
//			System.out.println(sorted_bc.get(i) + "\t" + distribution.get(i));
		}
	}
}
