

public class BarabasiAlbertModel {
	public static void main(String[] args) {
		// 初期頂点数
		int m0 = 10;
		// 目標頂点数
		int N=100;
		// 各頂点の次数
		int[] deg = new int[N];
		
		// ①
		// m0に従って完全ネットワークを構築
		for(int i=0; i<m0 ; i++){
			// ついでに次数も初期化
			deg[i]=m0 - 1;
			
			System.out.print(i + "\t");
			for(int j=i+1 ; j<m0 ; j++){
				System.out.print(j + " ");
			}
			System.out.println();
		}
		int degSum = m0*(m0-1);
		
		// ③
		// 目標の頂点数までループ
		for(int k=m0 ; k<100 ; k++){
			// ②
			// 頂点を追加しそれが持つ辺をつないでいく
			int m = (int)(10*Math.random()+1);
			double[] p = new double[m0];
			double totalP=0;
			for(int i=0 ; i<m0 ; i++){
				p[i] = (double)deg[i]/degSum + totalP;
				totalP = p[i];
			}
			int targetNode=0;
			boolean loop;
			int[] targetList = new int[m];
			System.out.print(m0 + "\t");
			for(int i=0 ; i<m ; i++){
				do{
					targetNode=0;
					double x = Math.random();
					while(x > p[targetNode]){
						targetNode++;
					}
					loop=false;
					for(int j=0 ; j<i ; j++){
						if(targetList[j]==targetNode)
							loop=true;
					}
				}while(loop);

				deg[targetNode]++;
				targetList[i]=targetNode;
				System.out.print(targetNode + " ");
			}
			deg[m0]=m;
			System.out.println();
			
			m0++;
			degSum+=2*m;
		}	
	}
}
