// VertexReinforcedRandomwalk のテスト

public class RWTest_transition_03 {
	public static void main(String[] args) {
		int N=10000;
		double p = 0.0015;
		int stepN = N*1000;
		double deltaW = 1.0;
		RandomNetwork net = new RandomNetwork(N, p);
		
		net.setNode(false);
		net.setEdge();
		
		double[] vWeight;
		vWeight = net.VertexReinforcedRandomWalk(stepN, deltaW);
		
		int[] vWeightN = new int[N];
		for(int i=0;i<vWeight.length;i++){
			vWeightN[i] = (int)Math.round((vWeight[i]-1)/deltaW);
		}
		
		int[][] sortedVWeightN;
		Comprator2dim sorter = new Comprator2dim();
		sortedVWeightN = sorter.sort(vWeightN, 1, false);
		
		for(int i=0;i<vWeight.length;i++){
			System.out.println(sortedVWeightN[i][0] + "\t" + sortedVWeightN[i][1]);
		}
		
	}
}
