import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;

public class conbine {

	public static void main(String[] args) throws Exception{
		File file = new File("conbine.txt");
		if(file.exists()) {
			try {
				PrintWriter pw = new PrintWriter(new File("conbineResult.txt"));

				Scanner scan = new Scanner(file);
				double min = Double.parseDouble(scan.nextLine());
				double max = Double.parseDouble(scan.nextLine());
				double tics = Double.parseDouble(scan.nextLine());

				int times = (int)Math.round((max-min)/tics) + 1;

				for(int t=0;t<times;t++) {
//					double alpha = min + tics*t;
					BigDecimal alpha = new BigDecimal(min + tics*t);
					String alphaString = String.format("%.1f", Double.parseDouble(alpha.toPlainString()));

					File text = new File("biasedRW_alpha=" + alphaString + "/averageHS_alpha" + alphaString + ".txt");
					Scanner scan2 = new Scanner(text);
					pw.println(scan2.nextLine());

					scan2.close();
				}

				pw.close();
				scan.close();
			}catch(Exception e) {
				PrintWriter pw = new PrintWriter(new File("error.txt"));
				pw.println(e);
				pw.close();
			}

		}

	}

}
