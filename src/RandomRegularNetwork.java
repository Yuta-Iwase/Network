
public class RandomRegularNetwork extends ConfigrationNetwork{

	public RandomRegularNetwork(int N, int degree, int loopLimit) {
		super(returnDegreeList(N, degree), loopLimit);
	}

	private static int[] returnDegreeList(int input_N, int input_degree) {
		int[] degree = new int[input_N];
		for(int i=0;i<degree.length;i++) {
			degree[i] = input_degree;
		}
		return degree;
	}

}
