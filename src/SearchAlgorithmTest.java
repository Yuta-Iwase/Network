import java.util.ArrayList;


public class SearchAlgorithmTest {
	public static void main(String[] args) {
		int N=10000;
		int[] degree = new int[N];
		double gamma=2.8;

		double r;
		int sum=0;
		for(int i=0;i<N;i++){
			r = Math.random();
			degree[i] = (int)(Math.pow(1-r, 1/(1-gamma)));
			sum += degree[i];
		}
		if(sum%2==1)degree[0]++;

		ConfigurationModel2 net = new ConfigurationModel2(degree,10*N);
		net.sort();
		
		// 探索用変数
		int[][] list = net.list;
		ArrayList<Integer> queue = new ArrayList<Integer>();
		boolean[] visited = new boolean[N];
		for(int i=0;i<N;i++) visited[i]=false;
		int currentNode;
		
		// プロット用変数
		int compN = 0;
		int nodes;
		int bigCompIndex=0;
		int maxNodes=0;
		
		for(int i=0;i<N;i++){
			if(!visited[i]){
				System.out.print("連結成分" + (++compN) + "\t");
				queue.add(i);
				visited[i] = true;
				nodes=1;
				while(!queue.isEmpty()){
					currentNode = queue.get(0);
					queue.remove(0);
					for(int targetLink=0 ; targetLink<list.length ; targetLink++){
						if(list[targetLink][0] < currentNode){
							if(list[targetLink][1] == currentNode && !visited[list[targetLink][0]]){
								queue.add(list[targetLink][0]);
								visited[list[targetLink][0]] = true;
								nodes++;
							}
						}else if(list[targetLink][0] == currentNode){
							if(!visited[list[targetLink][1]]){
								queue.add(list[targetLink][1]);
								visited[list[targetLink][1]] = true;
								nodes++;
							}
						}else break;
					}
				}
				System.out.println("頂点数:" + nodes);
				if(nodes > maxNodes){
					bigCompIndex = compN;
					maxNodes = nodes;
				}
			}
		}
		
		// 巨大連結成分プロット
		System.out.println("巨大連結成分:" + bigCompIndex + " 頂点数=" + maxNodes);
		
		// 比較用にcsv形式で隣接リストを出力
		net.printList("SearchAlgorithmTestNetwork.csv");
	}
}
