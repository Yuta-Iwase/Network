
// 説明:
//// Gephiの File/Export/Graph file...
//// により出力されるcsvを隣接リストの形式にして
//// 再出力させる

// 使い方:
//// このオブジェクトの関数のListingを使う
//// 引数は、１つ目はGephiから出力されたcsv、２つ目は再出力したいファイル
//// それぞれパス(相対or絶対)で入力する
//// ３つ目は有向であるかを入力する(省略可、その場合有向として出力される)

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class GephiListing {
	public void Listing(String inputFilePath,String outputFilePath,boolean directed){
		Scanner scan = null;
		PrintWriter pw = null;
		try {
			scan = new Scanner(new File(inputFilePath));
			pw = new PrintWriter(new File(outputFilePath));
			
			/// 1行目は頂点名なので無視
			if(scan.hasNextLine())scan.nextLine();
			
			/// 以降リスト化していく
			int currentNode=0;
			int startPos,currentPos,onePos,beforeNode,betweenNodeN;
			String targetLine;
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
					if(directed || (!directed && currentNode < beforeNode+betweenNodeN))
						pw.println(currentNode + "\t" + (beforeNode+betweenNodeN));
					beforeNode = beforeNode+betweenNodeN;
					currentPos = onePos;
				}
				
				currentNode++;
			}
			
			scan.close();
			pw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void Listing(String inputFilePath,String outputFilePath){
		Listing(inputFilePath,outputFilePath,true);
	}
	
}
