import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * aaaaasssss
 * @author yuta
 *
 */
public class GEXFStylePrinter {
	int N;
	int[][] list;
	boolean directed;
	String filePath;
	PrintWriter pw;

	/**
	 * 初期化時、ネットワークの基本情報を打ち込む
	 * @param N 頂点数
	 * @param list 辺リスト
	 * @param directed 有向グラフか？
	 * @param filePath 出力するファイル名(パス)
	 */
	public GEXFStylePrinter(int N, int[][] list, boolean directed, String filePath) throws FileNotFoundException{
		this.N = N;
		this.list = list;
		this.filePath = filePath;
		pw = new PrintWriter(new File(filePath));
	}

	/**
	 * 必ずこのメソッドを始めに呼び出してください。
	 */
	public void init_1st(){
		String s;

		s = "<gexf>";
		pw.println(s);
//		System.out.println(s);

		if(directed){
			s = "\t" + "<graph defaultedgetype=\"" + "directed" + "\">";
		}else{
			s = "\t" + "<graph defaultedgetype=\"" + "undirected" + "\">";
		}
		pw.println(s);
//		System.out.println(s);
	}

	/**
	 * 2番めに呼ぶメソッド<br>
	 * 頂点についての情報を書き込む。<br>
	 * 設定しない引数には、nullまたは空リストや空文字列を与えてください。
	 * @param label 頂点名のリスト
	 * @param attributeName オリジナルの特徴量の名前
	 * @param attribute オリジナルの特徴量の値のリスト
	 */
	public void printNode_2nd(String[] label, String attributeName, double[] attribute){
		boolean use_attribute = true;
		if(attribute==null){
			use_attribute = false;
		}else if(attribute.length==0){
			use_attribute = false;
		}

		boolean use_label = (label==null || label.length==0);

		String s;
		if(use_attribute){
			String attribute_type = "";
			if(attribute.getClass().getName().equals("[I") || attribute.getClass().getName().equals("[D")){
				attribute_type = "float";
			}else{
				attribute_type = "string";
			}

			// attributesの記述
			s = "\t" + "\t" + "<attributes class=\"node\">";
			pw.println(s);
	//		System.out.println(s);

			s = "\t" + "\t" + "\t" + "<attribute id=\"0\" title=\"" + attributeName + "\" type=\"" + attribute_type + "\" />";
			pw.println(s);
	//		System.out.println(s);

			s = "\t" + "\t" + "</attributes>";
			pw.println(s);
	//		System.out.println(s);
		}

		s = "\t" + "\t" + "<nodes>";
		pw.println(s);
//		System.out.println(s);

		for(int i=0;i<N;i++){
			// 1/5の途中
			if(use_label){
				s = "\t" + "\t" + "\t" + "<node id=\"" + i + "\" label=\"" + i + "\"";
			}else{
				s = "\t" + "\t" + "\t" + "<node id=\"" + i + "\" label=\"" + label[i] + "\"";
			}
			pw.print(s);
//			System.out.print(s);

			if(use_attribute){
				// 1/5の終わり
				s = ">";
				pw.println(s);
		//		System.out.println(s);

				// 2/5
				s = "\t" + "\t" + "\t" + "\t" + "<attvalues>";
				pw.println(s);
		//		System.out.println(s);

				// 3/5
				s = "\t" + "\t" + "\t" + "\t" + "\t" + "<attvalue for=\"0\" value=\"" + attribute[i] + "\" />";
				pw.println(s);
		//		System.out.println(s);

				// 4/5
				s = "\t" + "\t" + "\t" + "\t" + "</attvalues>";
				pw.println(s);
		//		System.out.println(s);

				// 5/5
				s = "\t" + "\t" + "\t" + "</node>";
				pw.println(s);
		//		System.out.println(s);

			}else{
				// 1/5の終わり
				s = " />";
				pw.println(s);
		//		System.out.println(s);
			}
		}

		s = "\t" + "\t" + "</nodes>";
		pw.println(s);
//		System.out.println(s);
	}

	/**
	 * 2番めに呼ぶメソッド<br>
	 * 頂点についての情報を書き込む。<br>
	 * 設定しない引数には、nullまたは空リストや空文字列を与えてください。
	 * @param label 頂点名のリスト
	 * @param attributeName オリジナルの特徴量の名前
	 * @param attribute オリジナルの特徴量の値のリスト
	 */
	public void printNode_2nd(String[] label, String attributeName, int[] attribute){
		double[] attribute_int = new double[attribute.length];
		for(int i=0;i<attribute.length;i++){
			attribute_int[i] = attribute[i];
		}
		printNode_2nd(label, attributeName, attribute_int);
	}

	/**
	 * 2ndメソッドで何も引数を与えなくて良い場合はこれを使ってください。
	 */
	public void printNode_2nd() {
		printNode_2nd(null, null, new int[]{});
	}

	/**
	 * 3番めに呼ぶメソッド<br>
	 * 辺についての情報を書き込む。<br>
	 * 設定しない引数には、nullまたは空リストや空文字列を与えてください。
	 * @param weight 辺の重みのリスト
	 * @param attributeName オリジナルの特徴量の名前
	 * @param attribute オリジナルの特徴量の値のリスト
	 */
	public void printEdge_3rd(double[] weight, String attributeName, double[] attribute){
		boolean use_weight = true;
		if(weight==null){
			use_weight = false;
		}else if(weight.length==0){
			use_weight = false;
		}

		boolean use_attribute = true;
		if(attribute==null){
			use_attribute = false;
		}else if(attribute.length==0){
			use_attribute = false;
		}

		String s;
		if(use_attribute){
			String attribute_type = "";
			if(attribute.getClass().getName().equals("[I") || attribute.getClass().getName().equals("[D")){
				attribute_type = "float";
			}else{
				attribute_type = "string";
			}

			// attributesの記述
			s = "\t" + "\t" + "<attributes class=\"edge\">";
			pw.println(s);
	//		System.out.println(s);

			s = "\t" + "\t" + "\t" + "<attribute id=\"0\" title=\"" + attributeName + "\" type=\"" + attribute_type + "\" />";
			pw.println(s);
	//		System.out.println(s);

			s = "\t" + "\t" + "</attributes>";
			pw.println(s);
	//		System.out.println(s);
		}

		s = "\t" + "\t" + "<edges>";
		pw.println(s);
//		System.out.println(s);

		for(int i=0;i<list.length;i++){
			// 1/5の途中
			if(use_weight){
				s = "\t" + "\t" + "\t" + "<edge id=\"" + i + "\" source=\"" + list[i][0] + "\" target=\"" + list[i][1] + "\" weight=\"" + weight[i] + "\"";
			}else{
				s = "\t" + "\t" + "\t" + "<edge id=\"" + i + "\" source=\"" + list[i][0] + "\" target=\"" + list[i][1] + "\"";
			}
			pw.print(s);
//			System.out.print(s);

			if(use_attribute){
				// 1/5の終わり
				s = ">";
				pw.println(s);
		//		System.out.println(s);

				// 2/5
				s = "\t" + "\t" + "\t" + "\t" + "<attvalues>";
				pw.println(s);
		//		System.out.println(s);

				// 3/5
				s = "\t" + "\t" + "\t" + "\t" + "\t" + "<attvalue for=\"0\" value=\"" + attribute[i] + "\" />";
				pw.println(s);
		//		System.out.println(s);

				// 4/5
				s = "\t" + "\t" + "\t" + "\t" + "</attvalues>";
				pw.println(s);
		//		System.out.println(s);

				// 5/5
				s = "\t" + "\t" + "\t" + "</edge>";
				pw.println(s);
		//		System.out.println(s);

			}else{
				// 1/5の終わり
				s = " />";
				pw.println(s);
		//		System.out.println(s);
			}
		}

		s = "\t" + "\t" + "</edges>";
		pw.println(s);
//		System.out.println(s);
	}

	/**
	 * 3番めに呼ぶメソッド<br>
	 * 辺についての情報を書き込む。<br>
	 * 設定しない引数には、nullまたは空リストや空文字列を与えてください。
	 * @param weight 辺の重みのリスト
	 * @param attributeName オリジナルの特徴量の名前
	 * @param attribute オリジナルの特徴量の値のリスト
	 */
	public void printEdge_3rd(double[] weight, String  attributeName, int[] attribute){
		double[] attribute_int = new double[attribute.length];
		for(int i=0;i<attribute.length;i++){
			attribute_int[i] = attribute[i];
		}
		printEdge_3rd(weight, attributeName, attribute_int);
	}

	/**
	 * 3rdメソッドで何も引数を与えなくて良い場合はこれを使ってください。
	 */
	public void printEdge_3rd(){
		printEdge_3rd(null, null, new int[]{});
	}

	/**
	 * 終了時にこのメソッドを必ず呼び出してください。<br>
	 */
	public void terminal_4th(){
		String s;

		s = "\t" + "</graph>";
		pw.println(s);
//		System.out.println(s);


		s = "</gexf>";
		pw.println(s);
//		System.out.println(s);

		pw.close();
	}

}
