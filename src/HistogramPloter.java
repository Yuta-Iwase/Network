import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

// 読み
// int[] int[][] double[] double[][] "FileName"
// 出力
// plot(autorato,normalize,刻み数,(ファイル名))

/**
 * 新型
 */
public class HistogramPloter {
	int[][]		int_list;
	double[][]	double_list;

	int[]		frequency;

	public HistogramPloter() {
	}

	public void load(int[] input_list){
		int_list = new int[input_list.length][2];
		for(int i=0;i<input_list.length;i++){
			int_list[i][0] = i;
			int_list[i][1] = input_list[i];
		}
	}
	public void load(int[][] input_list){
		int_list = input_list;
	}
	public void load(double[] input_list){
		double_list = new double[input_list.length][2];
		for(int i=0;i<input_list.length;i++){
			double_list[i][0] = i;
			double_list[i][1] = input_list[i];
		}
	}
	public void load(double[][] input_list){
		double_list = input_list;
	}

	public void int_plot(boolean boxes, boolean y_normalize, String outputFilePath)throws Exception{
		// 出力ファイル設定
		PrintWriter pw = null;
		if(outputFilePath.length()>0){
			pw = new PrintWriter(new File(outputFilePath));
		}

		// 横軸の最小値,最高値を取得
		int minX = Integer.MAX_VALUE;
		int maxX = int_list[0][1];
		for(int i=0;i<int_list.length;i++){
			if(minX>int_list[i][1]){
				minX=int_list[i][1];
			}
			if(maxX<int_list[i][1]){
				maxX=int_list[i][1];
			}
		}

		// 区間数は(maxX-minX)+1個
		int ticks = (maxX-minX)+1;
//		double ticks_inverse = 1.0/ticks;

		// 度数をカウント
		frequency = new int[ticks];
		for(int i=0;i<int_list.length;i++){
			frequency[int_list[i][1]-minX]++;
		}

		if(y_normalize){
			// 縦軸の総和を計算
			int sumY = 0;
			for(int i=0;i<frequency.length;i++){
				sumY += frequency[i];
			}
			double sumY_inverse = 1.0/sumY;
			// 出力
			double length = 1.0;
			double currentPos = minX;
			if(boxes) currentPos += length*0.5;
			for(int i=0;i<frequency.length;i++){
				if(frequency[i]>0||boxes){
					if(outputFilePath.length()>0) pw.println(currentPos + "\t" + frequency[i]*sumY_inverse);
					System.out.println(currentPos + "\t" + frequency[i]*sumY_inverse);
				}
				currentPos += length;
			}
		}else{
			// 出力
			double length = 1.0;
			double currentPos = minX;
			if(boxes) currentPos += length*0.5;
			for(int i=0;i<frequency.length;i++){
				if(frequency[i]>0||boxes){
					if(outputFilePath.length()>0) pw.println(currentPos + "\t" + frequency[i]);
					System.out.println(currentPos + "\t" + frequency[i]);
				}
				currentPos += length;
			}
		}

		if(outputFilePath.length()>0) pw.close();
	}

	public void double_plot(int ticks, boolean boxes, boolean y_normalize,String outputFilePath)throws Exception{
		// 出力ファイル設定
		PrintWriter pw = null;
		if(outputFilePath.length()>0){
			pw = new PrintWriter(new File(outputFilePath));
		}

		// 横軸の最小値,最高値を取得
		double minX = Double.MAX_VALUE;
		double maxX = double_list[0][1];
		for(int i=0;i<double_list.length;i++){
			if(minX>double_list[i][1]){
				minX=double_list[i][1];
			}
			if(maxX<double_list[i][1]){
				maxX=double_list[i][1];
			}
		}
		double maxX_minus_minX_inverse = 1.0/(maxX-minX);

		// 区間数の逆数
		double ticks_inverse = 1.0/ticks;

		// 度数をカウント
		frequency = new int[ticks];
		int index;
		for(int i=0;i<double_list.length;i++){
			index = (int)((double_list[i][1]-minX)*maxX_minus_minX_inverse*ticks);
			if(index>=ticks)index--;
			frequency[index]++;
		}

		if(y_normalize){
			// 縦軸の最大値を取得
			int sumY = 0;
			for(int i=0;i<frequency.length;i++){
				sumY += frequency[i];
			}
			double sumY_inverse = 1.0/sumY;
			// 出力
			double length = (maxX-minX)*ticks_inverse;
			double currentPos = minX;
			if(boxes) currentPos += length*0.5;
			for(int i=0;i<frequency.length;i++){
				if(frequency[i]>0||boxes){
					if(outputFilePath.length()>0) pw.println(currentPos + "\t" + frequency[i]*sumY_inverse);
					System.out.println(currentPos + "\t" + frequency[i]*sumY_inverse);
				}
				currentPos += length;
			}
		}else{
			// 出力
			double length = (maxX-minX)*ticks_inverse;
			double currentPos = minX;
			if(boxes) currentPos += length*0.5;
			for(int i=0;i<frequency.length;i++){
				if(frequency[i]>0||boxes){
					if(outputFilePath.length()>0) pw.println(currentPos + "\t" + frequency[i]);
					System.out.println(currentPos + "\t" + frequency[i]);
				}
				currentPos += length;
			}
		}

		if(outputFilePath.length()>0) pw.close();
	}

	public void arrayList_double_plot(ArrayList<Double> input_list, int ticks, boolean boxes, boolean y_normalize,String outputFilePath) throws Exception{
		double[] list = new double[input_list.size()];
		for(int i=0;i<input_list.size();i++) {
			list[i] = input_list.get(i);
		}

		load(list);
		double_plot(ticks, boxes, y_normalize, outputFilePath);
	}

	public int[][] returnIntFrequency(int minX, int maxX){
		// 区間数は(maxX-minX)+1個
		int ticks = (maxX-minX)+1;

		// 度数をカウント
		int[][] fr = new int[ticks][2];
		for(int i=0;i<fr.length;i++) {
			fr[i][0] = minX+i;
			fr[i][1] = 0;
		}
		for(int i=0;i<int_list.length;i++){
			fr[int_list[i][1]-minX][1]++;
		}

		return fr;
	}

	public int[][] returnIntFrequency(){
		// 横軸の最小値,最高値を取得
		int minX = Integer.MAX_VALUE;
		int maxX = int_list[0][1];
		for(int i=0;i<int_list.length;i++){
			if(minX>int_list[i][1]){
				minX=int_list[i][1];
			}
			if(maxX<int_list[i][1]){
				maxX=int_list[i][1];
			}
		}

		return returnIntFrequency(maxX, minX);
	}

	public void plot_BoxesStyle(int[][] int_frequency, String outputFilePath) throws Exception{
		// 出力ファイル設定
		PrintWriter pw = null;
		if(outputFilePath.length()>0){
			pw = new PrintWriter(new File(outputFilePath));
		}


		int minX = int_frequency[0][0];
		double length = 1.0;
		double currentPos = minX + length*0.5;
		for(int i=0;i<int_frequency.length;i++){
			if(outputFilePath.length()>0) pw.println(currentPos + "\t" + int_frequency[i][1]);
			System.out.println(currentPos + "\t" + int_frequency[i][1]);
			currentPos += length;
		}

		if(outputFilePath.length()>0) pw.close();

	}

	public void plot_BoxesStyle(int[][] int_frequency, String outputFilePath, boolean y_normalize) throws Exception{
		// 出力ファイル設定
		PrintWriter pw = null;
		if(outputFilePath.length()>0){
			pw = new PrintWriter(new File(outputFilePath));
		}

		// 縦軸の合計値を計算
		int sumY = 0;
		for(int i=0;i<int_frequency.length;i++) {
			sumY += int_frequency[1][i];
		}
		double sumY_inverse = 1.0/sumY;

		int minX = int_frequency[0][0];
		double length = 1.0;
		double currentPos = minX + length*0.5;
		if(y_normalize) {
			for(int i=0;i<int_frequency.length;i++){
				if(outputFilePath.length()>0) pw.println(currentPos + "\t" + int_frequency[i][1]*sumY_inverse);
				System.out.println(currentPos + "\t" + int_frequency[i][1]*sumY_inverse);
				currentPos += length;
			}
		}else {
			for(int i=0;i<int_frequency.length;i++){
				if(outputFilePath.length()>0) pw.println(currentPos + "\t" + int_frequency[i][1]);
				System.out.println(currentPos + "\t" + int_frequency[i][1]);
				currentPos += length;
			}
		}


		if(outputFilePath.length()>0) pw.close();

	}

	public void plot_BoxesStyle(int[][] int_frequency, int ticks, String outputFilePath) throws Exception{
		// 出力ファイル設定
		PrintWriter pw = null;
		if(outputFilePath.length()>0){
			pw = new PrintWriter(new File(outputFilePath));
		}


		int minX = int_frequency[0][0];
		double length = ((double)int_frequency.length)/ticks;
		double currentPos = minX + length*0.5;
		double currentLineNumber = 0.0;
		double nextLineNumber;
		int testAllSum = 0;	//debag
		int sumFrequency;
		for(int i=0;i<ticks-1;i++){
			nextLineNumber = currentLineNumber + length;

			sumFrequency = 0;
			for(int j=(int)currentLineNumber ; j<(int)nextLineNumber ; j++) {
				sumFrequency += int_frequency[j][1];
			}

			if(outputFilePath.length()>0) pw.println(currentPos + "\t" + sumFrequency);
			System.out.println(currentPos + "\t" + sumFrequency);
			currentPos += length;
			currentLineNumber = nextLineNumber;

			testAllSum += sumFrequency; //debag
		}

		sumFrequency = 0;
		for(int j=(int)currentLineNumber ; j<int_frequency.length ; j++) {
			sumFrequency += int_frequency[j][1];
		}
		if(outputFilePath.length()>0) pw.println(currentPos + "\t" + sumFrequency);
		System.out.println(currentPos + "\t" + sumFrequency);
		testAllSum += sumFrequency; //debag

		// debag start
		int testSum = 0;
		for(int i=0;i<int_frequency.length;i++) {
			testSum += int_frequency[i][1];
		}
		if(testSum != testAllSum)System.out.println("******************error***********************");
		// debag end

		if(outputFilePath.length()>0) pw.close();
	}

	public double[][] logclasePlot(double[] input_list,int ticks, double maxValue){
		double[][] logscalse_frequency = new double[ticks][2];
		double minValue = Double.MAX_VALUE;
		for(int i=0;i<input_list.length;i++) {
			if(minValue>input_list[i] && input_list[i]>0) minValue=input_list[i];
		}

		double lengthRate = Math.pow(maxValue/minValue, 1.0/ticks);
		double currentX = minValue*Math.sqrt(lengthRate);
		for(int i=0;i<ticks;i++) {
			currentX *= lengthRate;
			logscalse_frequency[i][0] = currentX;
			logscalse_frequency[i][1] = 0;
		}

		int currentTick;
		for(int i=0;i<input_list.length;i++) {
			currentX = minValue*lengthRate;
			currentTick = 0;
			while(input_list[i] > currentX) {
				currentX *= lengthRate;
				currentTick++;
			}
			if(currentTick>=ticks) {
				System.out.println("error" + currentTick);
				currentTick = ticks-1;
			}
			logscalse_frequency[currentTick][1]++;
		}
		return logscalse_frequency;
	}

	public double[][] logclasePlot(double[] input_list,int ticks){
		double maxValue = Double.MIN_VALUE;
		for(int i=0;i<input_list.length;i++) {
			if(maxValue < input_list[i]) maxValue=input_list[i];
		}
		return logclasePlot(input_list, ticks, maxValue);
	}

}
