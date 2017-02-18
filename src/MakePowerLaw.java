
public class MakePowerLaw {

	public static void main(String[] args) {
		// 作りたいネットワークの設定
		int N = 1000;
		double gamma = 2.5;
		int minDegree = 2;
		int maxDegree = N-1;


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
		int[] degree = new int[N];
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

		// 完成した次数列degree[i]を出力
		for(int i=0;i<N;i++){
			System.out.println(i + "\t" + degree[i]);
		}

	}
}
