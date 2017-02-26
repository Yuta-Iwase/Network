import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

// 「US airports 500」の整頓
// (注)本来取り込むデータは「US airports」だったが誤って「US airports 500」を取り込んでしまった

public class AirportTest17_1_1_NetworkEditer {
	public static void main(String[] args) throws Exception{
		Scanner scan = new Scanner(new File("USairport500_edited.csv"));
		Scanner scan2 = new Scanner(new File("USairport500_edited.csv"));
		PrintWriter pw = new PrintWriter(new File("USairport500_weighted.csv"));
		PrintWriter pw2 = new PrintWriter(new File("USairport500_degreeList.csv"));
		PrintWriter pw3 = new PrintWriter(new File("USairport500_unweighted.csv"));

		int N=0;
		int M=0;
		int[][] list;
		int[] degree;

		int maxIndex=-1;
		while(scan2.hasNextInt()){
			maxIndex = Math.max(maxIndex, scan2.nextInt());
			maxIndex = Math.max(maxIndex, scan2.nextInt());
			scan2.nextInt();
		}
		boolean[] appNode = new boolean[maxIndex+1];
		for(int i=0;i<appNode.length;i++) appNode[i] = false;

		int cL,cR;
		double cW;
		boolean registered;
		double insertWeight;

		ArrayList<Integer> left  = new ArrayList<Integer>();
		ArrayList<Integer> right = new ArrayList<Integer>();
		ArrayList<Double> weight = new ArrayList<Double>();

		while(scan.hasNextInt()){
			registered = false;

			cL = scan.nextInt();
			cR = scan.nextInt();
			cW = scan.nextDouble();

			for(int i=0;i<left.size();i++){
				if((left.get(i)==cL&&right.get(i)==cR)||(left.get(i)==cR&&right.get(i)==cL)){
					insertWeight = (weight.get(i)+cW)/2.0;
					weight.add(i,insertWeight);
					weight.remove(i+1);
					registered = true;
				}
			}

			if(!registered){
				left.add(cL);
				right.add(cR);
				weight.add(cW);
				M++;
				appNode[cL] = true;
				appNode[cR] = true;
			}
		}

		for(int i=0;i<appNode.length;i++){
			if(appNode[i]) N++;
		}

		list = new int[M][2];
		degree = new int[N];
		for(int i=0;i<degree.length;i++) degree[i]=0;

		for(int i=0;i<left.size();i++){
			list[i][0] = Math.min(left.get(i), right.get(i));
			list[i][1] = Math.max(left.get(i), right.get(i));
			degree[list[i][0]]++;
			degree[list[i][1]]++;
			System.out.println(list[i][0] + "\t" + list[i][1] + "\t" + weight.get(i));
			pw.println(list[i][0] + "\t" + list[i][1] + "\t" + weight.get(i));
			pw3.println(list[i][0] + "\t" + list[i][1]);
		}

		System.out.println();

		for(int i=0;i<N;i++){
			System.out.println(i + "\t" + degree[i]);
			pw2.println(degree[i]);
		}

		System.out.println();
		System.out.println("N=" + N);
		System.out.println("M=" + M);


		pw.close();
		pw2.close();
		pw3.close();
		scan.close();
		scan2.close();
	}
}
