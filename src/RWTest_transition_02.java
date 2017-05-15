import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class RWTest_transition_02 {

	public static void main(String[] args) throws Exception{
		// ランダムネットワークの初期値
		int N = 10000;
		double p = 0.0038738738738738738; //N=1000,gamma=2.7のconfの平均次数に従う(厳密ではない)
//		double p = 0.007; //連結性があやしかったのでこちらを採用
		// 平均じ数に
		p =0.015;
		
		int times = 100;
		
		DistributionGanerator dg = new DistributionGanerator();
		int[] degree = dg.ganeratePowerLaw(N, 2.7);
//		ConfigrationNetwork net = new ConfigrationNetwork(degree, 100);
		RandomNetwork net = new RandomNetwork(N, 0.000387);
		
		// ステップ数
		int step = 100 * N;
		// プロット範囲
		double left = 0.1;//start
		double right = 10.1;//finish
		// 刻み数
		int sectionN = 10; //(注):プロットは、(その数+1)個で出力されます。
		// 指標2でのステップ数
		int transientStep = 10*N; //transientStep
		// 上位の比率(指標4で使用)
		double topRate = 0.01;
		// ディレクトリ名
		String dir = "transition02/";

		if(net.success){
			net.setNode(false);
			net.setEdge();
			double sectionLength = (right-left)/sectionN; // 区間幅
			double currentDeltaW;
			int currentNodeIndex;
			int serectedEdgeIndex;
			double w_max; // 指標1 ネットワーク上の最大重み
			int N_B; //指標2 transient_s中に遭遇する頂点の種類数
			double w_max2; //指標3 transient_s中に生成する最大重み
			int topN = (int)(net.N*topRate); //指標4の上位の個数(指標4:transient_s中に生成する上位重み)
			ArrayList<Double> topWeightList = new ArrayList<Double>(); //(暫定)上位を格納するリスト
			ArrayList<Integer> topIndexList = new ArrayList<Integer>(); //topWeightListに入っている辺のindexを格納する。被りを回避する役割
			boolean registered; //topリスト内に既に居るか確認
			int registeredListAddress; //topリストでのかつての位置
			int rank; //新入りの順位
			double[] newWList = new double[net.M];
			int[] N_B_list = new int[net.N]; //遭遇済みか検証
//			double inverse_s = 1.0/transientStep; //inverse_s
			PrintWriter w_max_pw = new PrintWriter(new File(dir + "w_max.csv"));
			PrintWriter N_B_pw = new PrintWriter(new File(dir + "N_B.csv"));
			
			double ave1,ave2,ave3;
			
			// 各deltaWごとに、指標を計算
			for(int i=0 ; i<=sectionN ; i++){
				// current系統の変数更新
				currentDeltaW = left + sectionLength*i;
				
				ave1=0;ave2=0;ave3=0;
				
				for(int t=0;t<times;t++){
					currentNodeIndex = net.ReinforcedRandomWalk(step, currentDeltaW,0);
					
					for(int j=0;j<newWList.length;j++)newWList[j]=0;
					
					// 指標4用のリスト初期化
					topWeightList.clear();
					topIndexList.clear();
					for(int ii=0;ii<topN;ii++){
						topWeightList.add(0.0);
						topIndexList.add(-1);
					}
					
					// 指標1:max_wを計算
					w_max = -1.0;
					for(int m=0 ; m<net.M ; m++){
						w_max = Math.max(w_max, net.weight[m]);
					}
					if(t==0) ave1 = w_max;
					else{
						ave1 += (w_max*1.0)/t;
						ave1 *= t/(t+1.0);
					}
					
					

					// 指標2,4を計算
					N_B = 0;
					for(int j=0;j<net.N;j++) N_B_list[j]=0;
					int selectedEdge,nextNodeIndex;
					int sumW; //☆sumW[]に
					double r,threshold;
					for(int tr=0;tr<transientStep;tr++){
						Network.Node currentNode = net.nodeList.get(currentNodeIndex);

						// ☆sumW系の処理は後で修正
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
						serectedEdgeIndex = currentNode.eList.get(selectedEdge).index;
						net.weight[serectedEdgeIndex] += currentDeltaW;
						newWList[serectedEdgeIndex] += currentDeltaW;
						
						// 指標4処理:上位リスト更新
						// registerdが偽 で topリストに入っていないなら登録
						if(topWeightList.get(topN-1) < newWList[serectedEdgeIndex]){
							// 登録済みの辺なのか確認
							registered = false;
							registeredListAddress = -1;
							for(int ii=0;ii<topIndexList.size();ii++){
								if(topIndexList.get(ii)==serectedEdgeIndex){
									registered = true;
									registeredListAddress = ii;
									break;
								}
							}
							// 登録済みなら、重み更新
							// 未登録なら、新入りを加入させる
							if(registered){
								topWeightList.remove(registeredListAddress);
								topIndexList.remove(registeredListAddress);
							}else{
								// 最弱をリストラ
								topWeightList.remove(topN-1);
								topIndexList.remove(topN-1);
							}
							// 1位より大きければ即登録
							if(topWeightList.get(0) < newWList[serectedEdgeIndex]){
								topWeightList.add(0, newWList[serectedEdgeIndex]);
								topIndexList.add(0,serectedEdgeIndex);
							}else{
								// 2位以下と比較
								rank = topN-1;
								while(topWeightList.get(rank-1) < newWList[serectedEdgeIndex]){
									rank--;
								}
								topWeightList.add(rank,newWList[serectedEdgeIndex]);
								topIndexList.add(rank,serectedEdgeIndex);
							}
						}
						
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
					if(t==0) ave2 = N_B;
					else{
						ave2 += (N_B*1.0)/t;
						ave2 *= t/(t+1.0);
					}
					
					// 指標3:max_w2を計算
					w_max2 = -1.0;
					for(int m=0 ; m<net.M ; m++){
						w_max2 = Math.max(w_max2, newWList[m]);
					}
					if(t==0) ave3 = w_max2;
					else{
						ave3 += (w_max2*1.0)/t;
						ave3 *= t/(t+1.0);
					}

//					w_max_pw.println(currentDeltaW + "\t" + (w_max/(currentDeltaW*step+1)));
//					System.out.println(currentDeltaW + "\t" + (w_max/(currentDeltaW*step+1)) + "\tperTimes");
//					N_B_pw.println(currentDeltaW + "\t" + (N_B*1.0/N));
//					System.out.println(currentDeltaW + "\t" + (N_B*1.0/N));
//					System.out.println(currentDeltaW + "\t" + (w_max2/(currentDeltaW*transientStep)));
//					for(int ii=0;ii<topN;ii++){
//						System.out.println(ii + "\t" + topWeightList.get(ii));
//					}
					
//					System.out.println();
				}
				w_max_pw.println(currentDeltaW + "\t" + (ave1/(currentDeltaW*step+1)));
				System.out.println(currentDeltaW + "\t" + (ave1/(currentDeltaW*step+1)));
				N_B_pw.println(currentDeltaW + "\t" + (ave2*1.0/N));
				System.out.println(currentDeltaW + "\t" + (ave2*1.0/N));
				System.out.println(currentDeltaW + "\t" + (ave3/(currentDeltaW*transientStep)));
//				for(int ii=0;ii<topN;ii++){
//					System.out.println(ii + "\t" + topWeightList.get(ii));
//				}
				System.out.println();
				
				net.LinkSalience();
				for(int ii=0;ii<net.M;ii++){
					System.out.println(ii+"\t"+net.edgeList.get(ii).linkSalience);
				}
			}
				
				
			
//			System.out.println();
//			double[][] s = new Comprator2dim().sort(net.weight, 1, false);
//			for(int i=0;i<s.length;i++){
//				System.out.println(s[i][0] + "\t" + s[i][1]);
//			}

			w_max_pw.close();
			N_B_pw.close();
		}

	}

}
