import java.util.ArrayList;
import java.util.Random;

//課題:
//// ・有向グラフに未対応
//更新
//17/02/13
//・バグを修正
// (this.degree = new int[N];が書かれていなかった)
//18/01/21
//・生成失敗のメッセージがうっとおしいのでオプションで出さないように修正

public class ClusteringConfigrationNetwork extends Network{

	public ClusteringConfigrationNetwork(int[] isolated_DegreeList,int[] clustering_DegreeList,int loopLimit) {
		generate(isolated_DegreeList,clustering_DegreeList, loopLimit, true);
	}

	public ClusteringConfigrationNetwork(int[] isolated_DegreeList,int[] clustering_DegreeList,int loopLimit,boolean message){
		generate(isolated_DegreeList,clustering_DegreeList, loopLimit, message);
	}

	private void generate(int[] isolated_DegreeList,int[] clustering_DegreeList,int loopLimit,boolean message) {
		directed = false;
		doubleCount = false;
		N = isolated_DegreeList.length;
		this.degree = new int[N];

		ArrayList<Integer> isolatedStubList = new ArrayList<Integer>();
		ArrayList<Integer> clusteringStubList = new ArrayList<Integer>();
		int[] clusteringStubremainder = new int[N];
		int sum_isolatedStub = 0;
		int sum__clusteringStub = 0;
		int sumDegree=0;
		for(int i=0;i<N;i++){
			this.degree[i] = 0;

			for(int j=0;j<isolated_DegreeList[i];j++){
				isolatedStubList.add(i);
			}
			this.degree[i] += isolated_DegreeList[i];
			sum_isolatedStub += isolated_DegreeList[i];

			for(int j=0;j<clustering_DegreeList[i];j++){
				clusteringStubList.add(i);
			}
			this.degree[i] += clustering_DegreeList[i];
			sum__clusteringStub += clustering_DegreeList[i];

			clusteringStubremainder[i] = clustering_DegreeList[i];
		}
		sumDegree = sum_isolatedStub + sum__clusteringStub;
		M = sumDegree/2;

		Random rnd = new Random();
		int disconnectedN=sum_isolatedStub;
		int targetEdgeA,targetEdgeB;
		int nowLine=0;
		int nowLoopLimit;
		boolean selfLoop,multiple;
		success = true;
		list = new int[M][2];
		generateLoop: do{
			nowLoopLimit=loopLimit;
			do{
				targetEdgeA=rnd.nextInt(disconnectedN);
				targetEdgeB=rnd.nextInt(disconnectedN);

				if(nowLoopLimit<=0){
					if(message) System.out.println("生成に失敗しました。");
					success = false;
					break generateLoop;
				}
				nowLoopLimit--;

				selfLoop= isolatedStubList.get(targetEdgeA)==isolatedStubList.get(targetEdgeB);
				multiple=false;
				cheakMultiple:for(int i=0;i<nowLine;i++){
					if((isolatedStubList.get(targetEdgeA)==list[i][0]&&isolatedStubList.get(targetEdgeB)==list[i][1])||
					   (isolatedStubList.get(targetEdgeA)==list[i][1]&&isolatedStubList.get(targetEdgeB)==list[i][0])){
						multiple=true;
						break cheakMultiple;
					}
				}
			}while(selfLoop || multiple);

			if(targetEdgeA >= targetEdgeB){
				list[nowLine][1]=isolatedStubList.get(targetEdgeA);
				isolatedStubList.remove(targetEdgeA);
				list[nowLine][0]=isolatedStubList.get(targetEdgeB);
				isolatedStubList.remove(targetEdgeB);
			}else{
				list[nowLine][1]=isolatedStubList.get(targetEdgeB);
				isolatedStubList.remove(targetEdgeB);
				list[nowLine][0]=isolatedStubList.get(targetEdgeA);
				isolatedStubList.remove(targetEdgeA);
			}

			disconnectedN -= 2;
			nowLine++;
		}while(disconnectedN>0);

		int baseNodeOfCluster_relIndex;
		int baseNodeOfCluster_absIndex;
		disconnectedN=sum__clusteringStub;
		nowLine=0;
		success = true;
		generateLoop: do{
			nowLoopLimit=loopLimit;
			do{
				// TODO
				baseNodeOfCluster_relIndex = rnd.nextInt(disconnectedN);
				baseNodeOfCluster_absIndex = clusteringStubList.get(baseNodeOfCluster_relIndex);

				if(nowLoopLimit<=0){
					if(message) System.out.println("生成に失敗しました。");
					success = false;
					break generateLoop;
				}
				nowLoopLimit--;

//				selfLoop= isolatedStubList.get(targetEdgeA)==isolatedStubList.get(targetEdgeB);
				multiple=false;
//				int
				boolean malti_Base_A,malti_Base_B,malti_A_B;
				cheakMultiple:for(int i=0;i<nowLine;i++){
//					int node_a = ;
//					malti_Base_A = ((clusteringStubList.get(targetEdgeA)==list[i][0]&&clusteringStubList.get(targetEdgeB)==list[i][1])||
//							   (clusteringStubList.get(targetEdgeA)==list[i][1]&&clusteringStubList.get(targetEdgeB)==list[i][0]));




					if((isolatedStubList.get(targetEdgeA)==list[i][0]&&isolatedStubList.get(targetEdgeB)==list[i][1])||
					   (isolatedStubList.get(targetEdgeA)==list[i][1]&&isolatedStubList.get(targetEdgeB)==list[i][0])){
						multiple=true;
						break cheakMultiple;
					}
				}
			}while(multiple);

			if(targetEdgeA >= targetEdgeB){
				list[nowLine][1]=isolatedStubList.get(targetEdgeA);
				isolatedStubList.remove(targetEdgeA);
				list[nowLine][0]=isolatedStubList.get(targetEdgeB);
				isolatedStubList.remove(targetEdgeB);
			}else{
				list[nowLine][1]=isolatedStubList.get(targetEdgeB);
				isolatedStubList.remove(targetEdgeB);
				list[nowLine][0]=isolatedStubList.get(targetEdgeA);
				isolatedStubList.remove(targetEdgeA);
			}

			disconnectedN -= 2;
			nowLine++;
		}while(disconnectedN>0);
	}

	private void generate(int[] isolated_DegreeList,int[] clustering_DegreeList,int loopLimit) {
		generate(isolated_DegreeList,clustering_DegreeList, loopLimit, true);
	}

}
