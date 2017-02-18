
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AssortTest {
	public static void main(String[] args){
		Scanner scan = null;
		try {
			File file = new File("network.csv");
			scan = new Scanner(file);
			int[][] list = new int[246][2];
			int[] degree = new int[100];
			for(int i=0;i<100;i++)degree[i]=0;
			String text;
			int tabIndex=0;
			for(int i=0;i<246;i++){
				text = scan.nextLine();
				tabIndex = text.indexOf("\t");
				list[i][0]=Integer.parseInt(text.substring(0,tabIndex));
				degree[list[i][0]]++;
				list[i][1]=Integer.parseInt(text.substring(tabIndex+1,text.length()));
				degree[list[i][1]]++;
			}

			double[] y = assort(list,degree);
			for(int x=1;x<y.length;x++){
				if(y[x]!=-123456)System.out.println(x + "\t" + y[x]);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}

	static double[] assort(int[][] list,int[] degree){
		int N = degree.length;
		int M = list.length;
		// 頂点別隣接頂点平均次数
		// Node by Adjacent Node Average Degree(NbANAD)
		double[] NbANAD = new double[N];
		for(int i=0;i<N;i++)NbANAD[i]=0;

		for(int i=0;i<M;i++){
			NbANAD[list[i][0]] += degree[list[i][1]];
			NbANAD[list[i][1]] += degree[list[i][0]];
		}
		for(int i=0;i<N;i++){
			if(degree[i]!=0)NbANAD[i] /= degree[i];
		}

		// 次数のうち最大のもの
		int maxDegree = max(degree);
		// 次数別隣接頂点平均次数
		// Degree by Adjacent Node Average Degree(DbANAD)
		double[] DbANAD = new double[maxDegree+1];
		for(int i=0;i<maxDegree+1;i++) DbANAD[i]=0;
		int nodeN;
		for(int d=0;d<=maxDegree;d++){
			nodeN=0;

			for(int i=0;i<N;i++){
				if(degree[i] == d){
					DbANAD[d] += NbANAD[i];
					nodeN++;
				}
			}
			if(nodeN!=0)DbANAD[d] /= nodeN;
			else DbANAD[d]=-123456;
		}
		return DbANAD;
	}

	
	static int max(int[] a){
		int M=0;
		for(int i=0;i<a.length;i++) M=Math.max(M, a[i]);
		return M;
	}
}
