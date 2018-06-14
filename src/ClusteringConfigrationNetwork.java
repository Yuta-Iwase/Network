import java.util.ArrayList;
import java.util.Arrays;
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
		generate(isolated_DegreeList,cluster_FragmentList, loopLimit);
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
		int sum__clusteringFragment = 0;
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
			sum__clusteringFragment += cluster_FragmentList[i];

			clusteringFragmentRemainder[i] = cluster_FragmentList[i];
		}

		switch(sum__clusteringFragment%3){
		case 1:
			clusterFragmentArray.add(0);
			clusterFragmentArray.add(1);
			sum__clusteringFragment += 2;
			break;

		case 2:
			clusterFragmentArray.add(0);
			sum__clusteringFragment += 1;
			break;
		}


		sumDegree = sum_isolatedStub + 2*sum__clusteringFragment;
		M = sumDegree/2;

		Random rnd = new Random();
		int disconnectedN=sum_isolatedStub;
		int targetEdgeA,targetEdgeB;
		int nowLine=0;
		int nowLoopLimit;
		boolean selfLoop,multiple;
		success = true;
		list = new int[M][2];
		if(isolatedStubArray.size()>0) {
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
		}


		int fragment_base,fragment_A,fragment_B;
		int baseNode,targetNodeA,targetNodeB;
		boolean conflict;
		disconnectedN=clusterFragmentArray.size();
		success = true;
		if(clusterFragmentArray.size()>0) {
			generateLoop: do{
				nowLoopLimit=loopLimit;
				do{
					fragment_base = rnd.nextInt(clusterFragmentArray.size());
					fragment_A = rnd.nextInt(clusterFragmentArray.size());
					fragment_B = rnd.nextInt(clusterFragmentArray.size());

					baseNode = clusterFragmentArray.get(fragment_base);
					targetNodeA = clusterFragmentArray.get(fragment_A);
					targetNodeB = clusterFragmentArray.get(fragment_B);

					if(nowLoopLimit<=0){
						if(message) System.out.println("生成に失敗しました。");
						success = false;
						break generateLoop;
					}
					nowLoopLimit--;

					conflict = (baseNode==targetNodeA || baseNode==targetNodeB || targetNodeA==targetNodeB);
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

				int[] fragmentList = new int[3];
				fragmentList[0] = fragment_base;
				fragmentList[1] = fragment_A;
				fragmentList[2] = fragment_B;
				for(int i=0;i<3;i++) {
					int currentFragment1 = fragmentList[i%3];
					int currentFragment2 = fragmentList[(i+1)%3];
					if(currentFragment1 >= currentFragment2){
						list[nowLine][1]=clusterFragmentArray.get(currentFragment1);
						list[nowLine][0]=clusterFragmentArray.get(currentFragment2);
					}else{
						list[nowLine][1]=clusterFragmentArray.get(currentFragment2);
						list[nowLine][0]=clusterFragmentArray.get(currentFragment1);
					}
					nowLine++;
				}
				Arrays.sort(fragmentList);
				clusterFragmentArray.remove(fragmentList[2]);
				clusterFragmentArray.remove(fragmentList[1]);
				clusterFragmentArray.remove(fragmentList[0]);
			}while(!clusterFragmentArray.isEmpty());
		}
		
	}

	private void generate(int[] isolated_DegreeList,int[] cluster_FragmentList,int loopLimit) {
		generate(isolated_DegreeList,cluster_FragmentList, loopLimit, true);
	}

}
