import java.io.File;
import java.io.PrintWriter;

public class RWTest_transition_01 {

	public static void main(String[] args) throws Exception{
		// ランダムネットワークの初期値
		int N = 1000;
//		double p = 0.0038738738738738738; //N=1000,gamma=2.7のconfの平均次数に従う(厳密ではない)
		double p = 0.007; //連結性があやしかったのでこちらを採用
		// ステップ数
		int step = 1000 * N;
		// プロット範囲
		double left = 1.0;
		double right = 30.0;
		// 刻み数
		int sectionN = 30; //(注):プロットは、(その数+1)個で出力されます。
		// 指標2でのステップ数
		int step2 = N;
		// ディレクトリ名
		String dir = "transition01/";

		// 作業変数
		RandomNetwork net = new RandomNetwork(N, p);
		net.setNode(false);
		net.setEdge();
		double sectionLength = (right-left)/sectionN; // 区間幅
		double currentDeltaW;
		double w_max; // 指標1
		int N_B; //指標2 step2中に遭遇する頂点の種類数
		int[] N_B_list = new int[net.M]; //遭遇済みか検証
		double divider_step2 = 1.0/step2;
		PrintWriter w_max_pw = new PrintWriter(new File(dir + "w_max.csv"));
		PrintWriter N_B_pw = new PrintWriter(new File(dir + "N_B.csv"));

		// 各deltaWごとに、指標を計算
		for(int i=0 ; i<=sectionN ; i++){
			currentDeltaW = left + sectionLength*i;
			net.ReinforcedRandomWalk(step, currentDeltaW);

			// 指標1:max_wを計算
			w_max = -1.0;
			for(int m=0 ; m<net.M ; m++){
				w_max = Math.max(w_max, net.weight[m]);
			}

			// 指標2を計算
			N_B = 0;
			for(int j=0;j<net.M;j++) N_B_list[j]=0;
			int currentNodeIndex = (int)(N * Math.random());
			int selectedEdge,nextNodeIndex;
			int sumW;
			double r,threshold;
			double[] newWeight = new double[net.M];
			for(int j=0;j<net.M;j++) newWeight[j]=1.0;
			Network.Node currentNode = net.nodeList.get(currentNodeIndex);
			for(int t=0;t<step2;t++){

				sumW = 0;
				for(int j=0;j<currentNode.eList.size();j++) sumW+=newWeight[currentNode.eList.get(j).index];
				r = (sumW*Math.random());
				selectedEdge = 0;
				threshold = newWeight[currentNode.eList.get(0).index];
				while(r > threshold){
					selectedEdge++;
					threshold += newWeight[currentNode.eList.get(selectedEdge).index];
				}

				N_B_list[currentNode.eList.get(selectedEdge).index]++;

				// nextNodeIndexの決定
				if(currentNode.eList.get(selectedEdge).node[0]!=currentNodeIndex){
					nextNodeIndex = currentNode.eList.get(selectedEdge).node[0];
				}else{
					nextNodeIndex = currentNode.eList.get(selectedEdge).node[1];
				}
				currentNodeIndex = nextNodeIndex;
			}
			for(int j=0;j<net.M;j++){
				if(N_B_list[j]!=0) N_B++;
			}

			w_max_pw.println(currentDeltaW + "\t" + (w_max/(currentDeltaW*step)));
			System.out.println(currentDeltaW + "\t" + (w_max/(currentDeltaW*step)));
			N_B_pw.println(currentDeltaW + "\t" + (N_B*divider_step2));
			System.out.println(currentDeltaW + "\t" + (N_B*divider_step2));
			System.out.println(N_B);
			System.out.println();
		}

		w_max_pw.close();
		N_B_pw.close();
	}

}
