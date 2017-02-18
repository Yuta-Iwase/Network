import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

// 課題:
//// ・重み付きネットワークに対応していない(7/28にて改善)
//// ・頂点のラベルに未対応
//// ・フィールド変数はprivateとしてゲッターを用いて呼び出すべきか?
//// ・フィールド変数weight[] と Edgeクラスのweight が役割として重複している。
//// ・(16/12/12-14)にかけてのエラーの大本はココ、このプロジェクトの火薬庫
// 更新
// 16/7/28
// ・Edgeクラス追加
// ・フィールド変数weight[],weighted追加
// ・searchEdgeメソッドを追加
// 16/8/8
// ・csvを読み込み対応した頂点に対応したラベルを頂点に与える
//   メソッド setLabel(String inputFilePath)の追加
// 16/12/14
// ・ダブルカウント(例:{0,1},{1,0}と書く)への対策を行う
// 　反動で正しく動作しないメソッド、クラスが現れるかも？
// 17/02/04
// ・setEdgeメソッドを定義
//  (注)list[][]が定義されてから使ってください
//  内容:ネットワークのEdgeのリストを作る
//       頂点について、所有する辺のリストeListを使うことができる


public class Network implements Cloneable{
	// フィールド変数 N,M,list,success の4つを持つ
	int N,M;
	int[][] list;
	int[] degree;
	double[] weight;
	boolean directed;
	boolean doubleCount;
	boolean weighted;
	boolean success = true; //基本true サブクラス次第でfalseにもなる

	// setNode()メソッドを実行することでノードリストを使うことができる
	ArrayList<Node> nodeList = new ArrayList<Node>();
	// setNode()->setEdge()で使用可能
	ArrayList<Edge> edgeList = new ArrayList<Edge>();
	// setLabel(String inputFilePath)メソッドを実行することでラベル設定を読み込むことができる
	String[] label;

	// パーコレーション等により頂点数が変化するとき用の頂点リスト
	private ArrayList<Integer> existNodeList = new ArrayList<Integer>();


	/** 隣接リストをコンソールへプリント */
	public void printList(){
		if(success)
			for(int i=0;i<list.length;i++){
				System.out.println(list[i][0] + "," + list[i][1]);
			}
		else
			System.out.println("生成に失敗しているため表示できません。");
	}

	/** 隣接リストをcsv形式で保存 */
	public void printList(String fileName){
		PrintWriter pw;
		if(success){
			try{
				pw = new PrintWriter(new File(fileName));
				for(int i=0;i<list.length;i++){
					pw.println(list[i][0] + "," + list[i][1]);
				}
				pw.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}else{
			System.out.println("生成に失敗しているため表示できません。");
		}
	}

	/** 隣接リストをcsv形式で保存
	次数0などの特別な頂点に対応(nodeListを定義しないと使えない) */
	public void printListExtention(String fileName){
		fileName = "[Extention]" + fileName;
		PrintWriter pw;
		if(success && existNodeList.size()>0){
			try{
				pw = new PrintWriter(new File(fileName));
				for(int i=0 ; i<existNodeList.size() ; i++){
					pw.println(existNodeList.get(i));
				}
				for(int i=0;i<list.length;i++){
					pw.println(list[i][0] + "," + list[i][1]);
				}
				pw.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}else{
			System.out.println("条件を満たさないため表示できません。");
		}
	}

	// sort()メソッドのためのメソッド
	private int[][] quickSort(int[][] list,int low,int high, int level){
		int space1,space2;
		if(low<high){
			int mid = (low + high)/2;
			int x = list[mid][level];
			int i=low;
			int j=high;
			while(i<=j){
				while(list[i][level] < x) i++;
				while(list[j][level] > x) j--;
				if(i<=j){
					space1=list[i][0]; space2=list[i][1];
					list[i][0]=list[j][0]; list[i][1]=list[j][1];
					list[j][0]=space1; list[j][1]=space2;
					i++; j--;
				}
			}
			quickSort(list,low,j,level);
			quickSort(list,i,high,level);
		}
		return list;
	}

	/** 隣接リストを辞書式順序(昇順)へ整列 */
	public void sort(){
		if(success){
			if(doubleCount) quickSort(list,0,M*2-1,0);
			else quickSort(list,0,M-1,0);
			int low=0;
			int high=0;
			sortLevel2 : for(int n=0 ; n<N ; n++){
				while(list[high][0] == n){
					high++;
					if(high == M)break sortLevel2;
				}
				if( (high-low) > 1){
					quickSort(list,low,high-1,1);
				}
				low = high;
			}
		}else{
			System.out.println("生成に失敗しているためソートできません。");
		}
	}

	/** このメソッドを実行することで<br>
	 * 頂点のリストnodeListを使うことができる。<br>
	 * (注):doubleCountが偽のときはsetNode(boolean input_doubleCount)を使うこと<br>*/
	public void setNode(){
		// 隣接リストを辞書式順序(昇順)へ整列
		sort();

		// Nodeを初期化しnodeListへ追加する
		for(int n=0;n<N;n++){
			nodeList.add( new Node(n) );
		}

		//各Nodeの隣接リストを定義
		int currentEdge = 0;
		for(int n=0;n<N;n++){
			for(int i=0;i<degree[n];i++){
				System.out.println(M + "\t" + n);
				nodeList.get(n).list.add(nodeList.get(list[currentEdge+i][1]));
			}
			currentEdge += degree[n];
		}
	}
	/** setNode()ではdoubleCountが偽のときの
	 * 動作がおかしかった。それを修正するためのオーバーロード*/
	public void setNode(boolean input_doubleCount){
		// 隣接リストを辞書式順序(昇順)へ整列
		sort();

		// Nodeを初期化しnodeListへ追加する
		for(int n=0;n<N;n++){
			nodeList.add( new Node(n) );
		}

		//各Nodeの隣接リストを定義
		if(input_doubleCount){
			int currentEdge = 0;
			for(int n=0;n<N;n++){
				for(int i=0;i<degree[n];i++){
					nodeList.get(n).list.add(nodeList.get(list[currentEdge+i][1]));
				}
				currentEdge += degree[n];
			}
		}else{
			for(int m=0;m<M;m++){
				nodeList.get(list[m][0]).list.add(nodeList.get(list[m][1]));
				nodeList.get(list[m][1]).list.add(nodeList.get(list[m][0]));
			}
		}
	}

	/** このメソッドを実行することで
	 * 辺のリストedgeListを使うことができる。
	 * 頂点について、所有する辺のリストeListを使うことができる。
	 * (注)list[][]が定義されている場合のみ使用可能*/
	public void setEdge(){
		Edge currentEdge = null;
		for(int i=0;i<M;i++){
			// 現在のループで扱う辺
			currentEdge = new Edge(list[i][0],list[i][1],i);
			// edgeListへ登録
			edgeList.add(currentEdge);
			// eListへ登録
			nodeList.get(list[i][0]).eList.add(currentEdge);
			nodeList.get(list[i][1]).eList.add(currentEdge);
		}
	}

	/** csvファイルを読み込みラベルを割り当てるメソッド
	 * 書式は「(頂点番号),(ラベル名)」を1行ずつ羅列させていく
	 * ※ 頂点間の区切りは原則カンマとするが、スペースやタブでも対応できるようにしておく。
	 * ！ 読み込みファイルは書き方を誤ると想定しないエラーが起こりやすいので注意 ！
	 */
	public void setLabel(String inputFilePath){
		Scanner scan = null;
		label = new String[N];
		String punctuation = "";
		String currentLine;
		int pancPos;
		try{
			scan = new Scanner(new File(inputFilePath));

			// 区切り文字の識別
			currentLine = scan.nextLine(); //1行目のみwhileループ外で行う
			if(currentLine.indexOf(",") > -1){
				punctuation = ",";
			}else if(currentLine.indexOf(" ") > -1){
				punctuation = " ";
			}else if(currentLine.indexOf("\t") > -1){
				punctuation = "\t";
			}else{
				scan.close();
				throw new Exception();
			}

			// 読み込みファイル1行目のみループ外で処理する
			pancPos = currentLine.indexOf(punctuation);
			label[Integer.parseInt(currentLine.substring(0, pancPos))] = currentLine.substring(pancPos+1);

			// ループ開始
			while(scan.hasNextLine()){
				currentLine = scan.nextLine();
				pancPos = currentLine.indexOf(punctuation);
				label[Integer.parseInt(currentLine.substring(0, pancPos))] = currentLine.substring(pancPos+1);
			}

			scan.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}

	/** 引数n,mをもつ辺のindexを返す*/
	public int searchEdge(int n,int m){
		int index;
		for(index=0 ; index<M ; index++){
			if( (list[index][0]==n&&list[index][1]==m) || (list[index][0]==m&&list[index][1]==n) ){
				break;
			}
		}
		return index;
	}

	/** サイト・パーコレーションを実行 */
	public void SitePercolation(double f){
		if(success){
			double x;
			int newM=0;
			ArrayList<Integer> vacantNodeList = new ArrayList<Integer>();
			int[] vacantNodeBinary = new int[N];
			for(int i=0;i<N;i++) vacantNodeBinary[i]=0;
			int currentLink=0;
			int[][] newList = new int[list.length][2];
			boolean occupied = true;
			for(int n=0 ; n<N ; n++){
				x = Math.random();
				// 非占有状態( !(x<f) のとき)なら次を実行
				if( !(x<f) ){
					vacantNodeList.add(n);
					vacantNodeBinary[n]=1;
				}else{
					existNodeList.add(n);
				}
			}
			for(int n=0;n<N;n++){
				if(vacantNodeBinary[n]==1){
					// ノードが空ならば無視し
					// ポインタを次の頂点へ移す
					currentLink += degree[n];
				}else{
					// 現在の辺の存在を判定
					// 以下、頂点nの次数の分、辺をループ
					for(int m=currentLink;m<(currentLink+degree[n]);m++){
						// 以下、空のノードリストと比較
						for(int i=0;i<vacantNodeList.size();i++){
							occupied = true;
							if(list[m][1]==vacantNodeList.get(i)){
								occupied = false;
								break;
							}
							if(list[m][1]<vacantNodeList.get(i)){
								break;
							}
						}
						if(occupied){
							newList[newM][0] = list[m][0];
							newList[newM][1] = list[m][1];
							newM++;
						}
					}
					currentLink += degree[n];
				}
			}
			// newListをlistへコピー(配列の長さはnewMへ制限)
			list = new int[newM][2];
			for(int m=0 ; m<newM ; m++){
				list[m][0] = newList[m][0];
				list[m][1] = newList[m][1];
			}

			// 変数更新
			if(directed) M=newM;
			else M=(newM/2);


		}else{
			System.out.println("生成に失敗しているためパーコレーションできません。");
		}
	}

	/** ボンド・パーコレーションを実行
	 * 無向,ダブルカウントのとき不具合があるかも？(16/12/14)*/
	public void BondPercolation(double f){
		if(success){
			double x;
			int newM=0;
			int[][] newList = new int[list.length][2];
			for(int m=0 ; m<list.length ; m++){
				x = Math.random();
				// 占有状態( x<f のとき)なら次を実行
				if(x<f){
					newList[newM][0] = list[m][0];
					newList[newM][1] = list[m][1];
					newM++;
				}
			}
			// newListをlistへコピー(配列の長さはnewMへ制限)
			list = new int[newM][2];
			for(int m=0 ; m<newM ; m++){
				list[m][0] = newList[m][0];
				list[m][1] = newList[m][1];
			}

			// 変数更新
			if(directed) M=newM;
			else M=(newM/2);
		}else{
			System.out.println("生成に失敗しているためパーコレーションできません。");
		}
	}

	/** 探索アルゴリズムを実行し結果をプリント */
	public int SearchAlgorithm(boolean print){
		if(success){
			// nodeListが未定義のときここで定義される
			// (nodeListはサイト・パーコレーション メソッドにて定義されている変数)
			if(existNodeList.isEmpty()){
				for(int i=0;i<N;i++) existNodeList.add(i);
			}

			// 探索用変数
			int[] vis = new int[N];
			int currentVis = 0;
			int currentNode;
			for(int i=0;i<N;i++) vis[i]=0;
			ArrayList<Integer> queue = new ArrayList<Integer>();

			// プロット用変数
			int compN = 0;
			int nodes;
			int maxNodes=0;

			// 探索部分
			for(int i=0;i<existNodeList.size();i++){
				if(vis[existNodeList.get(i)]==0){
					nodes=0;
					compN++;
					queue.add(existNodeList.get(i));
					vis[existNodeList.get(i)] = (++currentVis);
					nodes++;
					while(!queue.isEmpty()){
						currentNode = queue.get(0);
						queue.remove(0);

						for(int k=0;k<list.length;k++){
							if(list[k][0]==currentNode && vis[list[k][1]]==0){
								queue.add(list[k][1]);
								vis[list[k][1]] = (++currentVis);
								nodes++;
							}
						}
					}
					maxNodes = Math.max(maxNodes, nodes);
				}
			}
			if(print){
				System.out.println("連結成分数=" + compN);
				System.out.println("最大連結成分の頂点数=" + maxNodes);
			}
			return maxNodes;
		}else{
			System.out.println("生成に失敗しているため探索できません。");
			return 0;
		}
	}

	/** 頂点の媒介中心性を計算しプロットする(Brandesらの方法)
	* 無向グラフのみ実行可能 */
	public void betweenCentrality(){
		// 変数定義
		Node currentNode;
		ArrayList<Node> stack = new ArrayList<Node>();
		ArrayList<Node> queue = new ArrayList<Node>();
		int[] distance = new int[N];
		double[] sigma = new double[N];
		double[] delta = new double[N];
		int v,w,x,y;
		 // P:pの集合(lenght=N)
		 //└p[i]:頂点iのリスト(要素:Node)
		 // └Node
		 //
		 //↑この構造を作る
		 ArrayList<ArrayList<Node>> P = new ArrayList<ArrayList<Node>>();
		 for(int n=0;n<N;n++){
			 P.add(new ArrayList<Node>());
		 }

		for(int s=0;s<N;s++){
			// 初期化
			stack.clear(); queue.clear();
			for(int i=0;i<N;i++){
				distance[i] = -1;
				sigma[i] = 0;
				P.get(i).clear();
			}

			// 頂点sに対する処理
			distance[s] = 0;
			sigma[s] = 1;
			queue.add(nodeList.get(s));

			// 主となる処理
			while(!queue.isEmpty()){
				currentNode = queue.get(0);
				v = currentNode.index;
				stack.add( currentNode );
				queue.remove(0);
				// 現頂点の隣接頂点についてループ
				for(int neightbor=0 ; neightbor<currentNode.list.size() ; neightbor++){
					// 現ループの隣接頂点のindexをwとおく
					w = currentNode.list.get(neightbor).index;
					// wが未訪問のとき
					if(distance[w]<0){
						queue.add( nodeList.get(w) );
						distance[w] = distance[v]+1;
					}
					// sからwへの最短経路にvが含まれるとき
					// (⇔distance[w] = distance[v]+1のとき)
					if(distance[w] == distance[v]+1){
						sigma[w] += sigma[v];
						P.get(w).add(currentNode);
					}
				}
			}
			// 初期化
			for(int n=0;n<N;n++) delta[n]=0;
			// stackは頂点sからの距離が遠い順で返す
			while(!stack.isEmpty()){
				x = stack.get(stack.size()-1).index;
				stack.remove(stack.size()-1);
				for(int i=0 ; i<P.get(x).size() ; i++){
					y = P.get(x).get(i).index;
					delta[y] += (sigma[y]/sigma[x])*(1+delta[x]);
				}
				if(x!=s){
					nodeList.get(x).betweenCentrality += delta[x];
				}
			}
		}

		// 結果表示
		for(int n=0;n<N;n++){
			System.out.println(n + "\t" + nodeList.get(n).betweenCentrality);
		}
	}

	// Networkｵﾌﾞｼﾞｪｸﾄを複製できるようにメソッド追加
	public Network clone(){
		try {
			return (Network)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	protected class Node{
		int index;
		double betweenCentrality;
		ArrayList<Node> list = new ArrayList<Node>();
		ArrayList<Edge> eList = new ArrayList<Edge>();

		Node(int inputIndex){
			index = inputIndex;
			betweenCentrality = 0;
		}

	}

	protected static class Edge{
		int index;
		int[] node = new int[2];

		Edge(int i,int j) {
			node[0]=i;
			node[1]=j;
		}

		Edge(int i,int j,int inputIndex) {
			node[0]=i;
			node[1]=j;
			index = inputIndex;
		}

	}

}
