// 五十嵐さんのプログラム

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchConnectedComponent {

	public static void main(String[] args)throws Exception {
		Scanner scan1 = new Scanner (new File("aaaa.csv"));
		Scanner scan2 = new Scanner (new File("aaaa.csv"));
		
		int N=0;
		int M=0;//Nは頂点数でMが辺の数
		int a,b;
		//辺カウント
		
		while(scan1.hasNextInt()){
			M++;
			a = scan1.nextInt();
			b = scan1.nextInt();
			if(N<a)N=a;
			if(N<b)N=b;//Nを更新していく
		}
		N=N+1;
//		System.out.println(N);
//		System.out.println(M);//Mはランダムに生成される
		
		//NとMが判明した！
		//edgeListを作成していく
		int[][] edgeList = new int[M][2];
		int[] degree = new int[N];
		for(int i = 0; i < N;i++){
			degree[i]=0;
		}
		int currentLine = 0;
		while(scan2.hasNextInt()){
			a = scan2.nextInt();
			b = scan2.nextInt();
			edgeList[currentLine][0]=a;
			edgeList[currentLine][1]=b;//edgeList作成
//			System.out.println(edgeList[currentLine][0]+"\t"+edgeList[currentLine][1]);
			degree[a]++;
			degree[b]++;
			currentLine++;
		}
		for(int i = 0;i < N;i++){
			degree[i] = (int)(degree[i]*0.5);
//			System.out.println(i + "\t" + degree[i]);//次数の出力
		}
	
		//ここまでがデータの読み込み作業！！
		//ここから連結成分の探索をしていく
		int[]flag = new int[N];
		int[]address = new int[N];
		
		address[0] = 0;//address[]は,edgeListの何番目の頂点のはじまりが、何番目か
		flag[0] = 0;
		for (int i = 1;i < N;i++){
			address[i] = address[i-1] + degree[i-1];//前のアドレスに、前の番目の頂点分を足せばよい
			flag[i] = address[i];//edgeListをうめるにあたって、どこまで頂点のつながっている相手がうまっているか
	}
		for(int i = 0;i < N;i++){
//			System.out.println(i + "\t" + address[i]);
		}
		
		int[] label = new int[N];//頂点番号がどの大陸に属しているか
		int currentNode;
		int currentLabel = 0;
		int neighbor;
		boolean[]visitQ = new boolean[N];
		for (int i = 0;i < N;i++){
			visitQ[i] = false;
		}
		
		ArrayList<Integer>queue=
				new ArrayList<Integer>();
		//全頂点分,○
		for(int n = 0;n < N;n++){
			if(visitQ[n]){
				continue;
			}
			//ここから同一連結成分の探索☆
			queue.add(n);
			visitQ [n]=true;
			label[n] = currentLabel;
			while(!queue.isEmpty()){//キューが空じゃないないなら続行する
					currentNode = queue.get(0);//キューの底、０から始まる
					queue.remove(0);//キューの一番底を取り除いたことになる
				for(int i = 0;i < degree[currentNode];i++){
					neighbor = edgeList[address[currentNode]+i][1];
					if(!visitQ[neighbor]){
						queue.add(neighbor);	//未探索だった場合、続ける
						visitQ[neighbor]=true;
						label[neighbor] = currentLabel;
					}
				}
			}
			currentLabel++;//ここまで☆,○
		}
		
		int[] componentSizeList = new int[currentLabel];//一個一個の大陸のサイズ
		
		for(int i = 0; i < N; i++){
			componentSizeList[label[i]]++;
			System.out.println(i + "\t" + label[i]);//各ノードが何番の大陸に属しているのか？
		}
		System.out.println(currentLabel);//連結成分(大陸)の個数
		for(int i = 0;i < componentSizeList.length;i++){
//			System.out.println(i + "\t"+componentSizeList[i]);
		}
		scan1.close();
		scan2.close();
	}

}