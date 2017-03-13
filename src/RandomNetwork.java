import java.util.ArrayList;

public class RandomNetwork extends Network{
	public RandomNetwork(int input_N,double input_p) {
		N = input_N;
		double p = input_p;
		
		ArrayList<Integer> left = new ArrayList<Integer>();
		ArrayList<Integer> right = new ArrayList<Integer>();
		degree = new int[N];
		for(int i=0;i<N;i++) degree[i]=0;
		M = 0;
		for(int i=0;i<N;i++){
			for(int j=i+1 ;j<N ; j++){
				if(Math.random() < p){
					left.add(i);	right.add(j);
					degree[i]++;	degree[j]++;
					M++;
				}
			}
		}
		
		list = new int[M][2];
		for(int i=0;i<M;i++){
			list[i][0] = left.get(i);
			list[i][1] = right.get(i);
		}
		
	}

}
