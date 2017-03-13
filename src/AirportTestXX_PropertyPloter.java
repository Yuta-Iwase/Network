import java.io.File;
import java.io.PrintWriter;


public class AirportTestXX_PropertyPloter {
	static String plotDelimiter = ",";

	public static void main(String[] args) throws Exception{
//		S10b14_BetAport_salience();

//		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv", false, true, true, true);
//		net.setNode();
//		new AirportNetworkTransformer().makeUndirectedEdge(net);

//		net.printList();

//		RW_Ploter();
//		for(int i=0;i<6;i++){
//			property(6, i);
//			System.out.println("タスク" + i + "終了");
//		}
//		property(6, 0);
//		property(6, 1);
		property(6, 2);
		property(6, 3);
		property(6, 4);
		property(6, 5);
		
//		rRW("conf2_7/conf2.7_nw.csv","conf2_7/conf2.7_rRW20.0.csv",20.0,false);
//		property(7, 4);

	}

	public static void RW_Ploter() throws Exception{
		boolean weighted = false;
		String baseFile = "random_conf2_7min4/random_conf2.7min4_nw.csv";
		sRW(baseFile,"random_conf2_7min4/random_conf2.7min4_sRW.csv",weighted);
		rRW(baseFile,"random_conf2_7min4/random_conf2.7min4_rRW1.0.csv",1.0,weighted);
		rRW(baseFile,"random_conf2_7min4/random_conf2.7min4_rRW2.0.csv",2.0,weighted);
		rRW(baseFile,"random_conf2_7min4/random_conf2.7min4_rRW10.0.csv",10.0,weighted);
		pRW(baseFile,"random_conf2_7min4/random_conf2.7min4_pRW.csv",weighted);
	}

	public static void property(int input_fileN,int input_mode) throws Exception{
		int fileN = input_fileN; ///
		int mode = input_mode; ///
		String directory = "conf2_7min4/"; ///
		String coreName = "conf2.7min4_"; ///

		String headName = directory + coreName;
		String[] target = new String[7];
		String[] targetElement = new String[7];
		for(int i=0;i<fileN;i++){
			target[i] = headName;
		}
		targetElement[0] = "nw";
		targetElement[1] = "sRW";
		targetElement[2] = "rRW1.0";
		targetElement[3] = "rRW2.0";
		targetElement[4] = "rRW10.0";
		targetElement[5] = "pRW";
		if(fileN >= 7){
			targetElement[6] = "w";
		}

		for(int i=0;i<fileN;i++){
			target[i] += (targetElement[mode] + ".csv");
		}

		String pwFile = directory + targetElement[mode] + "/property.csv";
		PrintWriter pw = new PrintWriter(new File(pwFile));
		NetworkForCSVFile net;
		if(mode == 0){
			net = new NetworkForCSVFile(target[mode], false, false, false, false);
			net.weighted = true;
			net.weight = new double[net.M];
			for(int i=0 ; i<net.M;i++){
				net.weight[i] = 1.0;
			}
		}else{
			net = new NetworkForCSVFile(target[mode], false, true, false, false);
		}
		net.setNode(false);
		net.setEdge();
		net.EdgeBetweenness();
		net.LinkSalience();

		for(int i=0;i<net.M;i++){
			System.out.println(i + plotDelimiter + net.weight[i] + plotDelimiter + net.edgeList.get(i).betweenCentrality + plotDelimiter + net.edgeList.get(i).linkSalience);
			pw.println(i + plotDelimiter + net.weight[i] + plotDelimiter + net.edgeList.get(i).betweenCentrality + plotDelimiter + net.edgeList.get(i).linkSalience);
		}

		pw.close();
	}

	public static void sRW(String name,String out,boolean weighted) throws Exception{
		NetworkForCSVFile net = new NetworkForCSVFile(name,false,weighted,false,false);
		net.setNode(false);
		net.setEdge();
		PrintWriter pw = new PrintWriter(new File(out));

		int walkN = net.N*1000;

		// 作業変数定義
		int currentNodeIndex = (int)(net.N * Math.random());
		int selectedEdge,nextNodeIndex;
		double[] newWeight = new double[net.M];
		for(int i=0;i<net.M;i++) newWeight[i]=1.0;
		for(int t=0;t<walkN;t++){
			Network.Node currentNode = net.nodeList.get(currentNodeIndex);

			// ここが各ランダムウォークで変化する内容(辺の選択方法)
			selectedEdge = (int)(currentNode.eList.size()*Math.random());

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
			pw.println(net.list[i][0] + plotDelimiter + net.list[i][1] + plotDelimiter+ net.weight[i]);
//			System.out.println(net.list[i][0] + plotDelimiter + net.list[i][1] + plotDelimiter + net.weight[i]);
		}

		pw.close();
	}

	public static void rRW(String name,String out,double point,boolean weighted) throws Exception{
		NetworkForCSVFile net = new NetworkForCSVFile(name,false,weighted,false,false);
		net.setNode(false);
		net.setEdge();
		PrintWriter pw = new PrintWriter(new File(out));

		int walkN = net.N*1000;

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
			newWeight[currentNode.eList.get(selectedEdge).index] += point;

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
			pw.println(net.list[i][0] + plotDelimiter + net.list[i][1] + plotDelimiter+ net.weight[i]);
//			System.out.println(net.list[i][0] + plotDelimiter + net.list[i][1] + plotDelimiter + net.weight[i]);
		}

		pw.close();
	}

	public static void pRW(String name,String out,boolean weighted) throws Exception{
		NetworkForCSVFile net = new NetworkForCSVFile(name,false,weighted,false,false);
		net.setNode(false);
		net.setEdge();
		PrintWriter pw = new PrintWriter(new File(out));

		int walkN = net.N*1000;

		// 作業変数定義
		int currentNodeIndex = (int)(net.N * Math.random());
		int selectedEdge,nextNodeIndex;
		int sumDegree;
		double r,threshold;
		double[] newWeight = new double[net.M];
		for(int i=0;i<net.M;i++) newWeight[i]=1.0;
		for(int t=0;t<walkN;t++){
			Network.Node currentNode = net.nodeList.get(currentNodeIndex);

			// ここが各ランダムウォークで変化する内容(辺の選択方法)
			sumDegree = 0;
			for(int i=0;i<currentNode.list.size();i++) sumDegree+=net.degree[currentNode.list.get(i).index];
			r = (int)(sumDegree*Math.random());
			selectedEdge = 0;
			threshold = net.degree[currentNode.list.get(0).index];
			while(r > threshold){
				selectedEdge++;
				threshold += net.degree[currentNode.list.get(selectedEdge).index];
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
			pw.println(net.list[i][0] + plotDelimiter + net.list[i][1] + plotDelimiter+ net.weight[i]);
//			System.out.println(net.list[i][0] + plotDelimiter + net.list[i][1] + plotDelimiter + net.weight[i]);
		}

		pw.close();
	}


}
