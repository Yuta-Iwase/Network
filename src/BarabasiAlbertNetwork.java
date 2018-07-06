import java.util.ArrayList;

public class BarabasiAlbertNetwork extends Network{
	public BarabasiAlbertNetwork(int N0, int N, int insertEdges, long seed) {
		this.N = N;
		M = N0*(N0-1)/2 + (N-N0)*insertEdges;
		list = new int[M][2];
		degree = new int[N];

		// 完全グラフの構築
		int currentLine = 0;
		for(int i=0;i<N0;i++){
			for(int j=i+1;j<N0;j++){
				list[currentLine][0] = i;
				list[currentLine][1] = j;
				currentLine++;
			}
			degree[i] = N0-1;
		}
		int sumDegree = N0*(N0-1);

		// ネットワーク成長ダイナミクス(優先的接続)
		ArrayList<Integer> chosedNodeList = new ArrayList<Integer>();
		int rnd,x,y;
		for(int i=N0;i<N;i++){
			chosedNodeList.clear();
			while(chosedNodeList.size()<insertEdges){
				for(int m=0;m<insertEdges;m++){
					rnd = (int)(sumDegree*Math.random());
					x = rnd/2;
					y = rnd%2;
					if(chosedNodeList.contains(list[x][y])){
						chosedNodeList.clear();
						break;
					}else{
						chosedNodeList.add(list[x][y]);
					}
				}
			}

			for(int m=0;m<insertEdges;m++){
				list[currentLine][0] = i;
				list[currentLine][1] = chosedNodeList.get(m);
				degree[list[currentLine][0]]++;
				degree[list[currentLine][1]]++;
				currentLine++;
			}
			
			sumDegree += insertEdges*2;
		}
		
	}


}
