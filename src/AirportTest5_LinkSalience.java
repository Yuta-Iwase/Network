import java.util.ArrayList;


public class AirportTest5_LinkSalience {
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
		
		int[] c_s = new int[net.M*2];
		for(int i=0;i<net.N;i++) c_s[i]=0;
		
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
			
			//// accumulation
			while(!stack.isEmpty()){
				w = stack.get(stack.size()-1);
				stack.remove(stack.size()-1);
				
				for(int i=0 ; i<Pred.get(w).size() ; i++){
					v = Pred.get(w).get(i);
					for(m=0;m<net.M;m++){
						if((net.list[m][0]==v&&net.list[m][1]==w) || (net.list[m][0]==w&&net.list[m][1]==v))break;
					}
					c_s[m] = c_s[m]+1;
				}
			}
			
		}
		
		int max=0;
		for(m=0;m<net.M;m++){
			max = Math.max(max, c_s[m]);
		}
		int[] distribution = new int[max+1];
		for(int i=0;i<max;i++){
			distribution[i]=0;
		}
		for(m=0;m<net.M;m++){
			distribution[c_s[m]]++;
		}
		for(int i=0;i<max;i++){
//			System.out.println(i + "\t" + distribution[i]);
		}
		
		for(int i=0;i<net.M;i++){
			System.out.println(i + "," + c_s[i]);
		}
		
	}
}
