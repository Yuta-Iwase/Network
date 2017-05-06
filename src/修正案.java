import java.io.File;
import java.util.Scanner;

public class 修正案 {

	public static void main(String[] args) throws Exception{
		// mを求めるためだけに使う
		Scanner scan1 = new Scanner(new File("/Users/Owner/Desktop/x.txt"));
		// 数値を格納に使う
		Scanner scan2 = new Scanner(new File("/Users/Owner/Desktop/x.txt"));
//		// mを求めるためだけに使う
//		Scanner scan1 = new Scanner(new File("ファイル名"));
//		// 数値を格納に使う
//		Scanner scan2 = new Scanner(new File("ファイル名"));
		int n=100, m=0;
		
		// まず、scan1を走らせて、mを求める
		while(scan1.hasNextLine()){
			scan1.nextLine(); //次の行に移動
			m++;
		}
		scan1.close();
		// ↑これでmの値が判明、これを使ってpairListを定義する
		// pairListを定義
		int[][] pairList = new int[m][2];
		for(int i=0;i<m;i++){
			// i行目の数値を[i][0],[i][1]へ格納
			// (注):[i][1],[i][2]という書き方は誤り、[i][0],[i][1]が正しい
			pairList[i][0] = scan2.nextInt();
			pairList[i][1] = scan2.nextInt();
		}
		scan2.close();
		
		// (あと、29行目も[k][1],[k][2]となっていたので[k][0],[k][1]に直す必要があります。)
		
		// debug
		for(int i=0;i<m;i++){
			System.out.println(pairList[i][0] + "\t" + pairList[i][1]);
		}
		System.out.println(n);
		
	}

}
