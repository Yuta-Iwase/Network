import java.io.File;
import java.io.PrintWriter;

public class AirportTest18_2_1_MorePointTest {
	public static void main(String[] args) throws Exception{
		// 加点する重み
		double addWeight = 69.9;
		int N = 1574;
		final int walkN = 10000*N;

		String folderName = "USAirport/";
		String outputFileName = folderName + "USAirport_reinforcedRW_" + addWeight + ".csv";

		// 書き込み用オブジェクト定義
		PrintWriter pw = new PrintWriter(new File(outputFileName));

		// 基本データ読み込み・データ活用のための準備
		NetworkForCSVFile net = new NetworkForCSVFile(folderName + "USAirport_weighted.csv",false,true,false,false);
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
			r = (int)(sumW*Math.random());
			selectedEdge = 0;
			threshold = newWeight[currentNode.eList.get(0).index];
			while(r > threshold){
				selectedEdge++;
				threshold += newWeight[currentNode.eList.get(selectedEdge).index];
			}

			// 加重
			newWeight[currentNode.eList.get(selectedEdge).index] += addWeight;

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
