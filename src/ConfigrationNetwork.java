import java.util.ArrayList;
import java.util.Random;

//課題:
//// ・有向グラフに未対応
//更新
//17/02/13
//・バグを修正
// (this.degree = new int[N];が書かれていなかった)

public class ConfigrationNetwork extends Network{

	public ConfigrationNetwork(int[] degree,int loopLimit){
		directed = false;
		doubleCount = false;
		N = degree.length;
		this.degree = new int[N];

		ArrayList<Integer> array = new ArrayList<Integer>();
		int sumDegree=0;
		for(int i=0;i<N;i++){
			for(int j=0;j<degree[i];j++){
				array.add(i);
			}
			this.degree[i] = degree[i];
			sumDegree += degree[i];
		}
		M = sumDegree/2;

		Random rnd = new Random();
		int disconnectedN=sumDegree;
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
					System.out.println("生成に失敗しました。");
					success = false;
					break generateLoop;
				}
				nowLoopLimit--;

				selfLoop= array.get(targetEdgeA)==array.get(targetEdgeB);
				multiple=false;
				cheakMultiple:for(int i=0;i<nowLine;i++){
					if((array.get(targetEdgeA)==list[i][0]&&array.get(targetEdgeB)==list[i][1])||
					   (array.get(targetEdgeA)==list[i][1]&&array.get(targetEdgeB)==list[i][0])){
						multiple=true;
						break cheakMultiple;
					}
				}
			}while(selfLoop || multiple);

			if(targetEdgeA >= targetEdgeB){
				list[nowLine][1]=array.get(targetEdgeA);
				array.remove(targetEdgeA);
				list[nowLine][0]=array.get(targetEdgeB);
				array.remove(targetEdgeB);
			}else{
				list[nowLine][1]=array.get(targetEdgeB);
				array.remove(targetEdgeB);
				list[nowLine][0]=array.get(targetEdgeA);
				array.remove(targetEdgeA);
			}

			disconnectedN -= 2;
			nowLine++;
		}while(disconnectedN>0);
	}

}
