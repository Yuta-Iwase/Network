public class テスト用{
	public static void main(String[] args) throws Exception{
		double gamma = 2.7;
		int m0 = 2;
		int N = 1000;
		int bins = 50;

		double a = m0 * (gamma-3.0);

		Network net = new DMSNetwork(N, m0, a, 100);
		net.setNeightbor();
		net.SetWeight_to_Alpha(1.0);
//		System.out.println(net.weight.length);
//		System.out.println(net.neightborList.length);
//		System.out.println(net.addressList.length);

//		double sum_
		double max_str = 0.0;
		double min_str = Double.MAX_VALUE;
		double[] str_list = new double[N];
		int[] str_feq = new int[N*N];
		double INV_N = 1.0/net.N;

		for(int v=0;v<net.N;v++) {
			double current_str = 0.0;
			for(int e=0;e<net.degree[v];e++) {
				current_str += net.weight[net.neightborIndexList[net.addressList[v]+e]];
			}
			str_list[v] = current_str;
			if(max_str<current_str) max_str=current_str;
			if(min_str>current_str) min_str=current_str;
		}

		double xRange = max_str-min_str;
		double bin_width = xRange/bins;

		for(int i=0;i<net.N;i++) {
			double current_str = str_list[i];
			int current_binIndex = 0;
			while(current_binIndex<bins) {
				if(current_str<bin_width*(current_binIndex+1)) {
					break;
				}
				current_binIndex++;
			}
			if(current_binIndex >= bins) current_binIndex=bins-1;
			str_feq[current_binIndex]++;
		}

		for(int i=0;i<bins;i++) {
			if(str_feq[i]>0) {
				System.out.println(bin_width*i + "\t" + str_feq[i]*INV_N);
			}

		}
	}

}
