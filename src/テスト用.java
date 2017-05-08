import java.util.ArrayList;

public class テスト用 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		ArrayList<String> x = new ArrayList<String>();
		
		for(int i=0;i<10;i++) x.add(Character.toString(((char)('a'+i))));
		x.add(0, "あああ");
		
		for(int i=0;i<x.size();i++){
			System.out.println(x.get(i));
		}
		
	}

}
