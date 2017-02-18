import java.io.File;
import java.util.Scanner;

// .csvファイルを読み込む
// 重み付きの場合、
//   頂点,頂点,重み
// という書式で書く。
// 頂点間の区切りは原則カンマとするが、スペースやタブでも対応できるようにしておく。
// 注意点：
// ・引数doubleCountは読み込み時のデータがダブルカウントであるかの真偽
// ・引数makeDoubleCountは作成時、ダブルカウント的隣接リストを作るかの真偽
// 　有向の場合確定でfalseとなる

// 問題点:
// ・ラベルに対応していない
// ・頂点番号は、0から順に全て存在していると仮定する

// 更新点：
// ・(2016/12/13)バグを修正



public class NetworkForCSVFile extends Network {
	// 4つ目の変数を省略した場合、falseとして読み込まれる。
	public NetworkForCSVFile(String inputFilePath, boolean directed, boolean weighted) {
		this(inputFilePath,directed,weighted,false,false);
	}
	public NetworkForCSVFile(String inputFilePath, boolean directed, boolean weighted, boolean doubleCount ,boolean makeDoubleCount) {
		if(doubleCount && !makeDoubleCount){
			try{throw new Exception();} catch(Exception e){
				System.out.println("工事中です、4,5つ目の引数がこの組み合わせの時のプログラムをまだ書いてません。");
				};
			System.exit(1);
		}
		Scanner scan = null;
		Scanner dummyScan = null; //Mを測るためのScannerクラス
		N=0;
		M=0;
		this.directed = directed;
		this.weighted = weighted;

		String punctuation = "";
		String currentLine;
		int maxNodeIndex;
		int[] pancPos = new int[2];
		int currentLineIndex = 0;
		try{
			scan = new Scanner(new File(inputFilePath));
			dummyScan = new Scanner(new File(inputFilePath));

			//// N,Mを数える

			// 区切り文字の識別
			currentLine = dummyScan.nextLine(); //1行目のみwhileループ外で行う
			if(currentLine.indexOf(",") > -1){
				punctuation = ",";
			}else if(currentLine.indexOf(" ") > -1){
				punctuation = " ";
			}else if(currentLine.indexOf("\t") > -1){
				punctuation = "\t";
			}else{
				success = false;
			}
			maxNodeIndex = -1;

			// N,Mの更新
			M++;
			if(weighted){
				pancPos[0] = currentLine.indexOf(punctuation); // 一つ目の区切り文字を探す
				pancPos[1] = currentLine.substring(pancPos[0]+1).indexOf(punctuation) + (pancPos[0]+1); // 二つ目の区切り文字を探す

				maxNodeIndex = Math.max(Integer.parseInt(currentLine.substring(0, pancPos[0])), maxNodeIndex);
				maxNodeIndex = Math.max(Integer.parseInt(currentLine.substring(pancPos[0]+1,pancPos[1])), maxNodeIndex);
			}else{
				pancPos[0] = currentLine.indexOf(punctuation); // 一つ目の区切り文字"のみ"を探す

				maxNodeIndex = Math.max(Integer.parseInt(currentLine.substring(0, pancPos[0])), maxNodeIndex);
			}
			while(dummyScan.hasNextLine()){
				currentLine = dummyScan.nextLine();
				// N,Mの更新
				M++;
				if(weighted){
					pancPos[0] = currentLine.indexOf(punctuation); // 一つ目の区切り文字を探す
					pancPos[1] = currentLine.substring(pancPos[0]+1).indexOf(punctuation) + (pancPos[0]+1); // 二つ目の区切り文字を探す

					maxNodeIndex = Math.max(Integer.parseInt(currentLine.substring(0, pancPos[0])), maxNodeIndex);
					maxNodeIndex = Math.max(Integer.parseInt(currentLine.substring(pancPos[0]+1,pancPos[1])), maxNodeIndex);
				}else{
					pancPos[0] = currentLine.indexOf(punctuation); // 一つ目の区切り文字"のみ"を探す

					maxNodeIndex = Math.max(Integer.parseInt(currentLine.substring(0, pancPos[0])), maxNodeIndex);
					maxNodeIndex = Math.max(Integer.parseInt(currentLine.substring(pancPos[0]+1)), maxNodeIndex);
				}
			}
			N = maxNodeIndex+1;


			//// 得たNを用いてlist[][],degree[],weight[]を初期化
			// ダブルカウント的隣接リストを作成する場合はM*2となる
			if(!directed && !doubleCount && makeDoubleCount){
				list = new int[M*2][2];
				weight = new double[M*2];
			}else{
				list = new int[M][2];
				weight = new double[M];
			}
			degree = new int[N];
			for(int i=0;i<N;i++) degree[i]=0;


			//// 頂点,次数,重みを定義
			while(scan.hasNextLine()){
				currentLine = scan.nextLine();
				if(weighted){
					pancPos[0] = currentLine.indexOf(punctuation); // 一つ目の区切り文字を探す
					pancPos[1] = currentLine.substring(pancPos[0]+1).indexOf(punctuation) + (pancPos[0]+1); // 二つ目の区切り文字を探す

					list[currentLineIndex][0] = Integer.parseInt(currentLine.substring(0, pancPos[0]));
					list[currentLineIndex][1] = Integer.parseInt(currentLine.substring(pancPos[0]+1,pancPos[1]));
					degree[list[currentLineIndex][0]]++;
					degree[list[currentLineIndex][1]]++;
					weight[currentLineIndex]  = Double.parseDouble(currentLine.substring(pancPos[1]+1));
					currentLineIndex++;
				}else if(doubleCount || !makeDoubleCount){
					pancPos[0] = currentLine.indexOf(punctuation); // 一つ目の区切り文字"のみ"を探す
					list[currentLineIndex][0] = Integer.parseInt(currentLine.substring(0, pancPos[0]));
					list[currentLineIndex][1] = Integer.parseInt(currentLine.substring(pancPos[0]+1));
					degree[list[currentLineIndex][0]]++;
					degree[list[currentLineIndex][1]]++;
					weight[currentLineIndex]  = 1.0;
					currentLineIndex++;
				}else{
					pancPos[0] = currentLine.indexOf(punctuation); // 一つ目の区切り文字"のみ"を探す
					list[currentLineIndex][0] = Integer.parseInt(currentLine.substring(0, pancPos[0]));
					list[currentLineIndex][1] = Integer.parseInt(currentLine.substring(pancPos[0]+1));
					list[currentLineIndex+1][0] = Integer.parseInt(currentLine.substring(pancPos[0]+1));
					list[currentLineIndex+1][1] = Integer.parseInt(currentLine.substring(0, pancPos[0]));
					degree[list[currentLineIndex][0]]++;
					degree[list[currentLineIndex][1]]++;
					weight[currentLineIndex]  = 1.0;
					currentLineIndex += 2;
				}
			}
			// 次数が二度加算されているため半減させる
			if(doubleCount){
				for(int i=0;i<N;i++) degree[i]/=2;
				M /= 2;
			}

			if(makeDoubleCount) this.doubleCount=true;

			// 終了処理
			scan.close();
			dummyScan.close();
		}catch(Exception e){
			System.out.println(e);
			success = false;
		}
	}
}
