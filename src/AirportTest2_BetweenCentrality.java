/////////////////////////////////////
/////////////失敗作//////////////////
/////////////////////////////////////


import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class AirportTest2_BetweenCentrality {
	static ArrayList<Node> nodes = new ArrayList<Node>();
	static ArrayList<Walk> stack = new ArrayList<Walk>();

	public static void main(String[] args) {
		read("S10b-14_BetAport_Roman_AvoidMultiple.csv");

		int currentIndex;
		int currentHop;
		int minHop;
		// allWalkN : 最短経路の総数
		// walkN : 最短経路の内、目的の頂点を含む経路数
		int allWalkN=0,walkN=0;

		double[] betweenCentrality = new double[nodes.size()];

		for(int n=0;n<nodes.size();n++){
			for(int start=0;start<nodes.size();start++){
				for(int goal=0;goal<start;goal++){
					if(n!=start && n!=goal && start!=goal){
						// 初期化
						allWalkN=0;
						walkN=0;
						stack.clear();
						for(int i=0;i<nodes.size();i++){
							nodes.get(i).visited=false;
							nodes.get(i).hop=0;
						}
						minHop = nodes.size()+1;

						stack.add(new Walk(nodes.get(start)));
						nodes.get(start).visited = true;
						nodes.get(start).hop=0;

						currentHop = 0;

						while(currentHop <= minHop){
							currentIndex = stack.get(0).getStabNode().index;

							for(int i=0;i<nodes.get(currentIndex).list.size();i++){
								// 新規登録
								if(!nodes.get(currentIndex).list.get(i).visited){
									// 新たな末端を加えた新経路追加
									stack.add(stack.get(0).prolong((nodes.get(currentIndex).list.get(i))) );
									// 末端を探索済みとする
									nodes.get(currentIndex).list.get(i).visited = true;
									// その頂点が何歩で到達されたか記録
									nodes.get(currentIndex).list.get(i).hop = (nodes.get(currentIndex).hop+1);
								}

								// 末端がゴールのときの処理
								if(nodes.get(currentIndex).list.get(i).index == goal){
									// ゴールが探索済みになると不都合なので、未探索に再更新
									nodes.get(currentIndex).list.get(i).visited = false;
									// 最小歩数が判明したため、それをminHopとして記録
									minHop = currentHop;
									// 最短経路数をカウント
									allWalkN++;
									// Node_iが含んでいる場合、カウント
									if(stack.get(0).searchNode(nodes.get(n))){
										walkN++;
									}
								}
							}
							stack.remove(0);
							currentHop = stack.get(0).getStabNode().hop;
						}
						betweenCentrality[n] += walkN/((double)allWalkN);
					}
				}
			}
			System.out.println(nodes.get(n).label + "\t" + betweenCentrality[n]);
		}

	}

	public static void read(String filePath){
		Scanner scan;
		String currentLine;
		String[] label = new String[2];
		int[] index = new int[2];
		boolean[] registered = new boolean[2];

		try {
			scan = new Scanner(new File(filePath));

			// 謎のバグ(恐らく文字コードのせい)により
			// 1行目の"東京"と2行目の"東京"だけ別の文字列と
			// 判定されてしまう
			// なので、csvを改行して1行目をダミーとして
			// 2行目から読み込んでいく
//			if(scan.hasNextLine())scan.nextLine();
			// ↑文字コード問題を解決したためCO

			while(scan.hasNextLine()){
				registered[0] = false;
				registered[1] = false;

				currentLine = scan.nextLine();
				label[0] = currentLine.substring(0,currentLine.indexOf(","));
				label[1] = currentLine.substring(currentLine.indexOf(",")+1);

				// 両頂点が登録済みであるか判定
				// 同時にindexも調べる
				for(int i=0;i<2;i++){
					search:for(int j=0;j<nodes.size();j++){
						if(label[i].equals(nodes.get(j).label)){
							registered[i] = true;
							index[i] = j;
							break search;
						}
					}
					if(!registered[i]){
						nodes.add(new Node(label[i]));
						index[i] = nodes.size()-1;
						nodes.get(index[i]).index = index[i];
					}
				}

				// 両方の頂点が登録済みである時、多重辺の可能性がある
				// それを判別し多重辺を回避
				if(registered[0] && registered[1]){
					for(int k=0 ; k<nodes.get(index[0]).list.size() ; k++){
						if(label[1].equals(nodes.get(index[0]).list.get(k).label)){
							continue;
						}
					}
				}

				nodes.get(index[0]).list.add(nodes.get(index[1]));
			}
			scan.close();

		}catch(Exception e){System.out.println(e);}
	}

	private static class Node{
		int index;
		int hop;
//		double rank;
		String label;
		ArrayList<Node> list = new ArrayList<Node>();
		boolean visited;

		Node(String inputLabel){
//			rank = 0;
			label = inputLabel;
		}

//		Node randomGet(){
//			int r = (int)(list.size()*Math.random());
//			return list.get(r);
//		}

	}

	private static class Walk{
		ArrayList<Node> walkNode = new ArrayList<Node>();

		// 新規用コンストラクタ
		Walk(Node node){
			walkNode.add(node);
		}

		// 追加用コンストラクタ
		Walk(ArrayList<Node> baseList,Node node){
			for(int i=0;i<baseList.size();i++){
				walkNode.add(baseList.get(i));
			}
			walkNode.add(node);
		}

		Node getStabNode(){
			return walkNode.get(walkNode.size()-1);
		}

		Walk prolong(Node node){
			return (new Walk(this.walkNode,node));
		}

		boolean searchNode(Node node){
			boolean exist = false;
			for(int i=0;i<walkNode.size();i++){
				if(walkNode.get(i).index == node.index) exist=true;
			}
			return exist;
		}
	}

}
