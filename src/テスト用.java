import java.io.File;
import java.io.PrintWriter;

public class テスト用 {
	int[] a ;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		PrintWriter pw = new PrintWriter(new File("aaa.txt"));
		pw.println("abc");
		pw.close();
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
