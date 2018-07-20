
// 説明:
//// Gephiの File/Export/Graph file...
//// により出力されるcsvをNetworkクラスに様式に合うように読み込む

// 課題:
//// ・重み付きネットワークに対応していない
//// ・(頂点のラベル情報が失われてしまう)

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class GephiNetwork extends Network{

	public GephiNetwork(String inputFilePath, boolean directed, boolean weighted){
		Scanner scan = null;
		ArrayList<Integer> linkArrayList = new ArrayList<Integer>();
		ArrayList<Double> weightArrayList = new ArrayList<>();
		N=0;
		M=0;
		this.directed = directed;
		this.weighted = weighted;

		try {
			scan = new Scanner(new File(inputFilePath));
			int currentNode=0;
			int startPos,currentPos,onePos,betweenNodeN;
			String targetLine;
			N=0;
			M=0;
			success=true;

			/// 1行目を用いて頂点数を計測
			if(scan.hasNextLine()){
				targetLine = scan.nextLine();
				currentPos=-1;
				while(targetLine.indexOf(";", currentPos+1)>=0){
					N++;
					currentPos = targetLine.indexOf(";", currentPos+1);
				}
			}else{
				success=false;
				System.out.println("1行目がありません。");
			}

			degree = new int[N];
			for(int i=0;i<N;i++) degree[i]=0;

			boolean endOfLine;
			if(success){
				/// 以降リスト化していく
				while(scan.hasNextLine()){
					// 頂点名の右端にまで移動
					targetLine = scan.nextLine();
					currentPos=targetLine.indexOf(";",0);

					int pairNode = 0;
					endOfLine = false;
					searth:while(true){
						int nextSemicolonPos = targetLine.indexOf(";",currentPos+1);
						if(nextSemicolonPos==-1) {
							endOfLine = true;
							nextSemicolonPos = targetLine.length();
						}

						String currentWeight_string = targetLine.substring(currentPos+1, nextSemicolonPos);
						if(!currentWeight_string.equals("0")) {
							double currentWeight = Double.parseDouble(currentWeight_string);
							weightArrayList.add(currentWeight);
							linkArrayList.add(currentNode);
							linkArrayList.add(pairNode);
							M++;
							degree[currentNode]++;
							pairNode++;
						}
						currentPos = nextSemicolonPos;

						if(endOfLine) break searth;
					}

					currentNode++;
				}
				list = new int[M][2];
				weight = new double[M];
				for(int m=0 ; m<M ; m++){
					list[m][0] = linkArrayList.get(0);
					linkArrayList.remove(0);
					list[m][1] = linkArrayList.get(0);
					linkArrayList.remove(0);
					weight[m] = weightArrayList.get(0);
					weightArrayList.remove(0);
				}
			}

			// コンストラクタの引数、directedが偽のとき(無向のとき)
			// パラメータMを半減させる
			// 更に必ずダブルカウントされているのでdoubleCountをtrueとする(16/12/14更新)
			if(!directed){
				M/=2;
				this.doubleCount = true;
			}

			scan.close();
			success=true;
		} catch (Exception e) {
			System.out.println(e);
			success=false;
		}
	}

	public GephiNetwork(String inputFilePath, boolean directed){
		Scanner scan = null;
		ArrayList<Integer> linkArrayList = new ArrayList<Integer>();
		N=0;
		M=0;
		this.directed = directed;

		try {
			scan = new Scanner(new File(inputFilePath));
			int currentNode=0;
			int startPos,currentPos,onePos,beforeNode,betweenNodeN;
			String targetLine;
			N=0;
			M=0;
			success=true;

			/// 1行目を用いて頂点数を計測
			if(scan.hasNextLine()){
				targetLine = scan.nextLine();
				currentPos=-1;
				while(targetLine.indexOf(";", currentPos+1)>=0){
					N++;
					currentPos = targetLine.indexOf(";", currentPos+1);
				}
			}else{
				success=false;
				System.out.println("1行目がありません。");
			}

			degree = new int[N];
			for(int i=0;i<N;i++) degree[i]=0;

			if(success){
				/// 以降リスト化していく
				// 1行ずつリスト化しoutputFileに書き込み
				while(scan.hasNextLine()){
					// 頂点名の右端にまで移動
					targetLine = scan.nextLine();
					startPos=targetLine.indexOf(";",0);

					currentPos=startPos;
					beforeNode=0;
					searthOne:while(true){
						onePos = targetLine.indexOf("1.0",currentPos+1);
						if(onePos==-1) break searthOne;
						betweenNodeN = ((onePos - currentPos) - 1)/2;
						linkArrayList.add(currentNode);
						linkArrayList.add((beforeNode+betweenNodeN));
						M++;
						degree[currentNode]++;
						beforeNode = beforeNode+betweenNodeN;
						currentPos = onePos;
					}

					currentNode++;
				}
				list = new int[M][2];
				for(int m=0 ; m<M ; m++){
					list[m][0] = linkArrayList.get(0);
					linkArrayList.remove(0);
					list[m][1] = linkArrayList.get(0);
					linkArrayList.remove(0);
				}
			}

			// コンストラクタの引数、directedが偽のとき(無向のとき)
			// パラメータMを半減させる
			// 更に必ずダブルカウントされているのでdoubleCountをtrueとする(16/12/14更新)
			if(!directed){
				M/=2;
				this.doubleCount = true;
			}

			scan.close();
			success=true;
		} catch (Exception e) {
			System.out.println(e);
			success=false;
		}
	}

}
