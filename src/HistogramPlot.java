import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

// 正規化されていない散布図を
// gnuplotのヒストグラム用に編集します。
// [注意]
// ・面倒なので要素間のスペースはtabにしか対応していません。
// ・面倒なのでxの値が昇順に並べてあるものしか対応しません。

// 17/01/21更新
// ・規格化オプション追加
// ・区間分割の不具合修正
//   例)刻み幅0.05のとき区間数が21になっていた

public class HistogramPlot {
	public static void main(String args[]) throws Exception{
		// 規格化オプション
		boolean normalize = true;
		
		// 入力情報
//		String fileName = "NodeBC.txt";
//		String fileName = "EdgeBC.txt";
//		String fileName = "Salience_dist.txt";
		String fileName = "PercolationTest3/after.txt";
		double lengthRate = 0.05; //刻み幅(割合表記)
		
		// 情報読み込み
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();
		Scanner scan = new Scanner(new File(fileName));
		String currentLine,xString,yString;
		int tabPos;
		while(scan.hasNext()){
			currentLine = scan.nextLine();
			tabPos = currentLine.indexOf("\t");
			xString = currentLine.substring(0,tabPos);
			yString = currentLine.substring(tabPos+1);
			x.add(Double.parseDouble(xString));
			y.add(Double.parseDouble(yString));
		}
		
		// 現在の区間内の個数(height)を積算
		final double sectionLength = x.get(x.size()-1) * lengthRate; //ひと区間の長さ
		int currentSectionLeft = 0;
		int currentIndex = 0;
		int currentHeight;
		ArrayList<Integer> height = new ArrayList<Integer>();
		while(currentSectionLeft < x.get(x.size()-1)){
			// 丸め誤差修正
			if(Math.pow(currentSectionLeft-x.get(x.size()-1),2)<100){
				break;
			}
			currentHeight = 0;
			while(x.get(currentIndex) < (currentSectionLeft+sectionLength)){
				currentHeight += y.get(currentIndex);
				currentIndex++;
				if(currentIndex >= y.size()) break;
			}
			height.add(currentHeight);
			
			currentSectionLeft += sectionLength;
		}
		int rightTopHeight = 0;
		for(int i=0;i<x.size();i++){
			if(x.get(i) >= x.get(x.size()-1)) rightTopHeight+=y.get(i);
		}
		if(rightTopHeight!=0){
			height.add(height.get(height.size()-1)+rightTopHeight);
			height.remove(height.size()-2);
		}
		
		// プロット(gnuplotのwith boxesに対応するように出力)
		if(normalize){
			double maxH=Double.MIN_VALUE;
			for(int i=0;i<height.size();i++){
				if(maxH < height.get(i)) maxH=height.get(i);
			}
			double currentPos = lengthRate/2;
			for(int i=0;i<height.size();i++){
				System.out.println(currentPos + "\t" + height.get(i)/maxH);
				currentPos += lengthRate;
			}
		}else{
			double currentPos = sectionLength/2;
			for(int i=0;i<height.size();i++){
				System.out.println(currentPos + "\t" + height.get(i));
				currentPos += sectionLength;
			}
		}
		
		scan.close();
	}
}
