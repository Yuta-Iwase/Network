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

public class ConfigrationNetwork extends Network{
	int generateCount = 0;

	public ConfigrationNetwork(int[] degree,int loopLimit) {
		generate(this, degree, loopLimit);
	}

	public ConfigrationNetwork(int[] degree,int loopLimit,boolean message){
		generate(this, degree, loopLimit, message);
	}

	/**
	 * ScaleFreeNetwork.class用
	 * @param N
	 * @param gamma
	 * @param minDegree
	 * @param maxDegree
	 * @param loopLimit
	 */
	protected ConfigrationNetwork(int N, double gamma, int minDegree, int maxDegree, int loopLimit) {
		do {
			MakePowerLaw dist = new MakePowerLaw(N, gamma, minDegree, maxDegree);
			generate(this, dist.degree, loopLimit, false);
			generateCount++;
		}while(!success);
	}


	public static void generate(Network net, int[] degree,int loopLimit,boolean message) {
		net.directed = false;
		net.doubleCount = false;
		net.N = degree.length;
		net.degree = new int[net.N];

		ArrayList<Integer> array = new ArrayList<Integer>();
		int sumDegree=0;
		for(int i=0;i<net.N;i++){
			for(int j=0;j<degree[i];j++){
				array.add(i);
			}
			net.degree[i] = degree[i];
			sumDegree += degree[i];
		}
		if(sumDegree%2==1){
			array.add(0);
			net.degree[0]++;
			sumDegree++;
		}
		net.M = sumDegree/2;

		Random rnd = new Random();
		int disconnectedN=sumDegree;
		int targetEdgeA,targetEdgeB;
		int nowLine=0;
		int nowLoopLimit;
		boolean selfLoop,multiple;
		net.success = true;
		net.list = new int[net.M][2];
		generateLoop: do{

			nowLoopLimit=loopLimit;
			do{
				targetEdgeA=rnd.nextInt(disconnectedN);
				targetEdgeB=rnd.nextInt(disconnectedN);

				if(nowLoopLimit<=0){
					if(message) System.out.println("生成に失敗しました。");
					net.success = false;
					break generateLoop;
				}
				nowLoopLimit--;

				selfLoop= array.get(targetEdgeA)==array.get(targetEdgeB);
				multiple=false;
				cheakMultiple:for(int i=0;i<nowLine;i++){
					if((array.get(targetEdgeA)==net.list[i][0]&&array.get(targetEdgeB)==net.list[i][1])||
					   (array.get(targetEdgeA)==net.list[i][1]&&array.get(targetEdgeB)==net.list[i][0])){
						multiple=true;
						break cheakMultiple;
					}
				}
			}while(selfLoop || multiple);

			if(targetEdgeA >= targetEdgeB){
				net.list[nowLine][1]=array.get(targetEdgeA);
				array.remove(targetEdgeA);
				net.list[nowLine][0]=array.get(targetEdgeB);
				array.remove(targetEdgeB);
			}else{
				net.list[nowLine][1]=array.get(targetEdgeB);
				array.remove(targetEdgeB);
				net.list[nowLine][0]=array.get(targetEdgeA);
				array.remove(targetEdgeA);
			}

			disconnectedN -= 2;
			nowLine++;
		}while(disconnectedN>0);
	}

	private void generate(Network net, int[] degree,int loopLimit) {
		generate(net, degree, loopLimit, true);
	}

}
