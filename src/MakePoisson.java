

public class MakePoisson {
	int[] degree;

	int N;
	double average;
	int minDegree,maxDegree;

	private double[] c;

	public MakePoisson(int input_N,double input_average,int input_minDegree,int input_maxDegree) {
		N = input_N;
		average = input_average;
		minDegree = input_minDegree;
		maxDegree = input_maxDegree;

		generate();
	}


	public MakePoisson(int input_N,double input_average) {
		N = input_N;
		average = input_average;
		minDegree = 0;
		maxDegree = N-1;

		generate();
	}

	public void generate(){
		if(average>0) {
			// 離散量の確率分布を定義
			double[] p = new double[maxDegree+1];
			p[minDegree] = (Math.pow(average, minDegree)*Math.exp(-average))/factorial(minDegree);
			double sum = p[minDegree];
			for(int i=minDegree+1;i<=maxDegree;i++){
				p[i] = p[i-1]*(average/i);
				sum += p[i];
			}
			// 規格化
			for(int i=minDegree;i<=maxDegree;i++){
				p[i] /= sum;
			}

			// p[i]の累積分布c[i]を定義
			c = new double[maxDegree+1];
			c[minDegree] = p[minDegree];
			for(int i=minDegree+1 ; i<=maxDegree ; i++){
				c[i] = c[i-1] + p[i];
			}
			c[maxDegree] = 1.0;

			// c[i]に従い次数列degree[i]生成
			degree = new int[N];
			int currentIndex;
			double r;
			double nextLeft,nextRight;
			for(int i=0;i<N;i++){
				r = Math.random();
				currentIndex = 0;
				nextLeft = 0;
				nextRight = c[minDegree];
				while(!(nextLeft<=r && r<nextRight)){
					nextLeft = c[minDegree+currentIndex];
					nextRight = c[minDegree+currentIndex+1];
					currentIndex++;
				}
				degree[i] = minDegree + currentIndex;
			}
		}else {
			degree = new int[N];
			for(int i=0;i<N;i++){
				degree[i] = 0;
			}
		}


	}

	private int factorial(int n) {
		int result = 1;
		for(int i=1;i<=n;i++) {
			result *= i;
		}
		return result;
	}

	// 完成した次数列degree[i]を出力
	public void printList(){
		for(int i=0;i<N;i++){
			System.out.println(i + "\t" + degree[i]);
		}
	}

	public double averageDegree() {
		return average;
	}

}
