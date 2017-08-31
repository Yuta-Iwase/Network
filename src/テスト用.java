import java.util.ArrayList;

public class テスト用{
	int[] a ;


	public static void main(String[] args) throws Exception{
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(123);
		list.add(-201);
		
		System.out.println(list.contains(123));
		System.out.println(list.contains(-201));
		System.out.println(list.contains(1234));


	}
}
