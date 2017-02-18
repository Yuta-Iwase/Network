
// 失敗作
public class 有相関実験 {
	public static void main(String[] args) {
		int 頂点数=100;
		int[] 次数 = new int[頂点数];
		int sum=0;
		for(int i=0;i<100;i++){
			double xx = Math.random();
			int degRand=-1;
			if(xx<0.6)degRand=1;
			if(0.60<=xx && xx<0.75)degRand=2;
			if(0.75<=xx && xx<0.95)degRand=3;
			if(0.80<=xx && xx<0.90)degRand=4;
			if(0.90<=xx && xx<0.95)degRand=5;
			if(0.95<=xx && xx<1.00)degRand=5;
			次数[i] = degRand;
			sum += 次数[i];
		}
		if(sum%2==1) {
			次数[頂点数-1]++;
			sum++;
		}
		int list[][] = new int[sum/2][2];
		
		int[] 未接続次数 = 次数.clone();
		int a,b;
		double x;
		double[] 確率 = new double[5];
		int targetD;
		int nowLine=0;
		int lostLine=0;
		do{
			a=max(未接続次数);
			
			x = Math.random();
			if(次数[a] == 1){
				確率[0]=0;
				確率[1]=0;
				確率[2]=0.50;
				確率[3]=0.30+確率[2];
				確率[4]=0.20+確率[3];
			}
			if(次数[a] == 2){
				確率[0]=0;
				確率[1]=0.15;
				確率[2]=0.50+確率[1];
				確率[3]=0.15+確率[2];
				確率[4]=0.20+確率[3];
			}
			if(3<=次数[a] && 次数[a]<=4){
				確率[0]=0.10;
				確率[1]=0.15+確率[0];
				確率[2]=0.50+確率[1];
				確率[3]=0.15+確率[2];
				確率[4]=0.10+確率[3];
			}
			if(次数[a]==5){
				確率[0]=0.20;
				確率[1]=0.15+確率[0];
				確率[2]=0.50+確率[1];
				確率[3]=0.15+確率[2];
				確率[4]=1000;
			}
			if(次数[a]==6){
				確率[0]=0.20;
				確率[1]=0.30+確率[0];
				確率[2]=0.50+確率[1];
				確率[3]=100;
				確率[4]=100;
			}
			targetD=-1;
			if(x<確率[0])targetD=次数[a]-2;
			else if(確率[0]<=x && x<確率[1])targetD=次数[a]-1;
			else if(確率[1]<=x && x<確率[2])targetD=次数[a];
			else if(確率[2]<=x && x<確率[3])targetD=次数[a]+1;
			else if(確率[3]<=x && x<確率[4])targetD=次数[a]+2;
			
			b=-1;
			for(int i=0;i<頂点数;i++){
				if(次数[i] == targetD && i!=a){
					b=i;
					break;
				}
			}
			int M,m;
			if(a<b){
				M=b;
				m=a;
			}else{
				m=b;
				M=a;
			}
			
			boolean 多重辺=false;
			for(int i=0;i<nowLine;i++){
				if(list[i][0]==m && list[i][1]==M)多重辺=true;
			}
			
			if(b>=0 && !多重辺){
				list[nowLine][0] = m;
				list[nowLine][1] = M;
				未接続次数[m]--;
				未接続次数[M]--;
				nowLine++;
				sum -= 2;
			}
			if(b>=0 && 多重辺){
				未接続次数[m]--;
				未接続次数[M]--;
				lostLine++;
				sum -= 2;
			}
			
		}while(sum!=0);
		
		for(int i=0;i<list.length-lostLine;i++){
			System.out.println(list[i][0] + "\t" + list[i][1]);
		}
	}
	static int max(int[] a){
		int m=0;
		for(int i=0;i<a.length;i++) m=Math.max(m, a[i]);
		return m;
	}
}
