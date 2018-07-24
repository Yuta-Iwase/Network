import java.util.ArrayList;

public class テスト用{
	public static void main(String[] args) throws Exception{
		ArrayList<A> l = new ArrayList<>();
		l.add(new A(10));
//		System.out.println(a);
		A[] a;
		try {
			a = (A[])l.toArray();
		}catch(Exception e) {
			System.out.println(e);
		};
		
//		System.out.println(a[0]);
	}
	
	static class A{
		int x ;
		public A(int aa) {
			x = aa;
			// TODO 自動生成されたコンストラクター・スタブ
		}
	}
	
}
