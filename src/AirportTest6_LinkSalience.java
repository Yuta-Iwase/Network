import java.util.ArrayList;


public class AirportTest6_LinkSalience {
	public static void main(String[] args) {
		GephiNetwork net = new GephiNetwork("S10b-14_BetAport_GephiPlot.csv", false);
		net.setNode();
		
		ArrayList<Integer> queue = new ArrayList<Integer>();
		ArrayList<Integer> stack = new ArrayList<Integer>();
		
		//distance from source
		int[] dist = new int[net.N];
		
		//list of predecessors on shortest paths from source
		ArrayList<ArrayList<Integer>> Pred = new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<net.N;i++) Pred.add(new ArrayList<Integer>());
		
//		int[] c_s = new int[net.M*2];
//		for(int i=0;i<c_s.length;i++) c_s[i]=0;
		ArrayList<Edge> edge = new ArrayList<Edge>();
		for(int i=0;i<net.M*2;i++)edge.add(new Edge());
		
		int v,w,m;
		
		for(int s=0 ; s<net.N ; s++){
			//// single-source shortest-paths problem
			// initialization
			for(int i=0 ; i<net.N ; i++){
				Pred.get(i).clear();
				dist[i] = Integer.MAX_VALUE;
			}
			dist[s] = 0;
			queue.add(s);
			
			while(!queue.isEmpty()){
				v = queue.get(0);
				queue.remove(0);
				
				stack.add(v);
				
				for(int neighbor=0 ; neighbor<net.nodeList.get(v).list.size() ; neighbor++){
					w = net.nodeList.get(v).list.get(neighbor).index;
					// path discovery
					if(dist[w] > dist[v] + 1){
						dist[w] = dist[v] + 1;
						
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
					if(dist[w] == dist[v]+1){
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
//						if((net.list[m][0]==v&&net.list[m][1]==w) || (net.list[m][0]==w&&net.list[m][1]==v)){
//							System.out.println("passed");
//							break;
//						}
						if(listNode[0]==node[0]&&listNode[1]==node[1])break;
					}
					edge.get(m).setNode(node[0], node[1]);
					edge.get(m).linkSalience = edge.get(m).linkSalience+1;
				}
			}
			
		}
		
//		int max=0;
//		for(m=0;m<net.M*2;m++){
//			max = Math.max(max, edge.get(m).linkSalience);
//		}
//		int[] distribution = new int[max+1];
//		for(int i=0;i<max;i++){
//			distribution[i]=0;
//		}
//		for(m=0;m<net.M*2;m++){
//			distribution[edge.get(m).linkSalience]++;
//		}
//		for(int i=0;i<max;i++){
//			System.out.println(i + "\t" + distribution[i]);
//		}
		System.out.println("*Vertices\t" + net.N);
		System.out.println("*Arcs");
		for(m=0;m<net.M*2;m++){
			if(edge.get(m).node[0]!=-1){
				System.out.println((edge.get(m).node[0]+1) + "\t" + (edge.get(m).node[1]+1) + "\t" + edge.get(m).linkSalience);
			}
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
