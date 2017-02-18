

public class RegularNetwork {
	public static void main(String[] args) {
		// 作りたいネットワークの頂点数
		int N=100;
		// 頂点の次数/2
		int k=5;
		
		
		for(int i=0 ; i<N ; i++){
			System.out.print(i + "\t");
			for(int j=1 ; j<=k ; j++){
				int target;
				if(i+j >= N) target= i+j - N;
				else target=i+j;
				System.out.print(target + " ");
			}
			System.out.println();
		}
	}
}
