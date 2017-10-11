import java.math.BigDecimal;

public class テスト用{
	static int[] a ;


	public static void main(String[] args) throws Exception{
		
		BigDecimal b = new BigDecimal("10");
		double d = 10.0;
		
		for(int i=0;i<100;i++) {
			System.out.println(d +"\t" + String.format("%.1f", d) + "\t" + b.toString() + "\t" + b.toPlainString());
			d += 0.1;
			b = b.add(new BigDecimal(0.1));
		}
		
	}
}
