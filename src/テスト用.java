import java.io.File;
import java.io.PrintWriter;

public class テスト用{
	public static void main(String[] args) throws Exception{
		int N = 1000;
		DMSNetwork DMS = new DMSNetwork(5, N, 2, 100);
		BarabasiAlbertNetwork BA = new BarabasiAlbertNetwork(5, N, 2, 1L);

//		PrintWriter pw3 = new PrintWriter(new File("c:\\users\\owner\\desktop\\heatmap.csv"));
//		int maxDegree = 0;
//		for(int i=0;i<N;i++){
//			if(maxDegree<DMS.degree[i]) maxDegree=DMS.degree[i];
//		}
//		int[][] heatMap = new int[maxDegree+1][maxDegree+1];
//		for(int i=0;i<DMS.M;i++){
//			int v0 = DMS.list[i][0];
//			int v1 = DMS.list[i][1];
//			int k0 = DMS.degree[v0];
//			int k1 = DMS.degree[v1];
//			heatMap[k0][k1]++;
//			heatMap[k1][k0]++;
//		}
//		for(int i=0;i<maxDegree+1;i++){
//			for(int j=0;j<maxDegree;j++){
//				pw3.print(Math.log(heatMap[i][j]+1) + ",");
//			}
//			pw3.println(Math.log(heatMap[i][maxDegree]+1));
//		}
//		pw3.close();
		
		PrintWriter pw4 = new PrintWriter(new File("c:\\users\\owner\\desktop\\heatmapBA.csv"));
		int maxDegree = 0;
		for(int i=0;i<N;i++){
			if(maxDegree<BA.degree[i]) maxDegree=BA.degree[i];
		}
		int[][] heatMap = new int[maxDegree+1][maxDegree+1];
		for(int i=0;i<BA.M;i++){
			int v0 = BA.list[i][0];
			int v1 = BA.list[i][1];
			int k0 = BA.degree[v0];
			int k1 = BA.degree[v1];
			heatMap[k0][k1]++;
			heatMap[k1][k0]++;
		}
		for(int i=0;i<maxDegree+1;i++){
			for(int j=0;j<maxDegree;j++){
				pw4.print(Math.log(heatMap[i][j]+1) + ",");
			}
			pw4.println(Math.log(heatMap[i][maxDegree]+1));
		}
		pw4.close();
	}
}
