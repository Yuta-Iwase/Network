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

	public ClusteringConfigrationNetwork(int[] isolated_DegreeList,int[] cluster_FragmentList,int loopLimit) {
		generate(isolated_DegreeList,cluster_FragmentList, loopLimit, true);
	}

	public ClusteringConfigrationNetwork(int[] isolated_DegreeList,int[] cluster_FragmentList,int loopLimit,boolean message){
		generate(isolated_DegreeList,cluster_FragmentList, loopLimit, message);
	}

	private void generate(int[] isolated_DegreeList,int[] cluster_FragmentList,int loopLimit,boolean message) {
		directed = false;
		doubleCount = false;
		N = isolated_DegreeList.length;
		this.degree = new int[N];

		ArrayList<Integer> isolatedStubArray = new ArrayList<Integer>();
		ArrayList<Integer> clusterFragmentArray = new ArrayList<Integer>();
		int[] clusteringFragmentRemainder = new int[N];
		int sum_isolatedStub = 0;
		int sum__clusteringStub = 0;
		int sumDegree=0;
		for(int i=0;i<N;i++){
			this.degree[i] = 0;

			for(int j=0;j<isolated_DegreeList[i];j++){
				isolatedStubArray.add(i);
			}
			this.degree[i] += isolated_DegreeList[i];
			sum_isolatedStub += isolated_DegreeList[i];

			for(int j=0;j<cluster_FragmentList[i];j++){
				clusterFragmentArray.add(i);
			}
			this.degree[i] += 2*cluster_FragmentList[i];
			sum__clusteringStub += 2*cluster_FragmentList[i];

			clusteringFragmentRemainder[i] = cluster_FragmentList[i];
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

				selfLoop= isolatedStubArray.get(targetEdgeA)==isolatedStubArray.get(targetEdgeB);
				multiple=false;
				cheakMultiple:for(int i=0;i<nowLine;i++){
					if((isolatedStubArray.get(targetEdgeA)==list[i][0]&&isolatedStubArray.get(targetEdgeB)==list[i][1])||
					   (isolatedStubArray.get(targetEdgeA)==list[i][1]&&isolatedStubArray.get(targetEdgeB)==list[i][0])){
						multiple=true;
						break cheakMultiple;
					}
				}
			}while(selfLoop || multiple);

			if(targetEdgeA >= targetEdgeB){
				list[nowLine][1]=isolatedStubArray.get(targetEdgeA);
				isolatedStubArray.remove(targetEdgeA);
				list[nowLine][0]=isolatedStubArray.get(targetEdgeB);
				isolatedStubArray.remove(targetEdgeB);
			}else{
				list[nowLine][1]=isolatedStubArray.get(targetEdgeB);
				isolatedStubArray.remove(targetEdgeB);
				list[nowLine][0]=isolatedStubArray.get(targetEdgeA);
				isolatedStubArray.remove(targetEdgeA);
			}

			disconnectedN -= 2;
			nowLine++;
		}while(disconnectedN>0);

		int fragment_base,fragment_A,fragment_B;
		int baseNode,targetNodeA,targetNodeB;
		boolean conflict;
		disconnectedN=sum__clusteringStub;
		nowLine=0;
		success = true;
		generateLoop: do{
			nowLoopLimit=loopLimit;
			do{
				fragment_base = rnd.nextInt(disconnectedN);
				fragment_A = rnd.nextInt(disconnectedN);
				fragment_B = rnd.nextInt(disconnectedN);

				baseNode = clusterFragmentArray.get(fragment_base);
				targetNodeA = clusterFragmentArray.get(fragment_A);
				targetNodeB = clusterFragmentArray.get(fragment_B);

				if(nowLoopLimit<=0){
					if(message) System.out.println("生成に失敗しました。");
					success = false;
					break generateLoop;
				}
				nowLoopLimit--;

				conflict = (baseNode==targetNodeA || baseNode==targetEdgeB || targetNodeA==targetNodeB);
				multiple=false;
				boolean malti_Base_A,malti_Base_B,malti_A_B;
				cheakMultiple:for(int i=0;i<nowLine;i++){
					malti_Base_A = ((baseNode==list[i][0]&&targetNodeA==list[i][1])||
							   (baseNode==list[i][1]&&targetNodeA==list[i][0]));
					malti_Base_B = ((baseNode==list[i][0]&&targetNodeB==list[i][1])||
							   (baseNode==list[i][1]&&targetNodeB==list[i][0]));
					malti_A_B = ((targetNodeA==list[i][0]&&targetNodeB==list[i][1])||
							   (targetNodeA==list[i][1]&&targetNodeB==list[i][0]));

					if(malti_Base_A || malti_Base_B || malti_A_B){
						multiple=true;
						break cheakMultiple;
					}
				}
			}while(conflict || multiple);


			// TODO
			if(targetEdgeA >= targetEdgeB){
				list[nowLine][1]=isolatedStubArray.get(targetEdgeA);
				isolatedStubArray.remove(targetEdgeA);
				list[nowLine][0]=isolatedStubArray.get(targetEdgeB);
				isolatedStubArray.remove(targetEdgeB);
			}else{
				list[nowLine][1]=isolatedStubArray.get(targetEdgeB);
				isolatedStubArray.remove(targetEdgeB);
				list[nowLine][0]=isolatedStubArray.get(targetEdgeA);
				isolatedStubArray.remove(targetEdgeA);
			}

			disconnectedN -= 2;
			nowLine++;
		}while(disconnectedN>0);
	}

	private void generate(int[] isolated_DegreeList,int[] cluster_FragmentList,int loopLimit) {
		generate(isolated_DegreeList,cluster_FragmentList, loopLimit, true);
	}

}
