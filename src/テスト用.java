import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class テスト用{
	static int[] a ;


	public static void main(String[] args) throws Exception{
		Scanner scan1 = new Scanner(new File("Backbone_by_RW.txt"));
		Scanner scan2 = new Scanner(new File("MST_Edges.txt"));
		Scanner scan3 = new Scanner(new File("HSS_list.txt"));
		PrintWriter pw = new PrintWriter(new File("result.txt"));

		ArrayList<Integer> bList = new ArrayList<>();
		ArrayList<Integer> mList = new ArrayList<>();
		ArrayList<Integer> hList = new ArrayList<>();

		while(scan1.hasNextInt()) {
			bList.add(scan1.nextInt());
		}
		while(scan2.hasNextInt()) {
			mList.add(scan2.nextInt());
		}
		while(scan3.hasNextInt()) {
			hList.add(scan3.nextInt());
		}

		int size = bList.size();
		int matchM = 0;
		int matchH = 0;

		int currentEdge = -1;
		for(int i=0;i<bList.size();i++) {
			currentEdge = bList.get(i);
			if(mList.contains(currentEdge)) {
				matchM++;
			}
			if(hList.contains(currentEdge)) {
				matchH++;
			}
		}

		System.out.println("MSSとの一致率:" + ((double)matchM)/size);
		System.out.println("HSSとの一致率:" + ((double)matchH)/size);
		pw.println("MSSとの一致率:" + ((double)matchM)/size);
		pw.println("HSSとの一致率:" + ((double)matchH)/size);

		scan1.close();
		scan2.close();
		scan3.close();
		pw.close();

	}
}
