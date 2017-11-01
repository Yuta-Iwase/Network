import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class InDegreeTest {

	public static void main(String[] args) throws Exception{
		Scanner scan = new Scanner(new File("web/web-Stanford.csv"));
		PrintWriter pw = new PrintWriter(new File("web/inDegreeDist.txt"));
		int N = 281903;
		int[] inDegreeList = new int[N];
		int[] inDegreeDist = new int[N+1];
		// 各頂点の入次数を計測
		while(scan.hasNextInt()) {
			scan.nextInt();
			int inNodeIndex = scan.nextInt()-1;
			inDegreeList[inNodeIndex]++;
		}
		// 入次数の分布を計算
		for(int i=0;i<N;i++) {
			inDegreeDist[inDegreeList[i]]++;
		}

		for(int i=0;i<inDegreeDist.length;i++) {
			if(inDegreeDist[i]!=0) {
				System.out.println(i + "\t" + inDegreeDist[i]);
				pw.println(i + "\t" + inDegreeDist[i]);
			}
		}

		scan.close();
		pw.close();

	}

}
