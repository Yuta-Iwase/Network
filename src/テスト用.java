import java.io.File;
import java.io.PrintWriter;

public class テスト用 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		PrintWriter pw = new PrintWriter(new File("てすとてすと.txt"));
		pw.println("aaaaa");

		pw.close();
	}

}
