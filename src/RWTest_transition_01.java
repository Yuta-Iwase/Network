import java.io.File;
import java.io.PrintWriter;

public class RWTest_transition_01 {

	public static void main(String[] args) throws Exception{
		// ランダムネットワークの初期値
		int N = 1000;
		double p = 0.0038738738738738738; //N=1000,gamma=2.7のconfの平均次数に従う(厳密ではない)
//		double p = 0.007; //連結性があやしかったのでこちらを採用
		// 平均じ数に
		p =0.01;
		
		// ステップ数
		int step = 10000 * N;
		// プロット範囲
		double left = 1.0;//start
		double right = 61.0;//finish
		// 刻み数
		int sectionN = 60; //(注):プロットは、(その数+1)個で出力されます。
		// 指標2でのステップ数
		int transientStep = 1000*N; //transientStep
		// ディレクトリ名
		String dir = "transition01/";

		// 作業変数
		RandomNetwork net = new RandomNetwork(N, p);
		net.setNode(false);
		net.setEdge();
		double sectionLength = (right-left)/sectionN; // 区間幅
		double currentDeltaW;
		int currentNodeIndex;
		double w_max; // 指標1 ネットワーク上の最大重み
		int N_B; //指標2 transient_s中に遭遇する頂点の種類数
		double w_max2; //指標3 transient_s中に遭遇する頂点の種類数 
		double[] newWList = new double[net.M];
		int[] N_B_list = new int[net.N]; //遭遇済みか検証
		double inverse_s = 1.0/transientStep; //inverse_s
		PrintWriter w_max_pw = new PrintWriter(new File(dir + "w_max.csv"));
		PrintWriter N_B_pw = new PrintWriter(new File(dir + "N_B.csv"));
		
		// 各deltaWごとに、指標を計算
		for(int i=0 ; i<=sectionN ; i++){
			for(int j=0;j<newWList.length;j++)newWList[j]=0;
			
			currentDeltaW = left + sectionLength*i;
			currentNodeIndex = net.ReinforcedRandomWalk(step, currentDeltaW);

			// 指標1:max_wを計算
			w_max = -1.0;
			for(int m=0 ; m<net.M ; m++){
				w_max = Math.max(w_max, net.weight[m]);
			}

			// 指標2を計算
			N_B = 0;
			for(int j=0;j<net.N;j++) N_B_list[j]=0;
			int selectedEdge,nextNodeIndex;
			int sumW; //sumW[]に
			double r,threshold;
			for(int t=0;t<transientStep;t++){
				Network.Node currentNode = net.nodeList.get(currentNodeIndex);

				sumW = 0;
				for(int j=0;j<currentNode.eList.size();j++) sumW+=net.weight[currentNode.eList.get(j).index];
				r = (sumW*Math.random());
				selectedEdge = 0;
				threshold = net.weight[currentNode.eList.get(0).index];
				while(r > threshold){
					selectedEdge++;
					threshold += net.weight[currentNode.eList.get(selectedEdge).index];
				}
				
				// 加重
				net.weight[currentNode.eList.get(selectedEdge).index] += currentDeltaW;
				newWList[currentNode.eList.get(selectedEdge).index] += currentDeltaW;

				// nextNodeIndexの決定
				if(currentNode.eList.get(selectedEdge).node[0]!=currentNodeIndex){
					nextNodeIndex = currentNode.eList.get(selectedEdge).node[0];
				}else{
					nextNodeIndex = currentNode.eList.get(selectedEdge).node[1];
				}
				currentNodeIndex = nextNodeIndex;
				
				N_B_list[currentNodeIndex]++;
			}
			for(int j=0;j<net.N;j++){
				if(N_B_list[j]!=0) N_B++;
			}
			
			// 指標3:max_w2を計算
			w_max2 = -1.0;
			for(int m=0 ; m<net.M ; m++){
				w_max2 = Math.max(w_max2, newWList[m]);
			}

			w_max_pw.println(currentDeltaW + "\t" + (w_max/(currentDeltaW*step+1)));
			System.out.println(currentDeltaW + "\t" + (w_max/(currentDeltaW*step+1)));
			N_B_pw.println(currentDeltaW + "\t" + (N_B*1.0/N));
			System.out.println(currentDeltaW + "\t" + (N_B*1.0/N));
			System.out.println(currentDeltaW + "\t" + (w_max2/(currentDeltaW*transientStep)));
			System.out.println();
		}
		
		System.out.println();
		double[][] s = new Comprator2dim().sort(net.weight, 1, false);
		for(int i=0;i<s.length;i++){
			System.out.println(s[i][0] + "\t" + s[i][1]);
		}

		w_max_pw.close();
		N_B_pw.close();
	}

}
