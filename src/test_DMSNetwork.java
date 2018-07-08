import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class test_DMSNetwork {

	public static void main(String[] args) throws Exception{
		int N = 1000;
		BarabasiAlbertNetwork BA = new BarabasiAlbertNetwork(5, N, 2, 1L);
		DMSNetwork DMS = new DMSNetwork(5, N, 2, 100);

		ArrayList<Network> netList = new ArrayList<>();
		netList.add(BA);
		netList.add(DMS);

		ArrayList<PrintWriter> pwList = new ArrayList<>();
		String desktioPath = "c:\\users\\owner\\desktop\\";
		pwList.add(new PrintWriter(new File(desktioPath + "heatmap_BA.csv")));
		pwList.add(new PrintWriter(new File(desktioPath + "heatmap_DMS.csv")));

		for(int t=0;t<netList.size();t++) {
			Network net = netList.get(t);

			int maxDegree = 0;
			for(int i=0;i<N;i++){
				if(maxDegree<net.degree[i]) maxDegree=net.degree[i];
			}
			int[][] heatMap = new int[maxDegree+1][maxDegree+1];
			for(int i=0;i<net.M;i++){
				int v0 = net.list[i][0];
				int v1 = net.list[i][1];
				int k0 = net.degree[v0];
				int k1 = net.degree[v1];
				heatMap[k0][k1]++;
				heatMap[k1][k0]++;
			}
			for(int i=0;i<maxDegree+1;i++){
				for(int j=0;j<maxDegree;j++){
					pwList.get(t).print(Math.log(heatMap[i][j]+1) + ",");
				}
				pwList.get(t).println(Math.log(heatMap[i][maxDegree]+1));
			}
			pwList.get(t).close();
		}

	}

}
