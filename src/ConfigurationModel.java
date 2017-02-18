

public class ConfigurationModel {
	public static void main(String[] args) {
		// 作りたいネットワークの頂点数
		int N=50;
		// 各頂点の次数
		int[] deg = new int[N];
		// 各頂点の未結合次数
		int[] remainder = new int[N];
				
		// ①
		// 次数を1～10の範囲で数字を割り当てる
		// (deg[N-1]のみ11も有りうる)
		// 未結合次数は次数と等しいとして初期化
		int degSum=0;
		for(int i=0 ; i<N ; i++){
			deg[i] = (int)(10*Math.random()+1);
			degSum += deg[i];
			if(i==N-1 && degSum%2==1) {
				deg[i]++;
				degSum++;
			}
			remainder[i] = deg[i];
		}
		
		// 隣接リスト
		int[][] list = new int[2][degSum/2];
		// ループでの隣接リストの現在の列
		int col = 0;
		
		// ⑤
		// 未結合次数が全て0になるまでループ
		do{
			// ②
			// 未結合次数が最大の頂点を見つけ
			// それをAとする
			int A=0;
			for(int i=0 ; i<N ; i++){
				if(remainder[i] > remainder[A]) A=i;
			}
			
			// ③(※多重辺andセルフループを防ぐ)
			// Aと繋がる確率(閾値)を各頂点で設定する
			double p[] = new double[N];
			int sumRemainder=0;
			for(int i=0 ; i<N ; i++) sumRemainder+=remainder[i];
			double totalP=0;
			for(int i=0 ; i<N ; i++){
				p[i] = (double)remainder[i]/sumRemainder + totalP;
				totalP = p[i];
			}

			
			// ループを出る条件(多重辺andセルフループを作らない)
			boolean BreakPoint;
			int B;
			do{
				// p[i]に従ってAに繋がる頂点を選び
				// それをBとする
				B=0;
				double x = Math.random();
				while(true){
					if(p[B] > x)break;
					B++;
				}
				
				BreakPoint=true;
				for(int c=0 ; c<col ; c++){
					// 多重辺orセルフループになるならbはtrueとなる
					boolean b = (
							(list[0][c]==A && list[1][c]==B) ||
							(list[0][c]==B && list[1][c]==A) ||
							(A==B)
							);
					if(b==true) BreakPoint=false;
				}
			}while(!BreakPoint);
			
			
			// ④
			// AとBを結合(2頂点をプロット)
			// その後A,Bの未接合次数を1減らす
			System.out.println(A + "\t" + B);
			list[0][col]=A;
			list[1][col]=B;
			col++;
			remainder[A]--;
			remainder[B]--;
			degSum -= 2;
		}while(degSum!=0);
		
	}
}
