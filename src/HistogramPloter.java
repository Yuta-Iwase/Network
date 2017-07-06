import java.io.File;
import java.io.PrintWriter;

// 読み
// int[] int[][] double[] double[][] "FileName"
// 出力
// plot(autorato,normalize,刻み数,(ファイル名))

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

	public void int_plot(boolean normalize,String outputFilePath)throws Exception{
		// 出力ファイル設定
		PrintWriter pw = null;
		if(outputFilePath.length()>0){
			pw = new PrintWriter(new File(outputFilePath));
		}

		// 横軸の最高値を取得
		int maxX = int_list[0][1];
		for(int i=0;i<int_list.length;i++){
			if(maxX<int_list[i][1]){
				maxX=int_list[i][1];
			}
		}

		// 区間数はmaxX+1個
		int ticks = maxX+1;
		double ticks_inverse = 1.0/ticks;

		// 度数をカウント
		frequency = new int[ticks];
		for(int i=0;i<=maxX;i++){
			frequency[int_list[i][1]]++;
		}

		if(normalize){
			// 縦軸の最大値を取得
			int maxY = 0;
			for(int i=0;i<=maxX;i++){
				if(maxY<frequency[i])maxY=frequency[i];
			}
			double maxY_inverse = 1.0/maxY;
			// 出力
			double length = ticks_inverse;
			double currentPos = length*0.5;
			for(int i=0;i<int_list.length;i++){
				if(outputFilePath.length()>0) pw.println(currentPos + "\t" + frequency[i]*maxY_inverse);
				System.out.println(currentPos + "\t" + frequency[i]*maxY_inverse);
				currentPos += length;
			}
		}else{
			// 出力
			double length = 1.0;
			double currentPos = length*0.5;
			for(int i=0;i<int_list.length;i++){
				if(outputFilePath.length()>0) pw.println(currentPos + "\t" + frequency[i]);
				System.out.println(currentPos + "\t" + frequency[i]);
				currentPos += length;
			}
		}

		pw.close();
	}

	public void double_plot(int ticks,boolean normalize,String outputFilePath)throws Exception{
		// 出力ファイル設定
		PrintWriter pw = null;
		if(outputFilePath.length()>0){
			pw = new PrintWriter(new File(outputFilePath));
		}

		// 横軸の最高値を取得
		double maxX = double_list[0][1];
		for(int i=0;i<double_list.length;i++){
			if(maxX<double_list[i][1]){
				maxX=double_list[i][1];
			}
		}
		double maxX_inverse = 1.0/maxX;

		// 区間数の逆数
		double ticks_inverse = 1.0/ticks;

		// 度数をカウント
		frequency = new int[ticks];
		for(int i=0;i<ticks;i++){
			frequency[(int)(double_list[i][1]*maxX_inverse*ticks)]++;
		}

		if(normalize){
			// 縦軸の最大値を取得
			int maxY = 0;
			for(int i=0;i<=maxX;i++){
				if(maxY<frequency[i])maxY=frequency[i];
			}
			double maxY_inverse = 1.0/maxY;
			// 出力
			double length = ticks_inverse;
			double currentPos = length*0.5;
			for(int i=0;i<int_list.length;i++){
				if(outputFilePath.length()>0) pw.println(currentPos + "\t" + frequency[i]*maxY_inverse);
				System.out.println(currentPos + "\t" + frequency[i]*maxY_inverse);
				currentPos += length;
			}
		}else{
			// 出力
			double length = 1.0;
			double currentPos = length*0.5;
			for(int i=0;i<int_list.length;i++){
				if(outputFilePath.length()>0) pw.println(currentPos + "\t" + frequency[i]);
				System.out.println(currentPos + "\t" + frequency[i]);
				currentPos += length;
			}
		}

		pw.close();
	}
}
