

public class MakeDoublePoisson {
//	int[] degree;
//
//	int N;
//	double isolatedAverage,clusterAverage;
//	int minDegree,maxDegree;
//
//	private double[] c;
//
//	public MakeDoublePoisson(int input_N,double input_isolatedAverage, double input_clusterAverage,int input_minDegree,int input_maxDegree) {
//		N = input_N;
//		isolatedAverage = input_isolatedAverage;
//		clusterAverage = input_clusterAverage;
//		minDegree = input_minDegree;
//		maxDegree = input_maxDegree;
//
//		generate();
//	}
//
//
//	public MakeDoublePoisson(int input_N,double input_isolatedAverage, double input_clusterAverage) {
//		N = input_N;
//		isolatedAverage = input_isolatedAverage;
//		clusterAverage = input_clusterAverage;
//		minDegree = 1;
//		maxDegree = N-1;
//
//		generate();
//	}
//
//	public void generate(){
//		// 離散量の確率分布を定義
//		double[][] p = new double[maxDegree+1][maxDegree+1];
//		double sum = 0.0;
//		p[minDegree] = (Math.pow(isolatedAverage, minDegree)*Math.exp(-isolatedAverage))/factorial(minDegree)
//						*(Math.pow(clusterAverage, minDegree)*Math.exp(-clusterAverage))/factorial(minDegree);
//		for(int i=minDegree+1;i<=maxDegree;i++){
//			p[i] = p[i-1]*(isolatedAverage/i)*(clusterAverage/i);
//			sum += p[i];
//		}
//		for(int s=0;s<maxDegree;s++) {
//			p[s][0] = (Math.pow(isolatedAverage, minDegree)*Math.exp(-isolatedAverage))/factorial(minDegree);
//			for(int t=0;s+2*t<maxDegree;t++) {
//
//			}
//		}
//		// 規格化
//		for(int i=minDegree;i<=maxDegree;i++){
//			p[i] /= sum;
//		}
//
//		// p[i]の累積分布c[i]を定義
//		c = new double[maxDegree+1];
//		c[minDegree] = p[minDegree];
//		for(int i=minDegree+1 ; i<=maxDegree ; i++){
//			c[i] = c[i-1] + p[i];
//		}
//		c[maxDegree] = 1.0;
//
//		// c[i]に従い次数列degree[i]生成
//		degree = new int[N];
//		int currentIndex;
//		double r;
//		double nextLeft,nextRight;
//		for(int i=0;i<N;i++){
//			r = Math.random();
//			currentIndex = 0;
//			nextLeft = 0;
//			nextRight = c[minDegree];
//			while(!(nextLeft<=r && r<nextRight)){
//				nextLeft = c[minDegree+currentIndex];
//				nextRight = c[minDegree+currentIndex+1];
//				currentIndex++;
//			}
//			degree[i] = minDegree + currentIndex;
//		}
//
//	}
//
//	private int factorial(int n) {
//		int result = 1;
//		for(int i=1;i<=n;i++) {
//			result *= i;
//		}
//		return result;
//	}
//
//	// 完成した次数列degree[i]を出力
//	public void printList(){
//		for(int i=0;i<N;i++){
//			System.out.println(i + "\t" + degree[i]);
//		}
//	}
//
//	public double averageDegree() {
//		int sum = 0;
//		for(int i=0;i<degree.length;i++) {
//			sum += degree[i];
//		}
//		return (double)sum/degree.length;
//	}

}
