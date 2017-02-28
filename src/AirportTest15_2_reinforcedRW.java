import java.io.File;
import java.io.PrintWriter;

// ネットワークの(重みの)ランダマイズ
// ②reinforcedRW

// 17/02/28 バグ修正

public class AirportTest15_2_reinforcedRW {
	public static void main(String[] args) throws Exception{
		final int walkN = 100000;
		String fileName = "reinforcedRW.csv";

		// 書き込み用オブジェクト定義
		PrintWriter pw = new PrintWriter(new File(fileName));

		// 基本データ読み込み・データ活用のための準備
		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv",false,true,true,true);
		net.setNode();
		new AirportNetworkTransformer().makeUndirectedEdge(net);
		net.setEdge();
		net.setLabel("LabelList.csv");

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

	}
}
