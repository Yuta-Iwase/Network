public class テスト用{
	public static void main(String[] args) throws Exception{

		double[] sizeDistribution = new double[123];


		double averageSize = 0.0;
		for(int i=0;i<sizeDistribution.length;i++) {
			averageSize += i*sizeDistribution[i];
		}




		System.out.println(averageSize);

	}
}
