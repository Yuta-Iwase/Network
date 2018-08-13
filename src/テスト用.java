import java.math.BigDecimal;

public class テスト用{
	public static void main(String[] args) throws Exception{
		int bins = 20;
		BigDecimal bin_width = BigDecimal.ONE.divide(new BigDecimal(bins));
		
		System.out.println(bin_width.doubleValue());
		
	}

}
