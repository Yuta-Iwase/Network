import java.io.File;
import java.io.PrintWriter;


// 17/02/28 バグ修正

public class AirportTest16_1_MakeBimodal {
	public static void main(String[] args) throws Exception{
		//// 各数値定義
		// コンフィグ用
		final int N = 1000;
		final double gamma = 2.7;
		final int minDegree = 2;
		final int maxDegree = N-1;
		// ランダマイズ用
		final int walkN = N*100000;
		String fileName = "ReinforcedRW用コンフィグネットワーク_highWalk.csv";

		//// コンフィグレーション
		// べき則に従う次数リストを作る
		int[] degree = new int[N];
		double c1 = Math.pow(maxDegree, 1-gamma) - Math.pow(minDegree, 1-gamma);
		double c2 = Math.pow(minDegree, 1-gamma);
		double c3 = (1.0/(1.0-gamma));
		int sum = 0;
		for(int i=0;i<N;i++){
			degree[i] = (int)Math.pow(Math.random()*c1+c2,c3);
			sum += degree[i];
		}
		if(sum%2==1) degree[0]+=1;
		// ネットワークオブジェクト生成
		ConfigrationNetwork net = new ConfigrationNetwork(degree, 100000);
		int limiter=0;
//		while(!net.success&&limiter<1000){
		while(!net.success){
			sum = 0;
			for(int i=0;i<N;i++){
				degree[i] = (int)Math.pow(Math.random()*c1+c2,c3);
				sum += degree[i];
			}
			if(sum%2==1) degree[0]+=1;

			net = new ConfigrationNetwork(degree, 100000);

			limiter++;

			System.out.print("生成" + limiter + "回目: ");
		}
		net.weight = new double[net.M];


		// 書き込み用オブジェクト定義
		PrintWriter pw = new PrintWriter(new File(fileName));
		// 基本データ読み込み・データ活用のための準備
		net.setNode(false);
		net.setEdge();
		// 作業変数定義
		int currentNodeIndex = (int)(net.N * Math.random());
		int selectedEdge,nextNodeIndex;
		int sumW;
		double r,threshold;
		double[] newWeight = new double[net.M];
		for(int i=0;i<net.M;i++) newWeight[i]=1.0;
		for(int t=0;t<walkN;t++){
			Network.Node currentNode = net.nodeList.get(currentNodeIndex);
			// ここが各ランダムウォークで変化する内容(辺の選択方法)
			sumW = 0;
			for(int i=0;i<currentNode.eList.size();i++) sumW+=newWeight[currentNode.eList.get(i).index];
			r = (sumW*Math.random());
			selectedEdge = 0;
			threshold = newWeight[currentNode.eList.get(0).index];
			while(r > threshold){
				selectedEdge++;
				threshold += newWeight[currentNode.eList.get(selectedEdge).index];
			}
			// 加重
			newWeight[currentNode.eList.get(selectedEdge).index] += 1.0;
			// nextNodeIndexの決定
			if(currentNode.eList.get(selectedEdge).node[0]!=currentNodeIndex){
				nextNodeIndex = currentNode.eList.get(selectedEdge).node[0];
			}else{
				nextNodeIndex = currentNode.eList.get(selectedEdge).node[1];
			}
			currentNodeIndex = nextNodeIndex;
		}
		// プロット
		for(int i=0;i<net.M;i++){
			net.weight[i] = newWeight[i];
			pw.println(net.list[i][0] + "," + net.list[i][1] + ","+ net.weight[i]);
			System.out.println(net.list[i][0] + "," + net.list[i][1] + ","+ net.weight[i]);
		}
		pw.close();

		if(!net.success){
			System.out.println();
			System.out.println("生成に失敗しています。やり直してください。");
		}

	}
}
