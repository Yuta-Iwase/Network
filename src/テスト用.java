import java.math.BigDecimal;

public class テスト用{
	static int[] a ;


	public static void main(String[] args) throws Exception{
<<<<<<< HEAD
		
		BigDecimal b = new BigDecimal("10");
		double d = 10.0;
		
		for(int i=0;i<100;i++) {
			System.out.println(d +"\t" + String.format("%.1f", d) + "\t" + b.toString() + "\t" + b.toPlainString());
			d += 0.1;
			b = b.add(new BigDecimal(0.1));
		}
		
=======

		double min=-2.5;
		double width = 0.1;
		double alpha;

		String folderName;
		String textName;
		File folder = null;
		File text = null;
		Scanner scan = null;

		PrintWriter pw = new PrintWriter(new File("averageHS.txt"));

		int currentIndex=0;
		do {
			alpha = min + width*(currentIndex++);
			folderName = "biasedRW_alpha=" + String.format("%.1f", alpha);
			folder = new File(folderName);
			if(folder.exists()) {
				textName = "averageHS_alpha" + String.format("%.1f", alpha) + ".txt";
				text = new File(folderName + "/" + textName);
				scan = new Scanner(text);
				pw.println(scan.nextLine());
				scan.close();
			}
		}while(folder.exists());


		pw.close();
>>>>>>> refs/remotes/origin/master
	}
}
