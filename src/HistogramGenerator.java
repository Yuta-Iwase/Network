import java.util.ArrayList;
import java.util.Arrays;

// ヒストグラム系クラス3代目

public class HistogramGenerator {

	private double[][] binPlot(double[] list,int bins,boolean logScale,double min,double max,boolean sorted) {
		// 度数、return用変数
		double[][] frequency = new double[bins][2];
		for(int i=0;i<bins;i++) frequency[i][1]=0;

		// ソートを実行していない場合、ソートする
		if(!sorted) Arrays.sort(list);

		// minが不正値の場合、修正
		if(logScale && min==0) {
			for(int i=0;i<list.length;i++) {
				if(list[i]>0) {
					min = list[i];
					break;
				}
			}
		}

		// 作業用変数定義
		int listLength = list.length;
		double increaseFactor;
		if(logScale) {
			increaseFactor = Math.pow(max/min, 1.0/bins);
		}else {
			increaseFactor = (max - min)/bins;
		}
		double currentMax;
		int currentListIndex = 0;
		boolean finish = false;



		// 最終bin以外の処理
		currentMax = min;
		masterLoop:for(int i=0;i<bins-1;i++){
			// 左端の定義
			if(logScale) currentMax *= increaseFactor;
			else currentMax += increaseFactor;
			// カウント
			while(list[currentListIndex]<currentMax) {
				frequency[i][1]++;
				currentListIndex++;
				if(currentListIndex>=listLength) {
					finish = true;
					break masterLoop;
				}
			}
		}

		// 最終binの処理
		if(!finish) {
			// 左端の定義
			if(logScale) currentMax = max;
			else currentMax = max;
			// カウント
			while(list[currentListIndex]<currentMax) {
				frequency[bins-1][1]++;
				currentListIndex++;
				if(currentListIndex>=listLength) {
					finish = true;
					break;
				}
			}
		}

		// frequencyの横軸を決定(各binの最小値を代表値とする)
		frequency[0][0] = min;
		for(int i=1;i<bins;i++) {
			if(logScale) frequency[i][0] = frequency[i-1][0]*increaseFactor;
			else frequency[i][0] = frequency[i-1][0]+increaseFactor;
		}

		return frequency;
	}


	public double[][] binPlot(double[] list,int bins,boolean logScale,double min,double max) {
		return binPlot(list, bins, logScale, min, max, false);
	}

	public double[][] binPlot(double[] list,int bins,boolean logScale) {
		Arrays.sort(list);
		return binPlot(list, bins, logScale, list[0], list[list.length-1], true);
	}

	public double[][] binPlot(int[] list,int bins,boolean logScale,double min,double max) {
		double[] doubleList = new double[list.length];
		for(int i=0;i<list.length;i++) doubleList[i]=list[i];
		return binPlot(doubleList, bins, logScale, min, max);
	}

	public double[][] binPlot(int[] list,int bins,boolean logScale) {
		double[] doubleList = new double[list.length];
		for(int i=0;i<list.length;i++) doubleList[i]=list[i];
		Arrays.sort(doubleList);
		return binPlot(doubleList, bins, logScale, doubleList[0], doubleList[doubleList.length-1]);
	}

	public double[][] binPlot(ArrayList<Double> arrayList, int bins, boolean logscale, double min, double max){
		double[] list = new double[arrayList.size()];
		for(int i=0;i<arrayList.size();i++) list[i]=arrayList.get(i);
		return binPlot(list, bins, logscale, min, max);
	}

	public double[][] binPlot(ArrayList<Double> arrayList, int bins, boolean logscale){
		double[] list = new double[arrayList.size()];
		for(int i=0;i<arrayList.size();i++) list[i]=arrayList.get(i);
		return binPlot(list, bins, logscale);
	}

	private int[][] nonBinPlot(int[] list,int min,int max,boolean sorted) {
		// bin数計算
		int bins = max-min+1;

		// 度数、return用変数
		int[][] frequency = new int[bins][2];
		for(int i=0;i<bins;i++) frequency[i][1]=0;

		// 作業用変数定義
		int listLength = list.length;
		int increaseFactor = 1;
		int currentMax = min;
		int currentListIndex = 0;

		// ソートを実行していない場合、ソートする
		if(!sorted) Arrays.sort(list);

		// カウント処理
		masterLoop:for(int i=0;i<bins;i++){
			// 左端の定義
			frequency[i][0] = currentMax;
			currentMax += increaseFactor;
			// カウント
			while(list[currentListIndex]<currentMax) {
				frequency[i][1]++;
				currentListIndex++;
				if(currentListIndex>=listLength) {
					break masterLoop;
				}
			}
		}

		return frequency;
	}


	public int[][] nonBinPlot(int[] list,int min,int max) {
		return nonBinPlot(list, min, max, false);
	}

	public int[][] nonBinPlot(int[] list) {
		Arrays.sort(list);
		return nonBinPlot(list, list[0], list[list.length-1], true);
	}

}
