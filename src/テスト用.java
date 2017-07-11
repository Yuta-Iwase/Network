public class テスト用 {
	int[] a ;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		int N = 100;
		double deltaW = Double.MAX_VALUE;

		// 生成
		MakePowerLaw dist;
		ConfigrationNetwork net;
		do{
			dist = new MakePowerLaw(N, 2.7, 2, N-1);
			net = new ConfigrationNetwork(dist.degree, 50);
		}while(!net.success);
		System.out.println("生成完了");


		net.setNode(false);
		net.setEdge();
		net.ReinforcedRandomWalk(10000, 3.0);
		net.LinkSalience();

		double[] linkSalience = new double[net.M];
		for(int i=0;i<net.M;i++){
			linkSalience[i] = net.edgeList.get(i).linkSalience;
		}

		GEXFStylePrinter pr = new GEXFStylePrinter(net.N,net.list,net.directed,"GEXF_test.gexf");
		pr.init_1st();
		pr.printNode_2nd(null, null, new double[0]);
		pr.printEdge_3rd(net.weight, "LinkSalience", linkSalience);
		pr.terminal_4th();

		System.out.println();
		for(int i=0;i<net.M;i++){
			System.out.println(i + "\t" + net.weight[i]);
		}
	}

	static void method(Object o){
		if(o.toString().length() == 0)System.out.println("null");
		else System.out.println("テスト:"  + o);

		if(o.equals(Integer.class)){
			System.out.println(o + "is Int");
		}
	}

	static String objectLoader(Object o){
		String s = o.getClass().getName();
		System.out.println(s);
		return s;
	}

}
