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
		int step2 = 1000*N; //transientStep
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
		double w_max2; //3
		double[] newWList = new double[net.M];
		double sumNewW;
		double sumNewW_List;
		int[] N_B_list = new int[net.N]; //遭遇済みか検証
		double divider_step2 = 1.0/step2; //inverse_s
		PrintWriter w_max_pw = new PrintWriter(new File(dir + "w_max.csv"));
		PrintWriter N_B_pw = new PrintWriter(new File(dir + "N_B.csv"));
		
		int c=0,c2=0;
		for(int i=0;i<net.N;i++){
			if(net.degree[i]==0)c++;
			if(net.nodeList.get(i).eList==null)c2++;
		}
//		System.out.println(c);
//		System.out.println(c2);
		System.out.println();

		// 各deltaWごとに、指標を計算
		for(int i=0 ; i<=sectionN ; i++){
			for(int j=0;j<newWList.length;j++)newWList[j]=0;
			sumNewW=0.0;
			sumNewW_List=0.0;
			
			currentDeltaW = left + sectionLength*i;
			currentDeltaW = 15.0;
			net.ReinforcedRandomWalk(step, currentDeltaW);

			// 指標1:max_wを計算
			w_max = -1.0;
			for(int m=0 ; m<net.M ; m++){
				w_max = Math.max(w_max, net.weight[m]);
			}

			// 指標2を計算
			N_B = 0;
			for(int j=0;j<net.N;j++) N_B_list[j]=0;
			int currentNodeIndex = Network.cNode;
			int selectedEdge,nextNodeIndex;
			int sumW; //sumW[]に
			double r,threshold;
			for(int t=0;t<step2;t++){
				Network.Node currentNode = net.nodeList.get(currentNodeIndex);

				sumW = 0;
				for(int j=0;j<currentNode.eList.size();j++) sumW+=net.weight[currentNode.eList.get(j).index];
				r = (sumW*Math.random());
				selectedEdge = 0;
//				System.out.println(currentNode.index);
//				System.out.println(currentNode.eList.size() + "/t" + net.degree[currentNode.index]);
				threshold = net.weight[currentNode.eList.get(0).index];
				while(r > threshold){
					selectedEdge++;
					threshold += net.weight[currentNode.eList.get(selectedEdge).index];
				}
				
				// 加重
//				net.weight[currentNode.eList.get(selectedEdge).index] += currentDeltaW;
				newWList[currentNode.eList.get(selectedEdge).index] += currentDeltaW;
//				sumNewW+=currentDeltaW;

				

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
				sumNewW_List+=newWList[m];
				w_max2 = Math.max(w_max2, newWList[m]);
			}

//			w_max_pw.println(currentDeltaW + "\t" + (w_max/(currentDeltaW*step+1)));
//			System.out.println(currentDeltaW + "\t" + (w_max/(currentDeltaW*step+1)));
//			N_B_pw.println(currentDeltaW + "\t" + (N_B*1.0/N));
			System.out.println(currentDeltaW + "\t" + (N_B*1.0/N));
//			System.out.println(currentDeltaW + "\t" + (w_max2/(currentDeltaW*step2)));
//			System.out.println(sumNewW_List + "\t" + sumNewW + "\t" + w_max2);
			System.out.println();
		}

		w_max_pw.close();
		N_B_pw.close();
	}

}
