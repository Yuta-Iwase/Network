
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class question513_2 {
	public static void main(String[] args) {
		PrintWriter pw = null;
		Scanner scan = null;
		String text;		
		try {
			// 書き込み用ファイル定義
			pw = new PrintWriter(new File("question513_2.gexf"));
			// テキスト読み込み
			File file = new File("ALICE'S ADVENTURES IN WONDERLAND.txt");
			scan = new Scanner(file);
			text = scan.nextLine();
			text = text.toLowerCase();
			// 検索をかけた単語の両端を定める数
			int old=-1, now=-1;
			// 次に登録する単語のID
			int wordID=0;
			
			////△ 単語登録
			String[] word = new String[11000];
			do{
				// 直近のドット、カンマ、スペースを検索
				int dot, comma, space;
				dot = text.indexOf(".",now+1);
				comma = text.indexOf(",",now+1);
				space = text.indexOf(" ",now+1);
				
				// 1～3行下の文は4行下の文のエラーを防ぐ文
				if(dot==-1) dot=Integer.MAX_VALUE;
				if(comma==-1) dot=Integer.MAX_VALUE;
				if(space==-1) dot=Integer.MAX_VALUE;
				now = Math.min(Math.min(dot, comma), space);
				
				// 現ループでの単語をnowWordとする
				String nowWord = text.substring(old+1, now);
				
				// 条件1 単語の文字数が1以上
				boolean bool1 = now-old>1;
				// 条件2 新出単語である
				boolean bool2 = true;
				for(int i=0 ; i<wordID ; i++){
					if(nowWord.equals(word[i])){
						bool2 = false;
					}
				}
				
				// 条件1,2を満たす場合は登録処理
				if(bool1 && bool2){
						word[wordID] = text.substring(old+1, now);
						wordID++;	
				}
				old=now;
			}while(text.indexOf(".",now+1)!=text.length()-1);
			////△
			
			////○ 頂点登録
			Node[] node = new Node[wordID];
			for(int i=0 ; i<wordID ; i++){
				node[i] = new Node(word[i],wordID);
			}
			////○
			
			////☆ 隣接ベクトルを求める
			old=-1;
			now=-1;
			// 検索をかけた現ループの次の単語の両端を定める数
			int nextOld=-1, nextNow=-1;
			// ループ開始
			do{				
				/// 現ループ内の単語(以下、現単語)を検索
				// 直近のドット、カンマ、スペースを検索
				int dot, comma, space;
				dot = text.indexOf(".",now+1);
				comma = text.indexOf(",",now+1);
				space = text.indexOf(" ",now+1);
				// 1～3行下の文は4行下の文のエラーを防ぐ文
				if(dot==-1) dot=Integer.MAX_VALUE;
				if(comma==-1) comma=Integer.MAX_VALUE;
				if(space==-1) space=Integer.MAX_VALUE;
				now = Math.min(Math.min(dot, comma), space);
				// 現ループでの単語をnowWordとする
				String nowWord = text.substring(old+1, now);
				// 現単語のIDを検索
				int nowWordID=-1;
				for(int i=0 ; i<wordID ; i++){
					if(nowWord.equals(node[i].getWord())){
						nowWordID=i;
					}
				}
				
				/// 現単語を元に条件1,3を定義
				// 条件1 単語の文字数が1以上
				boolean bool1 = now-old>1;
				// 条件3 現単語が文末である
				boolean bool3 = (Math.min(dot, comma) < space);
				
				if(bool1){
					// 現ループでの単語が文末かで分岐
					// 文末でない
					if(!bool3){
						nextOld = now;
						nextNow = now;
						// 直近のドット、カンマ、スペースを検索
						int nextDot, nextComma, nextSpace;
						nextDot = text.indexOf(".",nextNow+1);
						nextComma = text.indexOf(",",nextNow+1);
						nextSpace = text.indexOf(" ",nextNow+1);
						// 1～3行下の文は4行下の文のエラーを防ぐ文
						if(nextDot==-1) nextDot=Integer.MAX_VALUE;
						if(nextComma==-1) nextComma=Integer.MAX_VALUE;
						if(nextSpace==-1) nextSpace=Integer.MAX_VALUE;
						nextNow = Math.min(Math.min(nextDot, nextComma), nextSpace);
						// 現ループでの単語の次の単語(以下、次単語)をnextWordとする
						String nextWord = text.substring(nextOld+1, nextNow);
						
						// 条件1'  次単語の文字数が1以上
						boolean bool1Alt = nextNow-nextOld>1;
						
						if(bool1Alt){
							// 次単語のIDを検索
							int nextWordID=-1;
							for(int i=0 ; i<wordID ; i++){
								if(nextWord.equals(node[i].getWord())){
									nextWordID=i;
								}
							}							
							
							// 現単語の隣接ベクトルにカウント
							node[nowWordID].count(nextWordID);
							// 次単語の隣接ベクトルにカウント
							node[nextWordID].count(nowWordID);
						}
					}
					// 文末である
					else{
						// 文末ではカウント作業を行わない
						// 従ってelse内では何も行わない
					}
				}
				old=now;
			}while(text.indexOf(".",now+1)!=text.length()-1);
			////☆
			
			//// 辺を生成
			int edgeN = 0;
			for(int i=0 ; i<node.length ; i++){
				for(int j=0 ; j<wordID ; j++){
					if(node[i].adj[j] != 0) edgeN++; 
				}
			}
			Edge[] edge = new Edge[edgeN];
			int edgeID=0;
			for(int i=0 ; i<node.length ; i++){
				for(int j=0 ; j<wordID ; j++){
					if(node[i].adj[j] != 0){
						edge[edgeID] = new Edge(i,j,node[i].adj[j]);
						edgeID++;
					}
				}
			}
			
			//// 結果を.gext形式でプロット
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			pw.println("<gexf version=\"1.1\">");
			pw.println("<graph defaultedgetype=\"undirected\" idtype=\"string\" type=\"static\">");
			pw.println("<nodes count=\"" + node.length + "\">");
			for(int i=0 ; i<node.length ; i++){
				pw.println("<node id=\"" + (double)i + "\" label=\"" + node[i].getWord() + "\"/>");
			}
			pw.println("</nodes>");
			pw.println("<edges count=\"" + edge.length + "\">");
			for(int i=0 ; i<edge.length ; i++){
				pw.println("<edge id=\"" + (double)i + 
						"\" source=\"" + (double)edge[i].getSource() +
						"\" target=\"" + (double)edge[i].getTarget() +
						"\" weight=\"" + (double)edge[i].getWeight() +
						"\"/>");
			}
			pw.println("</edges>");
			pw.println("</graph>");
			pw.println("</gexf>");
			
			// 次数初期化
			int[] deg = new int[1000];
			for(int i=0 ; i<deg.length ; i++){
				deg[i] = 0;
			}
			// 次数計算
			int lastDeg=0;
			for(int i=0 ; i<node.length ; i++){
				int sum=0;
				for(int j=0 ; j<node[i].adj.length ;j++){
					sum += node[i].adj[j];
				}
				deg[sum]++;
				if(sum>lastDeg)lastDeg=sum;
			}
			
			
			// 次数分布
			for(int i=0 ; i<=lastDeg ; i++){
				if(deg[i] != 0){
					System.out.println(i + "\t" + deg[i]);
				}
			}

			
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} finally{
			if(scan != null){
				pw.close();
				scan.close();
			}
		}
	}
}
class Node{
	// 頂点名
	String word;
	// 隣接ベクトル
	int[] adj;
	Node(String s,int n){
		// 頂点名初期化
		word = s;
		// 隣接ベクトルをn次(単語数)にする
		adj = new int[n];
		// 初期化時点ではベクトルの全成分は0にする
		for(int i=0 ; i<n ; i++){
			adj[i]=0;
		}
	}
	
	String getWord(){
		return word;
	}
	// 隣接ベクトルのi番目にカウントする
	void count(int i){
		adj[i]++;
	}
}
class Edge{
	int source;
	int target;
	int weight;
	Edge(int s,int t,int w) {
		source = s;
		target = t;
		weight = w;
	}
	
	int getSource(){return source;}
	int getTarget(){return target;}
	int getWeight(){return weight;}
}
