import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class AirportTest35_1_some_samples_exam{

	public static void main(String[] args) throws Exception{
		//// 変動するパラメータ
		// メタ・パラメータ
		int times = 2;
		int bins = 50;
		// ネットワークパラメータ
		int N = 1000;
		double gamma = 3.2;

		// アルファに制御される回数は固定する
		final double ALPHA_CONTROLED_TIMES;
		// debag mode: alpha=1.0
		BigDecimal ALPHA_MIN = new BigDecimal("-3.0");
		BigDecimal ALPHA_MAX = new BigDecimal("3.0");
		BigDecimal ALPHA_WIDTH = new BigDecimal("0.1");
		ALPHA_CONTROLED_TIMES = ALPHA_MAX.subtract(ALPHA_MIN).divide(ALPHA_WIDTH).add(BigDecimal.ONE).doubleValue();

		//// 定数定義
		// ステップ数
		int num_step = 100*N;
		// オブジェクト定義
		HistogramGenerator hist = new HistogramGenerator();

		//// 処理

		// ④
		PrintWriter pw41 = new PrintWriter(new File("high_salience_frag.csv"));
		double current_HS_frag = 0.0;

		// 開始時のタイムスタンプ
		long start_time;

		// [ループ、level1]alpha回数処理
		BigDecimal dec_current_alpha = ALPHA_MIN;
		for(int alpha_factor=0;alpha_factor<ALPHA_CONTROLED_TIMES;alpha_factor++) {
			double alpha = dec_current_alpha.doubleValue();
			String alphaString = dec_current_alpha.toString();
			String temp_fileName = "alpha=" + alphaString;
			new File(temp_fileName).mkdirs();
			dec_current_alpha = dec_current_alpha.add(ALPHA_WIDTH);
			start_time = System.currentTimeMillis();
			System.out.println("start: alpha=" + alphaString);
			System.out.println(new Date().toString());


			// ①weight
			PrintWriter pw11 = new PrintWriter(new File(temp_fileName + "/w_LinerScale.csv"));
			PrintWriter pw12 = new PrintWriter(new File(temp_fileName + "/w_LogScale.csv"));
			PrintWriter pw13 = new PrintWriter(new File(temp_fileName + "/w_RawList.csv"));
			double[][] w_liner = new double[bins][2];
			double[][] w_log = new double[bins][2];
			for(int i=0;i<bins;i++) {
				w_liner[i][1] = 0.0;
				w_log[i][1] = 0.0;
			}
			double[][] current_w_liner = new double[bins][2];
			double[][] current_w_log = new double[bins][2];
			// ②edge_BC
			PrintWriter pw21 = new PrintWriter(new File(temp_fileName + "/edgeBC_LinerScale.csv"));
			PrintWriter pw22 = new PrintWriter(new File(temp_fileName + "/edgeBC_LogScale.csv"));
			PrintWriter pw23 = new PrintWriter(new File(temp_fileName + "/edgeBC_RawList.csv"));
			double[][] edgeBC_liner = new double[bins][2];
			double[][] edgeBC_log = new double[bins][2];
			for(int i=0;i<bins;i++) {
				edgeBC_liner[i][1] = 0.0;
				edgeBC_log[i][1] = 0.0;
			}
			double[][] current_edgeBC_liner = new double[bins][2];
			double[][] current_edgeBC_log = new double[bins][2];
			// ③link salience
			PrintWriter pw31 = new PrintWriter(new File(temp_fileName + "/LinkSalience_LinerScale.csv"));
			PrintWriter pw32 = new PrintWriter(new File(temp_fileName + "/LinkSalience_RawList.csv"));
			double[][] s_liner = new double[bins][2];
			for(int i=0;i<bins;i++) s_liner[i][1]=0.0;
			double[][] current_s_liner = new double[bins][2];
			// ⑤degree distribution(high salience)
			PrintWriter pw51 = new PrintWriter(new File(temp_fileName + "/degree(HS)_dist_LinerScale.csv"));
			PrintWriter pw52 = new PrintWriter(new File(temp_fileName + "/degree(HS)_dist_LogScale.csv"));
			PrintWriter pw53 = new PrintWriter(new File(temp_fileName + "/degree(HS)_dist_RawList_doubleCount.csv"));
			double[][] d_hs_liner = new double[bins][2];
			double[][] d_hs_log = new double[bins][2];
			ArrayList<Double> d_hs_RawList = new ArrayList<>();
			// ⑥strength
			PrintWriter pw61 = new PrintWriter(new File(temp_fileName + "/strength_LinerScale.csv"));
			PrintWriter pw62 = new PrintWriter(new File(temp_fileName + "/strength_LogScale.csv"));
			PrintWriter pw63 = new PrintWriter(new File(temp_fileName + "/strength_RawList.csv"));
			double[][] str_liner = new double[bins][2];
			double[][] str_log = new double[bins][2];
			for(int i=0;i<bins;i++) {
				str_liner[i][1] = 0.0;
				str_log[i][1] = 0.0;
			}
			double[][] current_str_liner = new double[bins][2];
			double[][] current_str_log = new double[bins][2];
			// ⑦node_BC
			PrintWriter pw71 = new PrintWriter(new File(temp_fileName + "/nodeBC_LinerScale.csv"));
			PrintWriter pw72 = new PrintWriter(new File(temp_fileName + "/nodeBC_LogScale.csv"));
			PrintWriter pw73 = new PrintWriter(new File(temp_fileName + "/nodeBC_RawList.csv"));
			double[][] nodeBC_liner = new double[bins][2];
			double[][] nodeBC_log = new double[bins][2];
			for(int i=0;i<bins;i++) {
				nodeBC_liner[i][1] = 0.0;
				nodeBC_log[i][1] = 0.0;
			}
			double[][] current_nodeBC_liner = new double[bins][2];
			double[][] current_nodeBC_log = new double[bins][2];
			// ⑧visited_nodes
			PrintWriter pw81 = new PrintWriter(new File(temp_fileName + "/visited_nodes.csv"));
			PrintWriter pw82 = new PrintWriter(new File(temp_fileName + "/visited_nodes_once.csv"));
			int[] visited_nodes = new int[num_step];
			int[] current_visited_nodes = new int[num_step];
			for(int i=0;i<num_step;i++) visited_nodes[i]=0;


			// [ループ、level2]times処理
			for(int t=0;t<times;t++) {
				// ネットワーク構築
				Network net;
				do{
					MakePowerLaw dist = new MakePowerLaw(N, gamma, 2, N-1);
					net = new ConfigrationNetwork(dist.degree, 100,false);
				}while(!net.success);
				net.setNode(false);
				net.setEdge();
				final double INV_M = 1.0/net.M;
				final double INV_N = 1.0/net.N;
				final double INV_SQUARE_N = 1.0/(net.N*net.N);

				// random walkの実行
				// ⑧用の準備も
				current_visited_nodes = net.BiasedRandomWalk_checkVisitedNodes(num_step, 1.0, alpha, (int)(System.currentTimeMillis()&Integer.MAX_VALUE), 0.0, true);

				// ①
				double[] w_round_list = new double[net.weight.length];
				for(int i=0;i<net.weight.length;i++) w_round_list[i]=Math.round(net.weight[i]);
				current_w_liner = hist.binPlot(w_round_list, bins, false, 1, num_step);
				current_w_log = hist.binPlot(w_round_list, bins, true, 1, num_step);
				for(int i=0;i<bins;i++) {
					w_liner[i][1] += current_w_liner[i][1]*INV_M;
					w_log[i][1] += current_w_log[i][1]*INV_M;
				}
				for(int i=0;i<w_round_list.length;i++) {
					pw13.println(w_round_list[i]);
				}
				// ②
				net.EdgeBetweenness();
				double[] edge_BC_list = new double[net.edgeList.size()];
				for(int i=0;i<edge_BC_list.length;i++) edge_BC_list[i]=net.edgeList.get(i).betweenCentrality*INV_SQUARE_N;
				current_edgeBC_liner = hist.binPlot(edge_BC_list, bins, false,0,1);
				current_edgeBC_log = hist.binPlot(edge_BC_list, bins, true,0,1);
				for(int i=0;i<current_edgeBC_liner.length;i++) {
					edgeBC_liner[i][1] += current_edgeBC_liner[i][1]*INV_M;
					edgeBC_log[i][1] += current_edgeBC_log[i][1]*INV_M;
				}
				for(int i=0;i<edge_BC_list.length;i++) {
					pw23.println(edge_BC_list[i]);
				}
				// ③
				net.LinkSalience();
				double[] s_list = new double[net.edgeList.size()];
				for(int i=0;i<s_list.length;i++) s_list[i]=net.edgeList.get(i).linkSalience*INV_N;
				current_s_liner = hist.binPlot(s_list, bins, false, 0, 1);
				for(int i=0;i<s_liner.length;i++) {
					s_liner[i][1] += current_s_liner[i][1]*INV_M;
				}
				for(int i=0;i<s_list.length;i++) {
					pw32.println(s_list[i]);
				}
				// ④,⑤
				current_HS_frag = 0.0;
				int[] HS_degree = new int[N];
				for(int i=0;i<HS_degree.length;i++) HS_degree[i]=0;
				for(int i=0;i<s_list.length;i++) {
					if(s_list[i]>=0.9) {
						// ④
						current_HS_frag++;
						// ⑤
						HS_degree[net.edgeList.get(i).node[0]]++;
						HS_degree[net.edgeList.get(i).node[1]]++;
					}
				}
				// ⑤
				for(int i=0;i<HS_degree.length;i++){
					if(HS_degree[i]>0){
						d_hs_RawList.add((double)HS_degree[i]);
						pw53.println(HS_degree[i]);
					}
				}
				// ④
				current_HS_frag *= INV_N;
				// ⑥
				double[] str_list = new double[net.nodeList.size()];
				for(int i=0;i<net.nodeList.size();i++) {
					str_list[i] = 0.0;
					for(int e=0;e<net.nodeList.get(i).eList.size();e++) {
						str_list[i] += w_round_list[net.nodeList.get(i).eList.get(e).index];
					}
				}
				current_str_liner = hist.binPlot(str_list, bins, false, 1, num_step);
				current_str_log = hist.binPlot(str_list, bins, true, 1, num_step);
				for(int i=0;i<bins;i++) {
					str_liner[i][1] += current_str_liner[i][1]*INV_N;
					str_log[i][1] += current_str_log[i][1]*INV_N;
				}
				for(int i=0;i<str_list.length;i++) {
					pw63.println(str_list[i]);
				}
				// ⑦
				net.nodeBetweenness_for_WeightedNet();
				double[] node_BC_list = new double[net.nodeList.size()];
				for(int i=0;i<node_BC_list.length;i++) node_BC_list[i]=net.nodeList.get(i).betweenCentrality*INV_SQUARE_N;
				current_nodeBC_liner = hist.binPlot(node_BC_list, bins, false,0,1);
				current_nodeBC_log = hist.binPlot(node_BC_list, bins, true,0,1);
				for(int i=0;i<current_nodeBC_liner.length;i++) {
					nodeBC_liner[i][1] += current_nodeBC_liner[i][1]*INV_N;
					nodeBC_log[i][1] += current_nodeBC_log[i][1]*INV_N;
				}
				for(int i=0;i<node_BC_list.length;i++) {
					pw73.println(node_BC_list[i]);
				}
				// ⑧
				for(int i=0;i<num_step;i++){
					visited_nodes[i] += current_visited_nodes[i];
				}


			}

			// ⑤
			d_hs_liner = hist.binPlot(d_hs_RawList,bins,false);
			d_hs_log = hist.binPlot(d_hs_RawList, bins, true);
			double d_divider = 1.0/(2*N*times);

			// bin関係print
			for(int i=0;i<bins;i++) {
				w_liner[i][0] = current_w_liner[i][0];
				w_liner[i][1] /= times;
				w_log[i][0] = current_w_log[i][0];
				w_log[i][1] /= times;
				edgeBC_liner[i][0] = current_edgeBC_liner[i][0];
				edgeBC_liner[i][1] /= times;
				edgeBC_log[i][0] = current_edgeBC_log[i][0];
				edgeBC_log[i][1] /= times;
				s_liner[i][0] = current_s_liner[i][0];
				s_liner[i][1] /= times;
				d_hs_liner[i][1] *= d_divider;
				d_hs_log[i][1] *= d_divider;
				str_liner[i][0] = current_str_liner[i][0];
				str_liner[i][1] /= times;
				str_log[i][0] = current_str_log[i][0];
				str_log[i][1] /= times;
				nodeBC_liner[i][0] = current_edgeBC_liner[i][0];
				nodeBC_liner[i][1] /= times;
				nodeBC_log[i][0] = current_edgeBC_log[i][0];
				nodeBC_log[i][1] /= times;

				if(w_liner[i][1]>0) pw11.println(w_liner[i][0]+","+w_liner[i][1]);
				if(w_log[i][1]>0) pw12.println(w_log[i][0]+","+w_log[i][1]);
				if(edgeBC_liner[i][1]>0) pw21.println(edgeBC_liner[i][0] + "," + edgeBC_liner[i][1]);
				if(edgeBC_log[i][1]>0) pw22.println(edgeBC_log[i][0] + "," + edgeBC_log[i][1]);
				if(s_liner[i][1]>0) pw31.println(s_liner[i][0] + "," + s_liner[i][1]);
				if(d_hs_liner[i][1]>0) pw51.println(d_hs_liner[i][0] + "," + d_hs_liner[i][1]);
				if(d_hs_log[i][1]>0) pw52.println(d_hs_log[i][0] + "," + d_hs_log[i][1]);
				if(str_liner[i][1]>0) pw61.println(str_liner[i][0] + ","  + str_liner[i][1]);
				if(str_log[i][1]>0) pw62.println(str_log[i][0] + ","  + str_log[i][1]);
				if(nodeBC_liner[i][1]>0) pw71.println(nodeBC_liner[i][0] + "," + nodeBC_liner[i][1]);
				if(nodeBC_log[i][1]>0) pw72.println(nodeBC_log[i][0] + "," + nodeBC_log[i][1]);
			}
			pw41.println(alphaString + "," + current_HS_frag);
			for(int i=0;i<num_step;i++){
				pw81.println(i + "," + ((double)visited_nodes[i]/N)/times);
				pw82.println(i + "," + ((double)current_visited_nodes[i]/N));
			}


			pw11.close();
			pw12.close();
			pw13.close();
			pw21.close();
			pw22.close();
			pw23.close();
			pw31.close();
			pw32.close();
			pw51.close();
			pw52.close();
			pw53.close();
			pw61.close();
			pw62.close();
			pw63.close();
			pw71.close();
			pw72.close();
			pw73.close();
			pw81.close();
			pw82.close();

			int rap = (int)((System.currentTimeMillis()-start_time)/1000);
			System.out.println("finish: rap=" + rap + "[s]");
			System.out.println();

		}
		pw41.close();

	}
}
