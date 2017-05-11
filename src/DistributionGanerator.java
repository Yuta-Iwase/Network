
public class DistributionGanerator {
	public int[] ganeratePowerLaw(int N, double gamma,int minDegree, int maxDegree){
		// return用変数
		int[] degree= new int[N];
		
		// 離散量の確率分布を定義
		double[] p = new double[N];
		double sum = 0.0;
		for(int i=minDegree;i<maxDegree;i++){
			p[i] = Math.pow(i, -gamma);
			sum += p[i];
		}
		// 規格化
		for(int i=minDegree;i<maxDegree;i++){
			p[i] /= sum;
		}

		// p[i]の累積分布c[i]を定義
		double[] c = new double[N];
		c[minDegree] = p[minDegree];
		for(int i=minDegree+1 ; i<maxDegree ; i++){
			c[i] = c[i-1] + p[i];
		}

		// c[i]に従い次数列degree[i]生成
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
		
		return degree;
	}
	
	public int[] ganeratePowerLaw(int N, double gamma){
		// min,maxの入力を省略した場合、自動的に2,N-1となる。
		int minDegree = 2;
		int maxDegree = N-1;
		return ganeratePowerLaw(N, gamma, minDegree, maxDegree);
	}
	
}
